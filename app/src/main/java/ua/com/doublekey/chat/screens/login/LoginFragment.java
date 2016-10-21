package ua.com.doublekey.chat.screens.login;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ua.com.doublekey.chat.App;
import ua.com.doublekey.chat.R;
import ua.com.doublekey.chat.common.Utils;
import ua.com.doublekey.chat.model.bus.LoginUiEvent;
import ua.com.doublekey.chat.model.bus.LoginUiUpdateEvent;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by doublekey on 21.10.2016.
 */

public class LoginFragment extends Fragment {
    @BindView(R.id.editLogin)
    EditText mEditLogin;

    @Inject
    Bus mBus;

    //// TODO: 21.10.2016 delete
    private long mClickedTime;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App) getActivity().getApplication()).getComponent().inject(this);

        mBus.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, parent, false);

        ButterKnife.bind(this, view);

        initUi();

        return view;
    }

    private void initUi(){
        Utils.hideKeyboard(getActivity());
    }

    @OnClick(R.id.buttonLogin)
    public void onButtonLoginClick(){
        mClickedTime = System.currentTimeMillis();
        mBus.post(new LoginUiEvent(LoginUiEvent.LOGIN_BUTTON_CLICK));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mBus.unregister(this);
    }

    @Subscribe
    public void onBusUiUpdate(LoginUiUpdateEvent event){
        switch (event.getType()){
            case LoginUiUpdateEvent.UPDATE_EDIT_LOGIN:
                mEditLogin.setText(String.valueOf(System.currentTimeMillis() - mClickedTime));
                break;
        }
    }

}
