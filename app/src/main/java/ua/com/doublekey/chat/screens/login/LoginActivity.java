package ua.com.doublekey.chat.screens.login;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ua.com.doublekey.chat.R;
import ua.com.doublekey.chat.base.SingleFragmentActivity;

/**
 * Created by doublekey on 21.10.2016.
 */

public class LoginActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }

}
