package com.developer.stoyanov.imageuploader.presenters;

import android.os.Bundle;

import com.developer.stoyanov.imageuploader.service.UploadServiceStatus;
import com.developer.stoyanov.imageuploader.views.MainView;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

public class MainPresenter extends MvpBasePresenter<MainView> implements UploadServiceStatus {

    public void onClickChangeLanguage() {
        if (getView() != null) {
            getView().showLanguageDialog();
        }
    }

    public void onClickOpenWebsite() {
        if (getView() != null) {
            getView().openWebsite();
        }
    }

    public void onClickHome() {
        if (getView() != null) {
            getView().showGetStarted();
        }
    }

    public void onReceive(int state, Bundle bundle) {
        if (getView() != null) {
            if (state == STATUS_PREPARE || state == STATUS_COMPRESS
                    || state == STATUS_UPLOAD) {
                getView().goToProcessView(state);
                getView().hideToolbar();
            } else if (state == STATUS_RESPONSE) {
                getView().goToResponseView(bundle);
                getView().showToolbar();
            } else if (state == STATUS_NETWORK_ERROR) {
                getView().showToolbar();
                getView().showError();
            }
        }
    }
}
