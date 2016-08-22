package com.developer.stoyanov.imageuploader.ui.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.stoyanov.imageuploader.NavigationManager;
import com.developer.stoyanov.imageuploader.R;
import com.developer.stoyanov.imageuploader.di.components.DaggerFragmentComponent;
import com.developer.stoyanov.imageuploader.pictures.PictureCapture;
import com.developer.stoyanov.imageuploader.pictures.PictureChooser;
import com.developer.stoyanov.imageuploader.presenters.GetStartedPresenter;
import com.developer.stoyanov.imageuploader.service.BroadcastManager;
import com.developer.stoyanov.imageuploader.ui.activities.MainActivity;
import com.developer.stoyanov.imageuploader.views.GetStartedView;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class GetStartedFragment extends MvpFragment<GetStartedView, GetStartedPresenter>
        implements GetStartedView, NavigationManager.NavigationListener {

    private static final String TAG = "GetStartedFragment";
    private Unbinder unbinder;

    @Inject
    BroadcastManager broadcastManager;
    @Inject
    NavigationManager navigationManager;
    @Inject
    PictureCapture pictureCapture;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.fab)
    FloatingActionsMenu fab;

    @NonNull
    @Override
    public GetStartedPresenter createPresenter() {
        return new GetStartedPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_started, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DaggerFragmentComponent.builder()
                .activityComponent(((MainActivity) getActivity()).getActivityComponent())
                .build()
                .inject(this);
        Log.d(TAG, "onCreate: Object is created: " + (pictureCapture != null));
    }

    @Override
    public void onResume() {
        super.onResume();
        broadcastManager.reset();
        navigationManager.setNavigationListener(this);
        presenter.onResume();
    }

    @OnClick(R.id.fab_menu_select)
    void onClickItemSelectPicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(Intent.createChooser(intent,
                getString(R.string.intent_chooser_title)),
                PictureChooser.REQUEST_PICK_IMAGE);
        presenter.onClickItemSelectPicture();
    }

    @OnClick(R.id.fab_menu_take_photo)
    void onClickItemTakePhoto() {
        if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.d(TAG, "onClickItemTakePhoto: true");
            pictureCapture.take();
        } else {
            Snackbar.make(coordinatorLayout, R.string.error_no_camera, Snackbar.LENGTH_LONG)
                    .show();
        }
        presenter.onClickItemTakePhoto();
    }

    @Override
    public void collapseFab() {
        if (fab.isExpanded()) fab.collapse();
    }

    @Override
    public void changeTitle() {
        ((Toolbar) getActivity().findViewById(R.id.toolbar))
                .setTitle(R.string.title_get_started);
    }

    @Override
    public void onPause() {
        navigationManager.setNavigationListener(null);
        super.onPause();
    }

    @Override
    public void onNavigationBack() {
        navigationManager.navigateBack(getActivity());
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
