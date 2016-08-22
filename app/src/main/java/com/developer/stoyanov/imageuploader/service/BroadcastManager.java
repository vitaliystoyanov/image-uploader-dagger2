package com.developer.stoyanov.imageuploader.service;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.developer.stoyanov.imageuploader.MainApplication;
import com.developer.stoyanov.imageuploader.data.DataHandler;

import javax.inject.Inject;

public class BroadcastManager {
    private static final String TAG = "BroadcastManager";
    public static final String ACTION_SERVICE = "com.developer.stoyanov.imageuploader.service.UploadService"; // FIXME: 8/4/16 Bad
    public static final String KEY_FILEPATH = "filePath";
    public static final String KEY_RESPONSE = "response";
    public static final String KEY_STATEMENT = "statement";

    @Inject
    DataHandler dataHandler;
    private Application context;

    public BroadcastManager(Application context) {
        this.context = context;
        ((MainApplication) context).getApplicationComponent().inject(this);
    }

    public void changeStatement(int statement, String response, String filePath) {
        Log.i(TAG, "changeStatement: Service choose state on - " + statement);
        Intent intent = sent(statement, response, filePath);
        save(statement, intent);
    }

    @NonNull
    private Intent sent(int statement, String response, String filePath) {
        Intent intent = new Intent();
        intent.putExtra(KEY_RESPONSE, response);
        intent.putExtra(KEY_FILEPATH, filePath);
        intent.setAction(ACTION_SERVICE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(KEY_STATEMENT, statement);
        context.sendBroadcast(intent);
        return intent;
    }

    public void changeStatement(int statement) {
        sent(statement);
        save(statement);
    }

    private void sent(int statement) {
        Intent intent = new Intent();
        intent.setAction(ACTION_SERVICE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(KEY_STATEMENT, statement);
        context.sendBroadcast(intent);
    }

    private void save(int statement) {
        dataHandler.setStatement(statement);
    }

    private void save(int statement, Intent intent) {
        dataHandler.setStatement(statement);
        dataHandler.setData(intentToBundle(intent));
    }

    private Bundle intentToBundle(Intent intent) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_FILEPATH, intent.getStringExtra(KEY_FILEPATH));
        bundle.putString(KEY_RESPONSE, intent.getStringExtra(KEY_RESPONSE));
        return bundle;
    }

    public void load() {
        int statement = dataHandler.getStatement();
        Bundle data = dataHandler.getData();
        if (statement != 0 && data != null) {
            sent(statement, data.getString(KEY_RESPONSE), data.getString(KEY_FILEPATH));
        } else if (data == null) {
            sent(statement);
        }
    }

    public void reset() {
        dataHandler.setStatement(0);
    }
}
