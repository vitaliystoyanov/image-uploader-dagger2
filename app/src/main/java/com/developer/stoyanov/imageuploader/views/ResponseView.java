package com.developer.stoyanov.imageuploader.views;

import android.os.Bundle;

import com.hannesdorfmann.mosby.mvp.MvpView;

public interface ResponseView extends MvpView, ChangeableTitle {

    void showLinks(Bundle savedInstanceState);

    void showImage(Bundle savedInstanceState);

    void showBackNavigationArrow(boolean visible);

    void scrollToBottom();

    void showFab();

    void hideFab();

}
