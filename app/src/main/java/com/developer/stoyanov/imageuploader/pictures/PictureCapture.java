package com.developer.stoyanov.imageuploader.pictures;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.developer.stoyanov.imageuploader.ui.activities.MainActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

public class PictureCapture {
    private static final String TAG = "PictureCapture";
    public static final int REQUEST_TAKE_PICTURE = 2;
    private static final String PREFIX_FILE = "file:";

    private MainActivity mainActivity;
    private Application context;
    private String currentPicturePath;

    public PictureCapture(Application context, MainActivity activity) {
        this.context = context;
        mainActivity = activity;
    }

    public void take() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            File pictureFile = null;
            try {
                pictureFile = createPicture();
            } catch (IOException e) {
                Log.e(TAG, "take: Can't create a picture: ", e);
            }
            if (pictureFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(pictureFile));
                mainActivity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PICTURE);
            }
        }
    }

    private File createPicture() throws IOException {
        String imageFileName = new SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault())
                .format(new Date());
        File storageDirectory = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File picture = new File(storageDirectory, imageFileName + ".jpg");
        currentPicturePath = picture.getAbsolutePath();
        return picture;
    }

    public String getCurrentPicturePath() {
        return currentPicturePath;
    }

    public void galleryAddPicture() {
        if (currentPicturePath == null) return;
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(PREFIX_FILE + currentPicturePath);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }
}
