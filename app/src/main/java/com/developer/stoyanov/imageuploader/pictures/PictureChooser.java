package com.developer.stoyanov.imageuploader.pictures;

import android.app.Application;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.developer.stoyanov.imageuploader.URIUtils;

public class PictureChooser {
    public static final int REQUEST_PICK_IMAGE = 3;

    private Application context;

    public PictureChooser(Application context) {
        this.context = context;
    }

    public String pictureFromMediaStore(Uri selectedImageUri) {
        return URIUtils.getPath(context, selectedImageUri);
    }

    @Deprecated
    private String getPath(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        cursor.close();
        return uri.getPath();
    }
}
