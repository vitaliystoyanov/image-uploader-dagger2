package com.developer.stoyanov.imageuploader.processor;

public class BitmapCompressorException extends Exception {

    @Override
    public String getMessage() {
        return "An error occurred in bitmap processor";
    }
}
