package com.developer.stoyanov.imageuploader.di.components;

import android.app.Application;
import android.content.ClipboardManager;

import com.developer.stoyanov.imageuploader.data.DataHandler;
import com.developer.stoyanov.imageuploader.di.modules.ApplicationModule;
import com.developer.stoyanov.imageuploader.service.BroadcastManager;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Application application();

    DataHandler databaseHandler();

    ClipboardManager clipboardManager();

    void inject(BroadcastManager broadcastManager);

}
