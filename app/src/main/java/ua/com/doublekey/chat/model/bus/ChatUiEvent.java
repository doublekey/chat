package ua.com.doublekey.chat.model.bus;

import ua.com.doublekey.chat.model.ChatMessage;

/**
 * Created by doublekey on 05.11.2016.
 *
 */

public class ChatUiEvent {
    public static final int REQUEST_INIT = 1;
    public static final int MESSAGE_SENT = 2;
    public static final int CLOSE = 3;

    private int type;

    private ChatMessage chatMessage;

    public ChatUiEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }
}
