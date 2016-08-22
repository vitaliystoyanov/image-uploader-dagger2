package com.developer.stoyanov.imageuploader.processor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BitmapCompressor {

    private static final String TAG = "BitmapCompressor";
    private static final long MAX_SIZE_IMAGE_COMPRESSED = 614400;
    private static final long MAX_SIZE_IMAGE = 1024 * 1024 * 5;
    private OnStateChangeListener listener;
    private File file;

    public void setFile(File file) {
        this.file = file;
    }

    public boolean checkImageSize() {
        return file.length() < MAX_SIZE_IMAGE;
    }

    public byte[] compress() throws BitmapCompressorException {
        stateChange(OnStateChangeListener.BEGINNING);
        ByteArrayOutputStream output = null;
        FileInputStream inputStream = null;
        try {
            output = new ByteArrayOutputStream();
            inputStream = new FileInputStream(file);
            Bitmap decodedBitmap = BitmapFactory.decodeStream(inputStream);
            if (decodedBitmap == null) {
                throw new BitmapCompressorException();
            }
            if (file.length() <= MAX_SIZE_IMAGE_COMPRESSED) {
                decodedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
                Log.i(TAG, "compress: Normal size - " + (output.size() / 1024) + " KB");
                stateChange(OnStateChangeListener.FINISHED);
                return output.toByteArray();
            } else {
                stateChange(OnStateChangeListener.COMPRESSING);
                Log.i(TAG, "compress: Compressing image. Image compressor run.");
                byte[] bufferStream = processBitmap(decodedBitmap);
                if (bufferStream != null) return bufferStream;
            }
        } catch (FileNotFoundException e) {
            throw new BitmapCompressorException();
        } finally {
            try {
                if (output != null) output.close();
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                Log.e(TAG, "compress: ", e);
            }
        }
        return null;
    }

    private byte[] processBitmap(Bitmap factory) {
        int quality = 95;
        long size;
        while (quality > 0) {
            ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();
            try {
                factory.compress(Bitmap.CompressFormat.JPEG, quality, bufferStream);
                size = bufferStream.size();
                Log.i(TAG, "compress: compress size: " + size + " B, " + (size / 1024) + " KB");
                if (size <= MAX_SIZE_IMAGE_COMPRESSED) {
                    Log.i(TAG, "compress: UploadedImage size after compression - "
                            + (bufferStream.size() / 1024) + " KB");
                    stateChange(OnStateChangeListener.FINISHED);
                    return bufferStream.toByteArray();
                }
                quality -= 5;
                Log.i(TAG, "compress: quality - " + quality);
            } finally {
                try {
                    bufferStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "processBitmap: ", e);
                }
            }
        }
        return null;
    }

    public void addOnStateChangeListener(OnStateChangeListener listener) {
        this.listener = listener;
    }

    private void stateChange(int state) {
        if (listener != null) {
            listener.onStateChanged(state);
        }
    }

    public static interface OnStateChangeListener {

        int BEGINNING = 1;
        int COMPRESSING = 2;
        int FINISHED = 3;

        void onStateChanged(int state);
    }
}
