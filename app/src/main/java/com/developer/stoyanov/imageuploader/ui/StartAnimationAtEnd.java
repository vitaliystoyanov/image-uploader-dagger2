package com.developer.stoyanov.imageuploader.ui;

import android.view.View;
import android.view.animation.Animation;

public class StartAnimationAtEnd implements Animation.AnimationListener {

    private View view;
    private Animation animation;

    public StartAnimationAtEnd(View view, Animation animation) {
        this.view = view;
        this.animation = animation;
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        view.startAnimation(this.animation);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}
