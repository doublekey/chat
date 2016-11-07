package ua.com.doublekey.chat.model.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import ua.com.doublekey.chat.model.ChatMessage;

import ua.com.doublekey.chat.model.dao.ChatMessageDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig chatMessageDaoConfig;

    private final ChatMessageDao chatMessageDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        chatMessageDaoConfig = daoConfigMap.get(ChatMessageDao.class).clone();
        chatMessageDaoConfig.initIdentityScope(type);

        chatMessageDao = new ChatMessageDao(chatMessageDaoConfig, this);

        registerDao(ChatMessage.class, chatMessageDao);
    }
    
    public void clear() {
        chatMessageDaoConfig.clearIdentityScope();
    }

    public ChatMessageDao getChatMessageDao() {
        return chatMessageDao;
    }

}
