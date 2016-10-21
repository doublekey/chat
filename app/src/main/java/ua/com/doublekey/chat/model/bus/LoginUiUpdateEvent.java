package ua.com.doublekey.chat.model.bus;

/**
 * Created by doublekey on 21.10.2016.
 */

public class LoginUiUpdateEvent {
    private int type;

    public static final int UPDATE_EDIT_LOGIN = 1;

    public int getType() {
        return type;
    }

    public LoginUiUpdateEvent(int type) {
        this.type = type;
    }
}
