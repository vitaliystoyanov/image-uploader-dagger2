package com.developer.stoyanov.imageuploader.service;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.developer.stoyanov.imageuploader.MainApplication;
import com.developer.stoyanov.imageuploader.R;

import javax.inject.Inject;

public class NotificationWrapper {
    public static final int REQUEST_CODE = 22;
    private static final int NOTIFICATION_ID = 1;

    private Application context;
    @Inject
    NotificationManager notificationManager;
    @Inject
    NotificationCompat.Builder builder;

    public NotificationWrapper(Application context) {
        this.context = context;
        ((MainApplication) context).getUploaderComponent().inject(this);
        builder.setSmallIcon(R.drawable.ic_cloud_upload_small)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(context.getString(R.string.service_title_notification));
    }

    public void addIntentNotificationData(Intent intent) {
        PendingIntent contentIntent = PendingIntent.getActivity(context, REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        commitNotification();
    }

    public void showNotification(int resId) {
        builder.setContentText(context.getString(resId));
        commitNotification();
    }

    public void showProgress() {
        builder.setProgress(100, 0, true);
        builder.setOngoing(true);
        commitNotification();
    }

    public void hideProgress() {
        builder.setProgress(0, 0, false);
        builder.setOngoing(false);
        commitNotification();
    }

    private void commitNotification() {
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void cancel() {
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
