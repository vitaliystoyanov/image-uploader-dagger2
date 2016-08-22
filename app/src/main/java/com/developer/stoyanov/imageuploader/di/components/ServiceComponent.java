package com.developer.stoyanov.imageuploader.di.components;

import android.app.NotificationManager;
import android.support.v7.app.NotificationCompat;

import com.developer.stoyanov.imageuploader.di.modules.BitmapProcessorModule;
import com.developer.stoyanov.imageuploader.di.modules.BroadcastModule;
import com.developer.stoyanov.imageuploader.di.modules.NotificationModule;
import com.developer.stoyanov.imageuploader.di.scopes.ServiceScope;
import com.developer.stoyanov.imageuploader.processor.Base64Coder;
import com.developer.stoyanov.imageuploader.processor.BitmapCompressor;
import com.developer.stoyanov.imageuploader.service.BroadcastManager;
import com.developer.stoyanov.imageuploader.service.NotificationWrapper;

import dagger.Component;

@ServiceScope
@Component(dependencies = ApplicationComponent.class,
        modules = {NotificationModule.class, BitmapProcessorModule.class, BroadcastModule.class})
public interface ServiceComponent {

    NotificationManager notificationManager();

    NotificationCompat.Builder notificationBuilder();

    NotificationWrapper notificationWrapper();

    Base64Coder coderBase64();

    BitmapCompressor bitmapCompressor();

    BroadcastManager broadcastManager();

}
