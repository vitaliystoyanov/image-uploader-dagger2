package com.developer.stoyanov.imageuploader.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.developer.stoyanov.imageuploader.MainApplication;
import com.developer.stoyanov.imageuploader.di.components.ApplicationComponent;
import com.developer.stoyanov.imageuploader.presenters.MainPresenter;
import com.developer.stoyanov.imageuploader.views.MainView;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

public abstract class BaseActivity extends MvpActivity<MainView, MainPresenter> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent(((MainApplication) getApplication()).getApplicationComponent());
    }

    protected abstract void setupActivityComponent(ApplicationComponent applicationComponent);

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }
}
