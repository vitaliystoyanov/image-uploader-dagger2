package com.developer.stoyanov.imageuploader;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.developer.stoyanov.imageuploader.ui.fragments.GetStartedFragment;
import com.developer.stoyanov.imageuploader.ui.fragments.ProcessFragment;
import com.developer.stoyanov.imageuploader.ui.fragments.ResponseFragment;

public class NavigationManager {

    public interface NavigationListener {
        void onNavigationBack();
    }

    private static final String TAG = "NavigationManager";
    private NavigationListener listener;
    private FragmentManager manager;

    public void init(FragmentManager fragmentManager) {
        manager = fragmentManager;
    }

    private void open(Fragment fragment) {
        if (manager != null) {
            manager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(fragment.toString())
                    .commit();
        }
    }

    private void openAsRoot(Fragment fragment) {
        popEveryFragment();
        open(fragment);
    }

    private void popEveryFragment() {
        int backStackCount = manager.getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            int backStackId = manager.getBackStackEntryAt(i).getId();
            manager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void navigateBack(Activity activity) {
        if (manager.getBackStackEntryCount() == 1) {
            activity.finish();
        } else {
            manager.popBackStackImmediate();
        }
    }

    public void openGetStarted() {
        openAsRoot(new GetStartedFragment());
    }

    public void openProcess(int statement) {
        openAsRoot(ProcessFragment.newInstance(statement));
    }

    public void openResponse(Bundle arguments) {
        ResponseFragment fragment = new ResponseFragment();
        fragment.setArguments(arguments);
        openAsRoot(fragment);
    }

    public void onBackPressed() {
        if (listener != null) {
            Log.d(TAG, "onBackPressed: onNavigationBack called!");
            listener.onNavigationBack();
        }
    }

    public void finish(Activity activity) {
        activity.finish();
    }

    public boolean isRootFragmentVisible() {
        return manager.getBackStackEntryCount() <= 1;
    }

    public NavigationListener getNavigationListener() {
        return listener;
    }

    public void setNavigationListener(NavigationListener listener) {
        Log.d(TAG, "setNavigationListener: listener == null? " + (listener == null));
        this.listener = listener;
    }
}
