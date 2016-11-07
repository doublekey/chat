package ua.com.doublekey.chat.model.bus;

import java.util.ArrayList;

import ua.com.doublekey.chat.model.ChatMessage;

/**
 * Created by doublekey on 31.10.2016.
 */

public class XmppHelperEvent {
    public static final int CONNECTED = 1;
    public static final int COMMON_ERROR = 2;
    public static final int AUTHENTICATED = 3;
    public static final int USER_LIST = 4;
    public static final int MESSAGE_ERROR = 5;
    public static final int MESSAGE_RECEIVED = 6;

    private int type;

    private ArrayList<String> userList;

    private ChatMessage chatMessage;

    public XmppHelperEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public ArrayList<String> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<String> userList) {
        this.userList = userList;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }
}
