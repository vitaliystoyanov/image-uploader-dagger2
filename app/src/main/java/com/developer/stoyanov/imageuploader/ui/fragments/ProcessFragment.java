package com.developer.stoyanov.imageuploader.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.stoyanov.imageuploader.NavigationManager;
import com.developer.stoyanov.imageuploader.R;
import com.developer.stoyanov.imageuploader.di.components.DaggerFragmentComponent;
import com.developer.stoyanov.imageuploader.presenters.ProcessPresenter;
import com.developer.stoyanov.imageuploader.ui.StartAnimationAtEnd;
import com.developer.stoyanov.imageuploader.ui.activities.MainActivity;
import com.developer.stoyanov.imageuploader.views.ProcessView;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProcessFragment extends MvpFragment<ProcessView, ProcessPresenter>
        implements ProcessView, NavigationManager.NavigationListener {

    private static final String TAG = "ProcessFragment";
    private static final String KEY_SERVICE_STATEMENT = "serviceStatement";

    private Animation animationScaleUp;
    private Animation animationScaleDown;
    private Animation animationRotate;
    private Animation animationUpDown;

    @Inject
    NavigationManager navigationManager;

    @BindView(R.id.status_progress)
    TextView status;
    @BindView(R.id.image_status_progress)
    ImageView imageStatus;
    @BindString(R.string.status_prepare)
    String textPrepare;
    @BindString(R.string.status_compress)
    String textCompress;
    @BindString(R.string.status_upload)
    String textUpload;

    public static ProcessFragment newInstance(int serviceStatement) {
        ProcessFragment instance = new ProcessFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_SERVICE_STATEMENT, serviceStatement);
        instance.setArguments(args);
        return instance;
    }

    @NonNull
    @Override
    public ProcessPresenter createPresenter() {
        return new ProcessPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        ButterKnife.bind(this, view);
        initAnimations();
        return view;
    }

    private void initAnimations() {
        animationScaleUp = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up);
        animationScaleDown = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_down);
        animationRotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_centre);
        animationUpDown = AnimationUtils.loadAnimation(getActivity(), R.anim.up_down);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DaggerFragmentComponent.builder()
                .activityComponent(((MainActivity) getActivity()).getActivityComponent()) // FIXME: 8/16/16 a strong reference on an object
                .build()
                .inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart(getArguments().getInt(KEY_SERVICE_STATEMENT));
    }

    @Override
    public void onResume() {
        super.onResume();
        navigationManager.setNavigationListener(this);
        presenter.onResume();
    }

    public void animateProcessCompress() {
        imageStatus.setImageResource(R.drawable.ic_process);
        animationScaleUp.setAnimationListener(new StartAnimationAtEnd(imageStatus, animationRotate));
        imageStatus.startAnimation(animationScaleUp);
    }

    public void animateProcessUpload() {
        imageStatus.setImageResource(R.drawable.ic_cloud);
        animationScaleUp.setAnimationListener(new StartAnimationAtEnd(imageStatus, animationUpDown));
        imageStatus.startAnimation(animationScaleUp);
    }

    @Override
    public void changeTitle() {
        ((Toolbar) getActivity().findViewById(R.id.toolbar)) // FIXME: 8/16/16 Move to ButterKnife. It's a bad approach for logic
                .setTitle(R.string.title_uploading);
    }

    @Override
    public void showPrepare() {
        imageStatus.setImageResource(R.drawable.ic_start);
        status.setText(textPrepare);
    }

    @Override
    public void showCompress() {
        status.setText(textCompress);
        animateProcessCompress();
    }

    @Override
    public void showUpload() {
        status.setText(textUpload);
        animateProcessUpload();
    }

    @Override
    public void onNavigationBack() {
        navigationManager.finish(getActivity());
    }

    @Override
    public void onPause() {
        navigationManager.setNavigationListener(null);
        super.onPause();
    }
}
