package ua.com.doublekey.chat.di;

import javax.inject.Singleton;

import dagger.Component;
import ua.com.doublekey.chat.screens.login.LoginFragment;
import ua.com.doublekey.chat.screens.login.LoginPresenter;


/**
 * Created by doublekey on 03.10.2016.
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
}
