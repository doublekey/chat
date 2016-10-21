package ua.com.doublekey.chat;

import android.app.Application;

import ua.com.doublekey.chat.di.AppModule;
import ua.com.doublekey.chat.di.DaggerIComponent;
import ua.com.doublekey.chat.di.IComponent;
import ua.com.doublekey.chat.screens.login.LoginPresenter;

/**
 * Created by doublekey on 03.10.2016.
 */
public class App extends Application {
    private IComponent mComponent;

    private LoginPresenter mPresenterLogin;

    @Override
    public void onCreate() {
        super.onCreate();

        mComponent = DaggerIComponent
                .builder()
                .appModule(new AppModule())
                .build();

        mPresenterLogin = LoginPresenter.newInstance(this);
    }

    public IComponent getComponent(){
        return mComponent;
    }

}
