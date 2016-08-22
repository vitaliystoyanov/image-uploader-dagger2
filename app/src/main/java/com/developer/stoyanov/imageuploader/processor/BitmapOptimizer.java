package com.developer.stoyanov.imageuploader.processor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BitmapOptimizer {
    private static final String TAG = "BitmapOptimizer";

    private String pathBitmap;
    private int requiredWidth;

    public BitmapOptimizer() {
    }

    public BitmapOptimizer(String pathBitmap, int requiredWidth) {
        this.pathBitmap = pathBitmap;
        this.requiredWidth = requiredWidth;
    }

    public Bitmap decodeScaled() {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(pathBitmap), null, options);

            int scale = 1;
            while (options.outWidth / scale / 2 >= requiredWidth) {
                scale *= 2;
            }

            BitmapFactory.Options optionsResult = new BitmapFactory.Options();
            optionsResult.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(pathBitmap), null, optionsResult);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "decodeScaled: ", e);
        }
        return null;
    }

    public String getPathBitmap() {
        return pathBitmap;
    }

    public void setPathBitmap(String pathBitmap) {
        this.pathBitmap = pathBitmap;
    }

    public int getRequiredWidth() {
        return requiredWidth;
    }

    public void setRequiredWidth(int requiredWidth) {
        this.requiredWidth = requiredWidth;
    }

}
