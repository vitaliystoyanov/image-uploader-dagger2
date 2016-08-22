package com.developer.stoyanov.imageuploader.processor;

import android.util.Base64;

public class Base64Coder {

    private byte[] file;

    public Base64Coder() {
    }

    public Base64Coder(byte[] file) {
        this.file = file;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String encode() {
        return Base64.encodeToString(file, Base64.DEFAULT);
    }
}
