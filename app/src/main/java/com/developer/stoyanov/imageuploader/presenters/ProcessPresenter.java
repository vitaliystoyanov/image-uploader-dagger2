package com.developer.stoyanov.imageuploader.presenters;

import com.developer.stoyanov.imageuploader.service.UploadServiceStatus;
import com.developer.stoyanov.imageuploader.views.ProcessView;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

public class ProcessPresenter extends MvpBasePresenter<ProcessView> implements UploadServiceStatus {


    public void onResume() {
        if (getView() != null) {
            getView().changeTitle();
        }
    }

    public void onStart(int statement) {
        if (getView() != null) {
            if (statement == STATUS_PREPARE) {
                getView().showPrepare();
            } else if (statement == STATUS_COMPRESS) {
                getView().showCompress();
            } else if (statement == STATUS_UPLOAD) {
                getView().showUpload();
            }
        }
    }
}
