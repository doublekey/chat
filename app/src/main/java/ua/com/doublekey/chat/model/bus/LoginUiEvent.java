package ua.com.doublekey.chat.model.bus;

/**
 * Created by doublekey on 21.10.2016.
 */

public class LoginUiEvent {
    public static final int LOGIN_BUTTON_CLICK = 1;

    private int type;

    public int getType() {
        return type;
    }

    public LoginUiEvent(int type) {
        this.type = type;
    }
}
