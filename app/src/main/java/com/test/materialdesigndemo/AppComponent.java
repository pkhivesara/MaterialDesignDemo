package com.test.materialdesigndemo;

import android.app.Application;
import dagger.Component;

import javax.inject.Singleton;

/**
 * Created by Pratik on 2/7/16.
 */

@Singleton
@Component(modules = {AppComponent.class})
public interface AppComponent {

    void inject(Application application);
}
