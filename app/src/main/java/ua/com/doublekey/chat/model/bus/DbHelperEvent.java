package ua.com.doublekey.chat.model.bus;

import java.util.List;

import ua.com.doublekey.chat.model.ChatMessage;

/**
 * Created by doublekey on 06.11.2016.
 *
 */

public class DbHelperEvent {

    public static final int MESSAGE_LIST = 1;

    private int type;

    public int getType() {
        return type;
    }

    private List<ChatMessage> messageList;

    public DbHelperEvent(int type) {
        this.type = type;
    }

    public List<ChatMessage> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<ChatMessage> messageList) {
        this.messageList = messageList;
    }
}
