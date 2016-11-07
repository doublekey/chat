package ua.com.doublekey.chat.screens.chat;

import android.support.v4.app.Fragment;

import ua.com.doublekey.chat.base.SingleFragmentActivity;
import ua.com.doublekey.chat.screens.login.LoginFragment;

/**
 * Created by doublekey on 04.11.2016.
 */

public class ChatActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ChatFragment();
    }

}
