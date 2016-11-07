package ua.com.doublekey.chat.di;

import android.database.sqlite.SQLiteDatabase;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ua.com.doublekey.chat.App;
import ua.com.doublekey.chat.common.DbHelper;
import ua.com.doublekey.chat.common.XmppHelper;
import ua.com.doublekey.chat.model.dao.DaoMaster;
import ua.com.doublekey.chat.model.dao.DaoSession;

/**
 * Created by doublekey on 03.10.2016.
 *
 */
@Module
public class AppModule {

    private final App mApp;

    public AppModule(App app) {
        this.mApp = app;
    }

    @Provides
    @Singleton
    public Bus provideBus(){
        return new Bus(ThreadEnforcer.ANY);
    }

    @Provides
    @Singleton
    public XmppHelper provideXmppHelper() {
        return new XmppHelper(mApp);
    }

    @Provides
    @Singleton
    DaoSession provideDaoSession() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mApp, "chat-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        return daoMaster.newSession();
    }

    @Provides
    @Singleton
    public DbHelper provideDbHelper() {
        return new DbHelper(mApp);
    }

}
