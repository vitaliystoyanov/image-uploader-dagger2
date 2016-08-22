package com.developer.stoyanov.imageuploader.ui.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.stoyanov.imageuploader.NavigationManager;
import com.developer.stoyanov.imageuploader.R;
import com.developer.stoyanov.imageuploader.di.components.DaggerFragmentComponent;
import com.developer.stoyanov.imageuploader.pojo.UploadedImage;
import com.developer.stoyanov.imageuploader.presenters.ResponsePresenter;
import com.developer.stoyanov.imageuploader.service.BroadcastManager;
import com.developer.stoyanov.imageuploader.ui.activities.MainActivity;
import com.developer.stoyanov.imageuploader.views.MainView;
import com.developer.stoyanov.imageuploader.views.ResponseView;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ResponseFragment extends MvpFragment<ResponseView, ResponsePresenter>
        implements ResponseView, NavigationManager.NavigationListener {

    private static final String TAG = "ResponseFragment";
    private Bundle arguments;
    private Unbinder unbinder;
    @Inject
    NavigationManager navigationManager;
    @Inject
    ClipboardManager clipboard;

    @BindView(R.id.fabtoolbar_layout)
    FABToolbarLayout fabToolbarLayout;
    @BindView(R.id.textview_filename)
    TextView filename;
    @BindView(R.id.imageview_uploaded)
    ImageView imageUploaded;
    @BindView(R.id.edittext_link_viewer)
    EditText linkViewer;
    @BindView(R.id.edittext_link)
    EditText linkFull;
    @BindView(R.id.fabtoolbar_fab)
    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_response, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DaggerFragmentComponent.builder()
                .activityComponent(((MainActivity) getActivity()).getActivityComponent()) // FIXME: 8/16/16 a strong reference on an object
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    public ResponsePresenter createPresenter() {
        return new ResponsePresenter();
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            presenter.onViewStateRestored(savedInstanceState);
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void showImage(Bundle savedInstanceState) {
        String path = savedInstanceState.getString(BroadcastManager.KEY_FILEPATH);
        if (path != null) {
            File file = new File(path);
            Picasso.with(getContext())
                    .load(file)
                    .into(imageUploaded, new Callback() {
                        @Override
                        public void onSuccess() {
                            if (imageUploaded != null) {
                                Animation fadeOut = new AlphaAnimation(0, 1);
                                fadeOut.setInterpolator(new AccelerateInterpolator());
                                fadeOut.setDuration(300);
                                imageUploaded.startAnimation(fadeOut);
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });
            String fileName = getString(R.string.field_filename) + " " + file.getName();
            filename.setText(fileName);
        }
    }

    @Override
    public void showLinks(Bundle savedInstanceState) {
        String JSONResponse = savedInstanceState.getString(BroadcastManager.KEY_RESPONSE);
        Log.i(TAG, "showLinks: JSONResponse - " + JSONResponse);
        UploadedImage uploadedImage = UploadedImage.getInstance(JSONResponse);
        if (uploadedImage != null && uploadedImage.getStatusCode() == 200) {
            linkViewer.setText(uploadedImage.getUrlViewer());
            linkFull.setText(uploadedImage.getUrlFull());
        } else {
            if (getActivity() instanceof MainView) {
                ((MainView) getActivity()).showError(); // FIXME: 8/18/16 A bad approach.
            } else {
                throw new RuntimeException("A class MainView must be implemented!");
            }
            Log.d(TAG, "showLinks: An image didn't uploaded, your API status response don't equal 200");
        }
    }

    @Override
    public void scrollToBottom() {
//        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    @Override
    public void onResume() {
        super.onResume();
        navigationManager.setNavigationListener(this);
        arguments = getArguments();
        presenter.onResume(arguments);
    }

    @Override
    public void showBackNavigationArrow(boolean visible) {
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(visible);
        }
    }

    @Override
    public void changeTitle() {
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_response);
        }
    }

    @Override
    public void hideFab() {
        fabToolbarLayout.hide();
        fabToolbarLayout.animate()
                .translationY(-fabToolbarLayout.getBottom())
                .setInterpolator(new AccelerateInterpolator())
                .start();
    }

    @Override
    public void showFab() {
        fabToolbarLayout.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    @OnClick(R.id.share_copy)
    public void copyLinkViewer() {
        String copyData = linkViewer.getText().toString();
        copyToClipboard(copyData);
        fabToolbarLayout.hide();
    }

/*    @OnClick(R.id.button_copy)
    public void copyLinkFull() {
        String copyData = linkFull.getText().toString();
        copyToClipboard(copyData);
    }*/

    private void copyToClipboard(String copyData) {
        ClipData clip = ClipData.newPlainText("Link", copyData);
        clipboard.setPrimaryClip(clip);
        Snackbar.make(fab, getString(R.string.link_copied), Snackbar.LENGTH_LONG).show(); // FIXME: 8/19/16 Change snackbar behavior
    }

    @OnClick(R.id.share_send_email)
    public void shareEmail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_TEXT, linkViewer.getText().toString());
        startActivity(Intent.createChooser(intent, getString(R.string.intent_chooser_email)));
    }

    @OnClick(R.id.share_social)
    public void shareSocial() {
        ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(getActivity());
        intentBuilder.setChooserTitle(R.string.intent_chooser_social_app)
                .setType("text/plain")
                .setText(linkViewer.getText().toString())
                .startChooser();
    }

    @OnClick(R.id.share_open_in_browser)
    public void shareOpenInBrowser() {
        String url = linkViewer.getText().toString();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(url));
        if (browserIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(browserIntent);
        }
    }

    @OnClick(R.id.fabtoolbar_fab)
    public void onClickFabShare() {
        fabToolbarLayout.show();
    }

    @Override
    public void onNavigationBack() {
        navigationManager.openGetStarted();
    }

    @Override
    public void onPause() {
        navigationManager.setNavigationListener(null);
        presenter.onPause();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState = arguments;
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
