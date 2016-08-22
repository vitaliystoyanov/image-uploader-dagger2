package com.developer.stoyanov.imageuploader;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar.SnackbarLayout;
import android.util.AttributeSet;
import android.view.View;

import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;

public class FloatingActionButtonBehavior extends CoordinatorLayout.Behavior<FABToolbarLayout> {

    public FloatingActionButtonBehavior(Context context, AttributeSet attrs) {
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FABToolbarLayout child, View dependency) {
        return dependency instanceof SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FABToolbarLayout child, View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }
}
