package ua.com.doublekey.chat.di;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by doublekey on 03.10.2016.
 */
@Module
public class AppModule {

    @Provides
    @Singleton
    public Bus provideBus(){
        return new Bus(ThreadEnforcer.ANY);
    }

}
