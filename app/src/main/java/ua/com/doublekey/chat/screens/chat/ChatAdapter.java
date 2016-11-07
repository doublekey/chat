package ua.com.doublekey.chat.screens.chat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ua.com.doublekey.chat.R;
import ua.com.doublekey.chat.model.ChatMessage;

/**
 * Created by doublekey on 03.10.2016.
 *
 * Shows the own messages on the right side and the others on the left side
 *
 */

public class ChatAdapter extends BaseAdapter {

    private static LayoutInflater mInflatter = null;
    private ArrayList<ChatMessage> mMessageList;

    public ChatAdapter(Activity activity, ArrayList<ChatMessage> list) {
        mMessageList = list;
        mInflatter = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return mMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage message = mMessageList.get(position);

        View view = convertView;
        if (convertView == null) view = mInflatter.inflate(R.layout.item_chat, null);

        TextView textMessage = (TextView) view.findViewById(R.id.message_text);
        textMessage.setText(message.getMessage());
        LinearLayout layout = (LinearLayout) view
                .findViewById(R.id.bubble_layout);
        LinearLayout parent_layout = (LinearLayout) view
                .findViewById(R.id.bubble_layout_parent);

        //If the message is mine aligns it to the right
        if (message.isMine()) {
            layout.setBackgroundResource(R.drawable.bubble_right);
            parent_layout.setGravity(Gravity.END);
        }
        //If the message is not mine aligns it to the left
        else {
            layout.setBackgroundResource(R.drawable.bubble_left);
            parent_layout.setGravity(Gravity.START);
        }
        textMessage.setTextColor(Color.BLACK);
        return view;
    }

}
