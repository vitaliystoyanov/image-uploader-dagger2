package com.developer.stoyanov.imageuploader.di.modules;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.NotificationCompat;

import com.developer.stoyanov.imageuploader.service.NotificationWrapper;

import dagger.Module;
import dagger.Provides;

@Module
public class NotificationModule {

    @Provides
    NotificationManager provideNotificationManager(Application application) {
        return (NotificationManager) application
                .getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Provides
    NotificationCompat.Builder provideNotificationBuilder(Application context) {
        return new NotificationCompat.Builder(context);
    }

    @Provides
    NotificationWrapper provideNotificationWrapper(Application context) {
        return new NotificationWrapper(context);
    }
}
