package com.developer.stoyanov.imageuploader.di.modules;

import android.app.Application;
import android.content.ClipboardManager;
import android.content.Context;

import com.developer.stoyanov.imageuploader.data.DataHandler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return application;
    }

    @Provides
    @Singleton
    DataHandler providesDatabaseHandler() {
        return new DataHandler(application);
    }

    @Provides
    @Singleton
    ClipboardManager provideClipboardManager() {
        return (ClipboardManager) application
                .getSystemService(Context.CLIPBOARD_SERVICE);
    }
}
