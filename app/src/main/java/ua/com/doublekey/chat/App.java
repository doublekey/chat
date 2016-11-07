package ua.com.doublekey.chat;

import android.app.Application;

import javax.inject.Inject;

import ua.com.doublekey.chat.di.AppModule;
import ua.com.doublekey.chat.di.DaggerIComponent;
import ua.com.doublekey.chat.di.IComponent;
import ua.com.doublekey.chat.screens.chat.ChatPresenter;
import ua.com.doublekey.chat.screens.login.LoginPresenter;

/**
 * Created by doublekey on 03.10.2016.
 */
public class App extends Application {

    private IComponent mComponent;

    //The presenters wire
    private LoginPresenter mLoginPresenter;
    private ChatPresenter mChatPresenter;

    @Override
    public void onCreate() {
        super.onCreate();

        mComponent = DaggerIComponent
                .builder()
                .appModule(new AppModule(this))
                .build();

        mLoginPresenter = LoginPresenter.newInstance(this);
        mChatPresenter = ChatPresenter.newInstance(this);
    }

    public IComponent getComponent(){
        return mComponent;
    }


}
