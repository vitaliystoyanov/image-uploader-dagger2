package com.developer.stoyanov.imageuploader.network;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public class BaseURL {
    private static final String BASE_API_URL = "https://pic.co.ua/api/1/upload";
    private static final String TAG = "BaseURL";

    private URL apiUrl;

    public BaseURL() {
        try {
            apiUrl = new URL(BASE_API_URL);
        } catch (MalformedURLException e) {
            Log.e(TAG, "BaseURL: ", e);
        }
    }

    public URL getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(URL apiUrl) {
        this.apiUrl = apiUrl;
    }
}
