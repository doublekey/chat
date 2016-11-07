package ua.com.doublekey.chat.screens.chat;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ua.com.doublekey.chat.App;
import ua.com.doublekey.chat.R;
import ua.com.doublekey.chat.common.Utils;
import ua.com.doublekey.chat.model.ChatMessage;
import ua.com.doublekey.chat.model.bus.ChatUiEvent;
import ua.com.doublekey.chat.model.bus.ChatUiUpdateEvent;

/**
 * Created by doublekey on 04.11.2016.
 *
 * The main chat fragment
 * It communicates with the presenter by bus.
 * It sends {@link ChatUiEvent} and updates the UI in {@link #onBusUiUpdate(ChatUiUpdateEvent)}
 */

public class ChatFragment extends Fragment {

    @Inject
    Bus mBus;

    @BindView(R.id.listMessages)
    ListView mListMessages;

    @BindView(R.id.editMessage)
    EditText mEditMessage;

    private String mUser;
    private String mOpponent;
    private static ArrayList<ChatMessage> mChatlist;
    private static ChatAdapter mChatAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App) getActivity().getApplication()).getComponent().inject(this);

        mBus.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, parent, false);

        ButterKnife.bind(this, view);

        //Gets the current UI state from the presenter (in case of rotation etc)
        ChatUiEvent event = new ChatUiEvent(ChatUiEvent.REQUEST_INIT);
        mBus.post(event);

        return view;
    }

    /**
     * Receives the messages from the presenter and updates the UI
     * @param event
     */
    @Subscribe
    public void onBusUiUpdate(ChatUiUpdateEvent event){
        switch (event.getType()){
            case ChatUiUpdateEvent.INIT:
                initUi(event);

                break;

            case ChatUiUpdateEvent.MESSAGE_RECEIVED:
                mChatlist.add(event.getChatMessage());
                mChatAdapter.notifyDataSetChanged();

                break;

            case ChatUiUpdateEvent.CACHE_MESSAGES_RECEIVED:
                mChatlist.addAll(event.getChatList());
                mChatAdapter.notifyDataSetChanged();

                break;
        }
    }

    /**
     * Initializes the UI by the data saved in the presenter
     * @param event
     */
    private void initUi(ChatUiUpdateEvent event){
        mUser = event.getUser();
        mOpponent = event.getOpponent();

        Utils.hideKeyboard(getActivity());

        //Sets the title
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (mOpponent != null && actionBar != null){
            actionBar.setTitle(getString(R.string.chat_with) + " " + mOpponent);
        }

        //Sets the list auto scroll
        mListMessages.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        mListMessages.setStackFromBottom(true);

        //Initializes the list
        mChatlist = new ArrayList<>();
        mChatlist.addAll(event.getChatList());

        mChatAdapter = new ChatAdapter(getActivity(), mChatlist);
        mListMessages.setAdapter(mChatAdapter);
    }

    @OnClick(R.id.imageSend)
    public void onSendClick(){
        sendMessage(mEditMessage.getText().toString());
    }

    /**
     * Shows the message in the list and sends the event to the presenter
     * @param message
     */
    public void sendMessage(String message) {
        if (message.equalsIgnoreCase("")) return;

        mEditMessage.setText("");

        ChatMessage chatMessage = new ChatMessage(
                mUser,
                mOpponent,
                message,
                true);

        mChatlist.add(chatMessage);
        mChatAdapter.notifyDataSetChanged();

        ChatUiEvent event = new ChatUiEvent(ChatUiEvent.MESSAGE_SENT);
        event.setChatMessage(chatMessage);
        mBus.post(event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mBus.post(new ChatUiEvent(ChatUiEvent.CLOSE));

        mBus.unregister(this);
    }


}
