package ua.com.doublekey.chat.model.bus;

import java.util.ArrayList;

/**
 * Created by doublekey on 21.10.2016.
 */

public class LoginUiUpdateEvent {
    private int type;

    private String user;
    private String password;
    private boolean isAuthorised;
    private ArrayList<String> userList;

    public static final int INIT = 1;
    public static final int AUTHENTICATED = 2;
    public static final int ERROR = 3;
    public static final int USER_LIST = 4;

    public int getType() {
        return type;
    }

    public ArrayList<String> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<String> userList) {
        this.userList = userList;
    }

    public LoginUiUpdateEvent(int type) {
        this.type = type;
    }

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

    public boolean isAuthorised() {
        return isAuthorised;
    }

    public void setAuthorised(boolean authorised) {
        isAuthorised = authorised;
    }
}
