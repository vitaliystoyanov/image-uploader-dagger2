package com.developer.stoyanov.imageuploader.di.modules;

import android.support.v4.app.FragmentManager;

import com.developer.stoyanov.imageuploader.NavigationManager;
import com.developer.stoyanov.imageuploader.di.scopes.ActivityScope;
import com.developer.stoyanov.imageuploader.ui.activities.MainActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private MainActivity mainActivity;

    public ActivityModule(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Provides
    @ActivityScope
    MainActivity provideMainActivity() {
        return mainActivity;
    }

    @Provides
    @ActivityScope
    FragmentManager provideFragmentManager(MainActivity activity) {
        return activity.getSupportFragmentManager();
    }

    @Provides
    @ActivityScope
    NavigationManager navigationManager(FragmentManager manager) {
        NavigationManager navigationManager = new NavigationManager();
        navigationManager.init(manager);
        return navigationManager;
    }
}
