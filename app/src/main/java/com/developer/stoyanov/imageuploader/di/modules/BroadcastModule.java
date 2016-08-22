package com.developer.stoyanov.imageuploader.di.modules;

import android.app.Application;

import com.developer.stoyanov.imageuploader.service.BroadcastManager;

import dagger.Module;
import dagger.Provides;

@Module
public class BroadcastModule {

    @Provides
    BroadcastManager provideBroadcastManager(Application context) {
        return new BroadcastManager(context);
    }
}
