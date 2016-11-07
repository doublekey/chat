package ua.com.doublekey.chat.screens.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ua.com.doublekey.chat.App;
import ua.com.doublekey.chat.R;
import ua.com.doublekey.chat.common.AppConstants;
import ua.com.doublekey.chat.common.Utils;
import ua.com.doublekey.chat.model.bus.LoginUiEvent;
import ua.com.doublekey.chat.model.bus.LoginUiUpdateEvent;
import ua.com.doublekey.chat.screens.chat.ChatActivity;

/**
 * Created by doublekey on 21.10.2016.
 *
 * The first screen fragment
 * It communicates with the presenter by bus.
 * It sends {@link LoginUiEvent} and updates the UI in {@link #onBusUiUpdate(LoginUiUpdateEvent)}
 */

public class LoginFragment extends Fragment {
    @BindView(R.id.editLogin)
    EditText mEditLogin;

    @BindView(R.id.editPassword)
    EditText mEditPassword;

    @BindView(R.id.textSelectUser)
    TextView mTextSelectUser;

    @BindView(R.id.listUsers)
    ListView mListUsers;

    @Inject
    Bus mBus;

    private ArrayList<String> mUserList;

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

        //Gets the current UI state from the presenter (in case of rotation etc)
        mBus.post(new LoginUiEvent(LoginUiEvent.REQUEST_INIT));

        return view;
    }


    private void initUi(LoginUiUpdateEvent event){
        Utils.hideKeyboard(getActivity());

        if (event.getUser() != null) mEditLogin.setText(event.getUser());
        if (event.getPassword() != null) mEditPassword.setText(event.getPassword());
        if (event.isAuthorised()){
            showUsersList(true);
            fillUserList(event.getUserList());
        }
        else{
            showUsersList(false);
        }
    }

    @OnClick(R.id.buttonLogin)
    public void onButtonLoginClick(){
        LoginUiEvent event = new LoginUiEvent(LoginUiEvent.LOGIN_BUTTON_CLICK);
        event.setUser(mEditLogin.getText().toString());
        event.setPassword(mEditPassword.getText().toString());
        event.setDomain(AppConstants.DOMAIN);
        mBus.post(event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mBus.unregister(this);
    }

    @Subscribe
    public void onBusUiUpdate(LoginUiUpdateEvent event){
        switch (event.getType()){
            case LoginUiUpdateEvent.AUTHENTICATED:
                Utils.hideKeyboard(getActivity());
                showUsersList(true);
                break;
            case LoginUiUpdateEvent.ERROR:
                Utils.showOkDialog(getActivity(), getString(R.string.common_authorisation_error));
                break;
            case LoginUiUpdateEvent.USER_LIST:
                fillUserList(event.getUserList());
                break;
            case LoginUiUpdateEvent.INIT:
                initUi(event);
                break;
        }
    }

    /**
     * Shows the users list to select somebody to chat with after authentication
     * @param show
     */
    private void showUsersList(boolean show){
        if (show){
            mTextSelectUser.setVisibility(View.VISIBLE);
            mListUsers.setVisibility(View.VISIBLE);
        }
        else{
            mTextSelectUser.setVisibility(View.GONE);
            mListUsers.setVisibility(View.GONE);
        }
    }

    /**
     * Creates the potential opponents list so user could select who to chat with
     * @param userList
     */
    public void fillUserList(ArrayList<String> userList) {
        if (userList == null) return;

        mUserList = userList;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.item_user_list, userList);

        mListUsers.setAdapter(adapter);
        mListUsers.setOnItemClickListener((adapterView, view, i, l)
                -> onUserNameClick(mUserList.get(i).split("@")[0]));
    }

    /**
     * Sends the data to the presenter of the next screen
     * and opens the proper activity
     * @param userName
     */
    private void onUserNameClick(String userName){
        LoginUiEvent event = new LoginUiEvent(LoginUiEvent.OPPONENT_SELECTED);
        event.setUser(mEditLogin.getText().toString());
        event.setOpponent(userName);
        mBus.post(event);

        Intent intent = new Intent(getActivity(), ChatActivity.class);
        startActivity(intent);
    }

}
