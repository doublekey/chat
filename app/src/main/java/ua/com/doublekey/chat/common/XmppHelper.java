package ua.com.doublekey.chat.common;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.squareup.otto.Bus;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import ua.com.doublekey.chat.App;
import ua.com.doublekey.chat.model.ChatMessage;
import ua.com.doublekey.chat.model.bus.XmppHelperEvent;

/**
 * Created by doublekey on 31.10.2016.
 *
 * Holds all the necessaries to work with OpenFire chat server
 * It runs all the methods in the separate treads and returns the results in the UI tread
 * by the event bus
 *
 * It use Smack library
 */

public class XmppHelper {

    @Inject
    Bus mBus;

    private Handler mUiHandler;

    private String mDomain;
    private String mUser;
    private String mPassword;
    private String mOpponent;
    private XMPPTCPConnection mConnection;
    private MessageListener mMessageListener;
    private CustomChatManagerListener mChatManagerListener;
    private Chat mChat;

    private boolean mIsConnected = false;

    private Gson mGson;

    /**
     * The listener for all the operations
     * It sends events trough the event bus in UI tread
     */
    public class XMPPConnectionListener implements ConnectionListener {
        @Override
        public void connected(XMPPConnection connection) {
            postBusEvent(new XmppHelperEvent(XmppHelperEvent.CONNECTED));
        }

        @Override
        public void authenticated(XMPPConnection connection, boolean resumed) {
            postBusEvent(new XmppHelperEvent(XmppHelperEvent.AUTHENTICATED));
        }

        @Override
        public void connectionClosed() {
        }

        @Override
        public void connectionClosedOnError(Exception e) {
        }

        @Override
        public void reconnectionSuccessful() {
        }

        @Override
        public void reconnectingIn(int seconds) {
        }

        @Override
        public void reconnectionFailed(Exception e) {
        }
    }

    /**
     * The opponent message listener
     * It sends the received messages through the bus
     */
    private class MessageListener implements ChatMessageListener {
        public MessageListener(Context context) {
        }

        /**
         * Invoked when the message is received
         *
         * @param chat
         * @param message
         */
        @Override
        public void processMessage(Chat chat, Message message) {
            if (message.getType() == Message.Type.chat
                    && message.getBody() != null) {
                new Thread(() -> {
                    ChatMessage chatMessage = mGson.fromJson(
                            message.getBody(), ChatMessage.class);

                    if (!chatMessage.getSender().equals(mOpponent)) return;

                    chatMessage.setMine(false);

                    XmppHelperEvent event = new XmppHelperEvent(XmppHelperEvent.MESSAGE_RECEIVED);
                    event.setChatMessage(chatMessage);
                    postBusEvent(event);
                }).start();
            }
        }

    }

    private class CustomChatManagerListener implements ChatManagerListener {
        @Override
        public void chatCreated(final org.jivesoftware.smack.chat.Chat chat,
                                final boolean createdLocally) {
            if (!createdLocally)
                chat.addMessageListener(mMessageListener);

        }
    }

    public boolean isConnected() {
        return mIsConnected;
    }

    public XmppHelper(App app) {
        app.getComponent().inject(this);

        //Saves the UI handler (this constructor is started from the main UI tread)
        mUiHandler = new Handler();

        mMessageListener = new MessageListener(app);
        mChatManagerListener = new CustomChatManagerListener();

        mGson = new Gson();

        mBus.register(this);
    }


    /**
     * Tries to login
     * Saves the attributes in success
     * Sends the proper event trough the bus
     * @param user
     * @param password
     */
    public void login(String user, String password){
        mUser = user;
        mPassword = password;

        new Thread(() -> {
            try {
                mConnection.login(user, password);
            } catch (Exception e) {
                postBusEvent(new XmppHelperEvent(XmppHelperEvent.COMMON_ERROR));
            }
        }).start();
    }

    /**
     * Initializes the connection used for all operations
     * and saves it in {@link #mConnection}
     * @param domain
     */
    public void connect(String domain) {
        mDomain = domain;

        new Thread(() -> {
            XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration
                    .builder();
            config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            config.setServiceName(domain);
            config.setHost(domain);
            config.setPort(5222);
            config.setDebuggerEnabled(true);
            XMPPTCPConnection.setUseStreamManagementResumptiodDefault(true);
            XMPPTCPConnection.setUseStreamManagementDefault(true);
            mConnection = new XMPPTCPConnection(config.build());
            XMPPConnectionListener connectionListener = new XMPPConnectionListener();
            mConnection.addConnectionListener(connectionListener);
            try {
                mConnection.connect();
            }
            catch (Exception e){
                postBusEvent(new XmppHelperEvent(XmppHelperEvent.COMMON_ERROR));
            }
        }).start();
    }

    /**
     * Posts the event through the bus in UI thread
     * @param event
     */
    private void postBusEvent(XmppHelperEvent event){
        mUiHandler.post(() -> {
            mBus.post(event);
        });
    }

    /**
     * Gets the all users list and sends it through the bus
     * It used after successful connection
     */
    public void getUserList(){
        new Thread(() -> {
            try {
                Roster roster = Roster.getInstanceFor(mConnection);
                if (!roster.isLoaded()) roster.reloadAndWait();
                Collection<RosterEntry> entries = roster.getEntries();

                ArrayList<String> usersList = new ArrayList<>();
                for (RosterEntry entry : entries){
                    usersList.add(entry.getName().split("@")[0]);
                }

                XmppHelperEvent event = new XmppHelperEvent(XmppHelperEvent.USER_LIST);
                event.setUserList(usersList);
                postBusEvent(event);
            }
            catch (Exception e){
                postBusEvent(new XmppHelperEvent(XmppHelperEvent.COMMON_ERROR));
            }
        }).start();
    }

    /**
     * Sends the message
     * @param chatMessage
     */
    public void sendMessage(ChatMessage chatMessage) {
        new Thread(() -> {
            String body = mGson.toJson(chatMessage);

            Message message = new Message();
            message.setBody(body);
            message.setStanzaId(String.valueOf(chatMessage.getDateTime()));
            message.setType(Message.Type.chat);

            try {
                if (mConnection.isAuthenticated()) {
                    mChat.sendMessage(message);
                }
                else {
                    login(mUser, mPassword);
                }
            }
            catch (Exception e) {
                postBusEvent(new XmppHelperEvent(XmppHelperEvent.MESSAGE_ERROR));
            }
        }).start();
    }

    /**
     * Sets the chat with selected opponent
     * @param opponent
     */
    public void setOpponent(String opponent){
        if (mConnection == null) return;

        mOpponent = opponent;

        new Thread(() -> {
            ChatManager.getInstanceFor(mConnection).addChatListener(
                    mChatManagerListener);

            mChat = ChatManager.getInstanceFor(mConnection).createChat(
                    opponent + "@" + mDomain,
                    mMessageListener);
        }).start();
    }

}
