package ua.com.doublekey.chat.model.bus;

/**
 * Created by doublekey on 21.10.2016.
 */

public class LoginUiEvent {
    public static final int LOGIN_BUTTON_CLICK = 1;
    public static final int REQUEST_INIT = 2;
    public static final int OPPONENT_SELECTED = 3;

    private int type;

    private String user;
    private String opponent;
    private String password;
    private String domain;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getType() {
        return type;
    }

    public LoginUiEvent(int type) {
        this.type = type;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }
}
