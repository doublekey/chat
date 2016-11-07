package ua.com.doublekey.chat.model.bus;

import java.util.List;

import ua.com.doublekey.chat.model.ChatMessage;

/**
 * Created by doublekey on 05.11.2016.
 *
 */

public class ChatUiUpdateEvent {

    public static final int INIT = 1;
    public static final int MESSAGE_RECEIVED = 2;
    public static final int CACHE_MESSAGES_RECEIVED = 3;

    private int type;

    private String user;
    private String opponent;
    private ChatMessage chatMessage;
    private List<ChatMessage> chatList;

    public ChatUiUpdateEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public List<ChatMessage> getChatList() {
        return chatList;
    }

    public void setChatList(List<ChatMessage> chatList) {
        this.chatList = chatList;
    }
}
