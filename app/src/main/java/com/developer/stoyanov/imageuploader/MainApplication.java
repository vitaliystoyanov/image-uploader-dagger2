package com.developer.stoyanov.imageuploader;

import android.app.Application;

import com.developer.stoyanov.imageuploader.di.components.ApplicationComponent;
import com.developer.stoyanov.imageuploader.di.components.DaggerApplicationComponent;
import com.developer.stoyanov.imageuploader.di.components.DaggerServiceComponent;
import com.developer.stoyanov.imageuploader.di.components.DaggerUploaderComponent;
import com.developer.stoyanov.imageuploader.di.components.ServiceComponent;
import com.developer.stoyanov.imageuploader.di.components.UploaderComponent;
import com.developer.stoyanov.imageuploader.di.modules.ApplicationModule;
import com.developer.stoyanov.imageuploader.di.modules.BitmapProcessorModule;
import com.developer.stoyanov.imageuploader.di.modules.BroadcastModule;
import com.developer.stoyanov.imageuploader.di.modules.NotificationModule;

public class MainApplication extends Application {

    private UploaderComponent uploaderComponent;
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        ServiceComponent serviceComponent = DaggerServiceComponent.builder()
                .applicationComponent(applicationComponent)
                .bitmapProcessorModule(new BitmapProcessorModule())
                .broadcastModule(new BroadcastModule())
                .notificationModule(new NotificationModule())
                .build();

        uploaderComponent = DaggerUploaderComponent.builder()
                .serviceComponent(serviceComponent)
                .build();
    }

    public UploaderComponent getUploaderComponent() {
        return uploaderComponent;
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
