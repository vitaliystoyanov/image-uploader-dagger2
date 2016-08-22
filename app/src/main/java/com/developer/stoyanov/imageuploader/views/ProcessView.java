package com.developer.stoyanov.imageuploader.views;

import com.hannesdorfmann.mosby.mvp.MvpView;

public interface ProcessView extends MvpView, ChangeableTitle {

    void showPrepare();

    void showCompress();

    void showUpload();

}
