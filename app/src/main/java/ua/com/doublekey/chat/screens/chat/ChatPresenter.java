package ua.com.doublekey.chat.screens.chat;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ua.com.doublekey.chat.App;
import ua.com.doublekey.chat.common.AppConstants;
import ua.com.doublekey.chat.common.DbHelper;
import ua.com.doublekey.chat.common.XmppHelper;
import ua.com.doublekey.chat.model.ChatMessage;
import ua.com.doublekey.chat.model.bus.ChatUiEvent;
import ua.com.doublekey.chat.model.bus.ChatUiUpdateEvent;
import ua.com.doublekey.chat.model.bus.DbHelperEvent;
import ua.com.doublekey.chat.model.bus.LoginUiEvent;
import ua.com.doublekey.chat.model.bus.XmppHelperEvent;

/**
 * Created by doublekey on 04.11.2016.
 *
 * The singleton chat screen presenter
 * It created in {@link App}
 *
 * It uses the bus for communicate with view and helpers.
 * All the bus methods have "onBus' prefix
 */

public class ChatPresenter {
    private static ChatPresenter mInstance = null;

    @Inject
    Bus mBus;

    @Inject
    XmppHelper mXmppHelper;

    @Inject
    DbHelper mDbHelper;

    private String mUser;
    private String mOpponent;
    private static ArrayList<ChatMessage> mChatList = new ArrayList<>();

    public void setApp(App app) {
        app.getComponent().inject(this);

        mBus.register(this);
    }

    public static ChatPresenter newInstance(App app) {
        if (mInstance == null){
            mInstance = new ChatPresenter();
            mInstance.setApp(app);
        }

        return mInstance;
    }

    /**
     * Processes the view UI events
     * @param event
     */
    @Subscribe
    public void onBusUiEvent(ChatUiEvent event) {
        switch (event.getType()) {
            case ChatUiEvent.REQUEST_INIT:
                ChatUiUpdateEvent updateEvent = new ChatUiUpdateEvent(ChatUiUpdateEvent.INIT);
                updateEvent.setUser(mUser);
                updateEvent.setOpponent(mOpponent);
                updateEvent.setChatList(mChatList);

                mBus.post(updateEvent);

                break;

            case ChatUiEvent.MESSAGE_SENT:
                mChatList.add(event.getChatMessage());
                mXmppHelper.sendMessage(event.getChatMessage());

                break;

            case ChatUiEvent.CLOSE:
                //Saves the messages in the db
                mDbHelper.saveChatMessages(mChatList, AppConstants.MESSAGES_TO_SAVE,
                        mUser, mOpponent);

                break;
        }
    }

    /**
     * Catches the Select user event of the login activity to save the necessary data
     * @param event
     */
    @Subscribe
    public void onBusLoginUiEvent(LoginUiEvent event) {
        if (event.getType() != LoginUiEvent.OPPONENT_SELECTED) return;

        mUser = event.getUser();
        mOpponent = event.getOpponent();
        mChatList.clear();

        mXmppHelper.setOpponent(mOpponent);

        //Gets the cached messages for the selected opponent
        mDbHelper.getChatMessages(mUser, mOpponent);

    }

    @Subscribe
    public void onBusXmppHelperEvent(XmppHelperEvent event) {
        switch (event.getType()) {
            case XmppHelperEvent.MESSAGE_RECEIVED:
                mChatList.add(event.getChatMessage());

                ChatUiUpdateEvent messageEvent
                        = new ChatUiUpdateEvent(ChatUiUpdateEvent.MESSAGE_RECEIVED);
                messageEvent.setChatMessage(event.getChatMessage());
                mBus.post(messageEvent);

                break;
        }
    }

    /**
     * Receives the cached messages and updates the UI
     * @param event
     */
    @Subscribe
    public void onBusDbHelperEvent(DbHelperEvent event) {
        switch (event.getType()){
            case DbHelperEvent.MESSAGE_LIST:
                correctMyAttribute(event.getMessageList());

                mChatList.addAll(event.getMessageList());

                ChatUiUpdateEvent updateEvent =
                        new ChatUiUpdateEvent(ChatUiUpdateEvent.CACHE_MESSAGES_RECEIVED);
                updateEvent.setChatList(event.getMessageList());

                mBus.post(updateEvent);
                break;
        }

    }

    /**
     * Corrects the My attribute of the messages list
     * It useful for situations when user logs in with another account
     * @param messageList
     */
    private void correctMyAttribute(List<ChatMessage> messageList){
        for (ChatMessage chatMessage : messageList){
            chatMessage.setMine(chatMessage.getSender().equals(mUser));
        }
    }
}
