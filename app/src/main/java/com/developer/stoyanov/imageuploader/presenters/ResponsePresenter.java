package com.developer.stoyanov.imageuploader.presenters;

import android.os.Bundle;

import com.developer.stoyanov.imageuploader.views.ResponseView;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

public class ResponsePresenter extends MvpBasePresenter<ResponseView> {

    public void onResume(Bundle arguments) {
        if (getView() != null) {
            getView().changeTitle();
//            getView().hideFab();
            getView().showBackNavigationArrow(true);
            getView().showImage(arguments);
            getView().showLinks(arguments);
//            getView().showFab();
//            getView().scrollToBottom();
        }
    }

    public void onViewStateRestored(Bundle savedInstanceState) {
        if (getView() != null) {
            getView().showLinks(savedInstanceState); // FIXME: 8/19/16
            getView().showImage(savedInstanceState);
        }
    }

    public void onPause() {
        if (getView() != null) {
            getView().showBackNavigationArrow(false);
        }
    }
}
