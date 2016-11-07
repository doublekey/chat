package ua.com.doublekey.chat.common;

import android.os.Handler;

import com.squareup.otto.Bus;

import org.greenrobot.greendao.query.Query;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import ua.com.doublekey.chat.App;
import ua.com.doublekey.chat.model.ChatMessage;
import ua.com.doublekey.chat.model.bus.DbHelperEvent;
import ua.com.doublekey.chat.model.dao.ChatMessageDao;
import ua.com.doublekey.chat.model.dao.DaoSession;

/**
 * Created by doublekey on 06.11.2016.
 *
 * This helper uses greenDAO
 *
 * It runs every process in separate threads and returns the results in UI tread by the bus
 */

public class DbHelper {

    @Inject
    DaoSession mDaoSession;

    @Inject
    Bus mBus;

    private Handler mUiHandler;

    public DbHelper(App app) {
        app.getComponent().inject(this);

        //Saves the UI handler (this constructor is started from the main UI tread)
        mUiHandler = new Handler();
    }

    /**
     * Saves only #numberToSave message objects
     * @param messageList
     * @param numberToSave
     * @param opponent this param is needed to clear first the old messages with this opponent
     */
    public void saveChatMessages(List<ChatMessage> messageList, int numberToSave,
                                 String user, String opponent){
        new Thread(() -> {
            clearChatMessages(user, opponent);

            int minNumber = Math.min(messageList.size(), numberToSave);

            for (int i = messageList.size() - minNumber; i < messageList.size(); i++){
                //Clears id for messages which was taken from the cache so they could be
                //regenerated automatically
                messageList.get(i).setId(null);

                mDaoSession.getChatMessageDao().insert(messageList.get(i));
            }
        }).start();
    }


    /**
     * Deletes all the messages of the chat with selected opponent
     * @param user
     * @param opponent
     */
    private void clearChatMessages(String user, String opponent){
        Query query = mDaoSession.getChatMessageDao().queryBuilder()
                .where(ChatMessageDao.Properties.Sender.eq(user),
                        ChatMessageDao.Properties.Receiver.eq(opponent))
                .build();

        mDaoSession.getChatMessageDao().deleteInTx(query.list());

        query = mDaoSession.getChatMessageDao().queryBuilder()
                .where(ChatMessageDao.Properties.Sender.eq(opponent),
                        ChatMessageDao.Properties.Receiver.eq(user))
                .build();

        mDaoSession.getChatMessageDao().deleteInTx(query.list());
    }


    /**
     * Returns the all messages with the selected opponent
     * and sends them trough the bus
     * @param user
     * @param opponent
     */
    public void getChatMessages(String user, String opponent){
        new Thread(() -> {
            //As should be used messages where current user is sender and the opponent is receiver
            //and vice versa there should be 2 request combined
            Query query = mDaoSession.getChatMessageDao().queryBuilder()
                    .where(ChatMessageDao.Properties.Sender.eq(user),
                            ChatMessageDao.Properties.Receiver.eq(opponent))
                    .build();
            List<ChatMessage> queryList = query.list();
            query = mDaoSession.getChatMessageDao().queryBuilder()
                    .where(ChatMessageDao.Properties.Sender.eq(opponent),
                            ChatMessageDao.Properties.Receiver.eq(user))
                    .build();
            queryList.addAll(query.list());

            //Sorts combined list by id
            sortChatMessages(queryList);

            DbHelperEvent event = new DbHelperEvent(DbHelperEvent.MESSAGE_LIST);
            event.setMessageList(queryList);

            postBusEvent(event);
        }).start();
    }

    /**
     * Posts the event through the bus in UI thread
     * @param event
     */
    private void postBusEvent(DbHelperEvent event){
        mUiHandler.post(() -> {
            mBus.post(event);
        });
    }

    /**
     * Sorts the combined messages list by id
     * Used in {@link #getChatMessages(String, String)}
     */
    private void sortChatMessages(List<ChatMessage> list){
        Collections.sort(list, new Comparator<ChatMessage>() {
            public int compare(ChatMessage first, ChatMessage second) {
                return first.getId().compareTo(second.getId());
            }
        });
    }

}
