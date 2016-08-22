package com.developer.stoyanov.imageuploader.di.modules;

import android.app.Application;

import com.developer.stoyanov.imageuploader.di.scopes.ActivityScope;
import com.developer.stoyanov.imageuploader.pictures.PictureCapture;
import com.developer.stoyanov.imageuploader.pictures.PictureChooser;
import com.developer.stoyanov.imageuploader.ui.activities.MainActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class PictureModule {

    @Provides
    @ActivityScope
    PictureCapture providePictureCapture(Application context, MainActivity activity) {
        return new PictureCapture(context, activity);
    }

    @Provides
    @ActivityScope
    PictureChooser providePictureChooser(Application context) {
        return new PictureChooser(context);
    }
}
