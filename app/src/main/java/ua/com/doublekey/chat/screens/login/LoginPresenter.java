package ua.com.doublekey.chat.screens.login;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import ua.com.doublekey.chat.App;
import ua.com.doublekey.chat.model.bus.LoginUiEvent;
import ua.com.doublekey.chat.model.bus.LoginUiUpdateEvent;

/**
 * Created by doublekey on 21.10.2016.
 */

public class LoginPresenter {
    @Inject
    Bus mBus;

    private App mApp;

    public void setApp(App mApp) {
        this.mApp = mApp;

        mApp.getComponent().inject(this);

        mBus.register(this);
    }

    public static LoginPresenter newInstance(App app) {
        LoginPresenter loginPresenter = new LoginPresenter();
        loginPresenter.setApp(app);
        return loginPresenter;
    }

    @Subscribe
    public void onBusUiEvent(LoginUiEvent event) {
        switch (event.getType()){
            case LoginUiEvent.LOGIN_BUTTON_CLICK:
                mBus.post(new LoginUiUpdateEvent(LoginUiUpdateEvent.UPDATE_EDIT_LOGIN));
                break;
        }

    }



}
