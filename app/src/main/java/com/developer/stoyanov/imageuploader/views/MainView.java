package com.developer.stoyanov.imageuploader.views;

import android.os.Bundle;

import com.hannesdorfmann.mosby.mvp.MvpView;

public interface MainView extends MvpView {

    void showLanguageDialog();

    void showGetStarted();

    void showError();

    void openWebsite();

    void goToProcessView(int statement);

    void goToResponseView(Bundle arguments);

    void hideToolbar();

    void showToolbar();

}
