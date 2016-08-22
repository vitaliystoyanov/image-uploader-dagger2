package com.developer.stoyanov.imageuploader.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.afollestad.materialdialogs.MaterialDialog;
import com.developer.stoyanov.imageuploader.NavigationManager;
import com.developer.stoyanov.imageuploader.R;
import com.developer.stoyanov.imageuploader.di.components.ActivityComponent;
import com.developer.stoyanov.imageuploader.di.components.ApplicationComponent;
import com.developer.stoyanov.imageuploader.di.components.DaggerActivityComponent;
import com.developer.stoyanov.imageuploader.di.modules.ActivityModule;
import com.developer.stoyanov.imageuploader.di.qualifiers.LanguageDialog;
import com.developer.stoyanov.imageuploader.di.qualifiers.RetryDialog;
import com.developer.stoyanov.imageuploader.pictures.PictureCapture;
import com.developer.stoyanov.imageuploader.pictures.PictureChooser;
import com.developer.stoyanov.imageuploader.service.BroadcastManager;
import com.developer.stoyanov.imageuploader.service.UploadService;
import com.developer.stoyanov.imageuploader.views.MainView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements MainView {
    private static final String TAG = "MainActivity";
    private static final String EXTRA_FILE_PATH = "filePath";
    private UploaderReceiver receiver;
    private ActivityComponent activityComponent;

    @Inject
    BroadcastManager broadcastManager;
    @Inject
    PictureChooser pictureChooser;
    @Inject
    PictureCapture pictureCapture;
    @Inject
    Intent openBrowserIntent;
    @Inject
    IntentFilter intentFilterReceiver;
    @Inject
    NavigationManager navigationManager;
    @Inject
    @LanguageDialog
    MaterialDialog materialDialog;
    @Inject
    @RetryDialog
    MaterialDialog retryDialog;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar.setTitle(R.string.title_get_started);
        setSupportActionBar(toolbar);
        setupReceiver();
        navigationManager.openGetStarted();
    }

    @Override
    protected void setupActivityComponent(ApplicationComponent appComponent) {
        activityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(appComponent)
                .build();
        activityComponent.inject(this);
        Log.d(TAG, "onCreate: Object is injected: " + (navigationManager != null));
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    private void setupReceiver() {
        receiver = new UploaderReceiver();
        registerReceiver(receiver, intentFilterReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_open_web_site) {
            presenter.onClickOpenWebsite();
        } else if (id == android.R.id.home) {
            presenter.onClickHome();
        } else if (id == R.id.action_languages) {
            presenter.onClickChangeLanguage();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode - " + requestCode);
        if (requestCode == PictureCapture.REQUEST_TAKE_PICTURE && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: took");
            String currentPicturePath = pictureCapture.getCurrentPicturePath();
            if (currentPicturePath != null) {
                Log.d(TAG, "onActivityResult: REQUEST_TAKE_PICTURE: currentPicturePath - "
                        + currentPicturePath);
                pictureCapture.galleryAddPicture();
                startUploadService(currentPicturePath);
            }
        } else if (requestCode == PictureChooser.REQUEST_PICK_IMAGE &&
                resultCode == RESULT_OK && data != null) {
            String picturePath = pictureChooser.pictureFromMediaStore(data.getData());
            Log.i(TAG, "onActivityResult: REQUEST_PICK_IMAGE picturePath = " + picturePath);
            if (picturePath != null) {
                Log.d(TAG, "onActivityResult: startUploadService(picturePath)");
                startUploadService(picturePath);
            }
        }
    }

    private void startUploadService(String picturePath) {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, UploadService.class);
        intent.putExtra(EXTRA_FILE_PATH, picturePath);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        broadcastManager.load();
        if (receiver == null) {
            Log.i(TAG, "onResume: receiver == null");
            receiver = new UploaderReceiver();
            registerReceiver(receiver, intentFilterReceiver);
        }
    }

    @Override
    public void showGetStarted() {
        navigationManager.openGetStarted();
    }

    @Override
    public void showError() {
        navigationManager.openGetStarted();
        retryDialog.show();
    }

    @Override
    public void showLanguageDialog() {
        materialDialog.show();
    }

    @Override
    public void openWebsite() {
        startActivity(openBrowserIntent);
    }

    @Override
    public void goToProcessView(int statement) {
        navigationManager.openProcess(statement);
    }

    @Override
    public void goToResponseView(Bundle arguments) {
        navigationManager.openResponse(arguments);
    }

    @Override
    public void hideToolbar() {
        appBarLayout.animate()
                .translationY(-toolbar.getBottom())
                .setInterpolator(new AccelerateInterpolator())
                .start();
    }

    @Override
    public void showToolbar() {
        appBarLayout.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    @Override
    public void onBackPressed() {
        navigationManager.onBackPressed();
    }

    public class UploaderReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: statement = "
                    + intent.getIntExtra(BroadcastManager.KEY_STATEMENT, -1));
            int statement = intent.getIntExtra(BroadcastManager.KEY_STATEMENT, -1);
            String filePath = intent.getStringExtra(BroadcastManager.KEY_FILEPATH);
            String response = intent.getStringExtra(BroadcastManager.KEY_RESPONSE);
            if (statement != -1) {
                Bundle bundle = new Bundle();
                bundle.putString(BroadcastManager.KEY_FILEPATH, filePath);
                bundle.putString(BroadcastManager.KEY_RESPONSE, response);
                presenter.onReceive(statement, bundle);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
