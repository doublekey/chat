package ua.com.doublekey.chat.di;

import javax.inject.Singleton;

import dagger.Component;
import ua.com.doublekey.chat.common.DbHelper;
import ua.com.doublekey.chat.common.XmppHelper;
import ua.com.doublekey.chat.screens.chat.ChatFragment;
import ua.com.doublekey.chat.screens.chat.ChatPresenter;
import ua.com.doublekey.chat.screens.login.LoginFragment;
import ua.com.doublekey.chat.screens.login.LoginPresenter;


/**
 * Created by doublekey on 03.10.2016.
 *
 */
@Singleton
@Component(
        modules = {
                AppModule.class
        }
)
public interface IComponent {
    void inject(LoginPresenter loginPresenter);
    void inject(LoginFragment loginFragment);

    void inject(ChatPresenter chatPresenter);
    void inject(ChatFragment chatFragment);

    void inject(XmppHelper xmppHelper);
    void inject(DbHelper dbHelper);
}
