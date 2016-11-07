package ua.com.doublekey.chat.screens.login;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import javax.inject.Inject;

import ua.com.doublekey.chat.App;
import ua.com.doublekey.chat.common.XmppHelper;
import ua.com.doublekey.chat.model.bus.LoginUiEvent;
import ua.com.doublekey.chat.model.bus.LoginUiUpdateEvent;
import ua.com.doublekey.chat.model.bus.XmppHelperEvent;

/**
 * Created by doublekey on 21.10.2016.
 *
 * The singleton login screen presenter
 * It created in {@link App}
 *
 * It uses the bus for communicate with view and helpers.
 * All the bus methods have "onBus' prefix
 */

public class LoginPresenter {

    private static LoginPresenter mInstance = null;

    @Inject
    Bus mBus;

    @Inject
    XmppHelper mXmppHelper;

    //UI data
    private String mUser;
    private String mPassword;
    private boolean mIsAuthorised = false;
    private ArrayList<String> mUserList;

    public void setApp(App app) {
        app.getComponent().inject(this);

        mBus.register(this);
    }

    public static LoginPresenter newInstance(App app) {
        if (mInstance == null){
            mInstance = new LoginPresenter();
            mInstance.setApp(app);
        }

        return mInstance;
    }

    /**
     * Processes the view UI events
     * @param event
     */
    @Subscribe
    public void onBusUiEvent(LoginUiEvent event) {
        switch (event.getType()){
            case LoginUiEvent.LOGIN_BUTTON_CLICK:
                mUser = event.getUser();
                mPassword = event.getPassword();

                //Makes the connection to the server first
                //Then it will use login and password to connect
                //in #onBusXmppHelperEvent
                if (!mXmppHelper.isConnected()){
                    mXmppHelper.connect(event.getDomain());
                }
                else{
                    mXmppHelper.login(mUser, mPassword);
                }

                break;

            case LoginUiEvent.REQUEST_INIT:
                //Sends saved data to the view
                LoginUiUpdateEvent uiUpdateEvent = new LoginUiUpdateEvent(LoginUiUpdateEvent.INIT);
                uiUpdateEvent.setUser(mUser);
                uiUpdateEvent.setPassword(mPassword);
                uiUpdateEvent.setAuthorised(mIsAuthorised);
                uiUpdateEvent.setUserList(mUserList);
                mBus.post(uiUpdateEvent);

                break;
        }

    }


    @Subscribe
    public void onBusXmppHelperEvent(XmppHelperEvent event) {
        switch (event.getType()) {
            case XmppHelperEvent.CONNECTED:
                mXmppHelper.login(mUser, mPassword);
                break;
            case XmppHelperEvent.AUTHENTICATED:
                mIsAuthorised = true;
                mBus.post(new LoginUiUpdateEvent(LoginUiUpdateEvent.AUTHENTICATED));
                mXmppHelper.getUserList();
                break;
            case XmppHelperEvent.COMMON_ERROR:
                mBus.post(new LoginUiUpdateEvent(LoginUiUpdateEvent.ERROR));
                break;
            case XmppHelperEvent.USER_LIST:
                mUserList = event.getUserList();
                LoginUiUpdateEvent updateEvent
                        = new LoginUiUpdateEvent(LoginUiUpdateEvent.USER_LIST);
                updateEvent.setUserList(mUserList);
                mBus.post(updateEvent);
                break;
        }

    }

}
