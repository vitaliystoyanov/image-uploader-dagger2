package com.developer.stoyanov.imageuploader.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.developer.stoyanov.imageuploader.MainApplication;
import com.developer.stoyanov.imageuploader.R;
import com.developer.stoyanov.imageuploader.network.BaseURL;
import com.developer.stoyanov.imageuploader.network.MultipartHTTPRequest;
import com.developer.stoyanov.imageuploader.processor.Base64Coder;
import com.developer.stoyanov.imageuploader.processor.BitmapCompressor;
import com.developer.stoyanov.imageuploader.processor.BitmapCompressorException;
import com.developer.stoyanov.imageuploader.ui.activities.MainActivity;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

public class UploadService extends IntentService
        implements BitmapCompressor.OnStateChangeListener, UploadServiceStatus {
    private static final String TAG = "UploadService";

    public static final String EXTRA_FILE_PATH = "filePath";
    public static final String MULTIPART_KEY = "key";
    public static final String MULTIPART_SOURCE = "source";

    @Inject
    BroadcastManager broadcastManager;
    @Inject
    NotificationWrapper notificationWrapper;
    @Inject
    BitmapCompressor bitmapCompressor;
    @Inject
    Base64Coder base64Coder;

    public UploadService() {
        super(UploadService.class.getName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((MainApplication) getApplication()).getUploaderComponent().inject(this);
        notificationWrapper.addIntentNotificationData(new Intent(this, MainActivity.class));
        bitmapCompressor.addOnStateChangeListener(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String filePath = intent.getStringExtra(EXTRA_FILE_PATH);
        Log.i(TAG, "onHandleIntent: Service accepted such a file path - " + filePath);
        bitmapCompressor.setFile(new File(filePath));
        if (filePath != null) {
            if (bitmapCompressor.checkImageSize()) {
                broadcastManager.changeStatement(STATUS_PREPARE);
                notificationWrapper.showProgress();
                byte[] compressedFile = compress();
                if (compressedFile != null) {
                    String response = upload(compressedFile);
                    if (response != null) {
                        Log.i(TAG, "onHandleIntent: Server response body - " + response);
                        broadcastManager.changeStatement(STATUS_RESPONSE, response, filePath);
                        notificationWrapper.showNotification(R.string.service_complete);
                        notificationWrapper.hideProgress();
                    } else {
                        broadcastManager.changeStatement(STATUS_NETWORK_ERROR);
                        notificationWrapper.cancel();
                    }
                }
            } else {
                broadcastManager.changeStatement(STATUS_LARGE_FILE);
                notificationWrapper.cancel();
            }
        } else {
            broadcastManager.changeStatement(STATUS_ERROR_FILEPATH);
            notificationWrapper.cancel();
        }
    }

    private byte[] compress() {
        try {
            return bitmapCompressor.compress();
        } catch (BitmapCompressorException e) {
            Log.e(TAG, "compress: ", e);
            broadcastManager.changeStatement(STATUS_ERROR_FILEPATH);
            notificationWrapper.cancel();
        }
        return null;
    }

    private String upload(byte[] rawFile) {
        try {
            base64Coder.setFile(rawFile);
            MultipartHTTPRequest httpRequest = new MultipartHTTPRequest(new BaseURL().getApiUrl());
            httpRequest.addFormField(MULTIPART_KEY, getString(R.string.api_client_id));
            httpRequest.addFormField(MULTIPART_SOURCE, base64Coder.encode());
            broadcastManager.changeStatement(STATUS_UPLOAD);
            notificationWrapper.showNotification(R.string.service_upload);
            byte[] result = httpRequest.finish();
            return result != null ? new String(result) : null;
        } catch (IOException e) {
            Log.e(TAG, "upload: ", e);
            broadcastManager.changeStatement(STATUS_NETWORK_ERROR);
            notificationWrapper.cancel();
        }
        return null;
    }

    @Override
    public void onStateChanged(int state) {
        Log.i(TAG, "onStateChanged: bitmapCompressor changed state on - " + state);
        if (state == BitmapCompressor.OnStateChangeListener.COMPRESSING) {
            broadcastManager.changeStatement(STATUS_COMPRESS);
            notificationWrapper.showNotification(R.string.service_compress);
        }
    }
}