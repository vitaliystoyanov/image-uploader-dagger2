package com.developer.stoyanov.imageuploader.presenters;

import com.developer.stoyanov.imageuploader.views.GetStartedView;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

public class GetStartedPresenter extends MvpBasePresenter<GetStartedView> {


    public void onClickItemTakePhoto() {
        if (getView() != null) {
            getView().collapseFab();
        }
    }

    public void onClickItemSelectPicture() {
        if (getView() != null) {
            getView().collapseFab();
        }
    }

    public void onResume() {
        if (getView() != null) {
            getView().changeTitle();
        }
    }
}
