package com.developer.stoyanov.imageuploader.di.components;

import android.app.Application;
import android.content.ClipboardManager;

import com.developer.stoyanov.imageuploader.NavigationManager;
import com.developer.stoyanov.imageuploader.di.modules.ActivityModule;
import com.developer.stoyanov.imageuploader.di.modules.BroadcastModule;
import com.developer.stoyanov.imageuploader.di.modules.IntentModule;
import com.developer.stoyanov.imageuploader.di.modules.PictureModule;
import com.developer.stoyanov.imageuploader.di.modules.UIModule;
import com.developer.stoyanov.imageuploader.di.scopes.ActivityScope;
import com.developer.stoyanov.imageuploader.pictures.PictureCapture;
import com.developer.stoyanov.imageuploader.ui.activities.MainActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class,
        modules = {ActivityModule.class, IntentModule.class, UIModule.class,
                PictureModule.class, BroadcastModule.class})
public interface ActivityComponent {

    Application application();

    PictureCapture pictureCapture();

    ClipboardManager clipboardManager();

    NavigationManager navigationManager();

    void inject(MainActivity mainActivity);

}
