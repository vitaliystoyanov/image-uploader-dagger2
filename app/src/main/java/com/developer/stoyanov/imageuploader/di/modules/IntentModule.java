package com.developer.stoyanov.imageuploader.di.modules;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

import com.developer.stoyanov.imageuploader.service.BroadcastManager;

import dagger.Module;
import dagger.Provides;

@Module
public class IntentModule {
    private static final String WEBSITE = "https://pic.co.ua";

    @Provides
    IntentFilter provideIntentFilter() {
        IntentFilter intentFilter = new IntentFilter(BroadcastManager.ACTION_SERVICE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        return intentFilter;
    }

    @Provides
    Intent provideIntent() {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(WEBSITE));
    }
}
