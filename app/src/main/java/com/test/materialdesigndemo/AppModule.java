package com.test.materialdesigndemo;

import android.app.Application;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Created by Pratik on 2/7/16.
 */

@Module
public class AppModule {

    Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application getAppContext() {
        return application;
    }
}
