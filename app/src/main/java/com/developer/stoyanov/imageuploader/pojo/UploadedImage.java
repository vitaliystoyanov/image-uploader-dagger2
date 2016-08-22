package com.developer.stoyanov.imageuploader.pojo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class UploadedImage {
    private static final String TAG = "UploadedImage";

    private int statusCode;
    private int code;
    private String filename;
    private String url;
    private String urlViewer;

    public UploadedImage(int statusCode, int code, String url, String urlViewer) {
        this.statusCode = statusCode;
        this.code = code;
        this.url = url;
        this.urlViewer = urlViewer;
    }

    public static UploadedImage getInstance(String jsonResponse) {
        try {
            JSONObject object = new JSONObject(jsonResponse);
            int statusCode = object.getInt("status_code");
            int code = object.getJSONObject("success").getInt("code");

            JSONObject objectImage = object.getJSONObject("image");
            String url = objectImage.getString("url");
            String urlViewer = objectImage.getString("url_viewer");
            return new UploadedImage(statusCode, code, url, urlViewer);
        } catch (JSONException e) {
            Log.e(TAG, "getInstance: ", e);
        }
        return null;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUrlFull() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlViewer() {
        return urlViewer;
    }

    public void setUrlViewer(String urlViewer) {
        this.urlViewer = urlViewer;
    }
}
