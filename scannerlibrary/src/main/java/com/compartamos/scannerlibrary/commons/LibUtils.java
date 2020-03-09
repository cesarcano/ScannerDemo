package com.compartamos.scannerlibrary.commons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * <p>Lib Utils</p>
 * Used to global functions of the app.
 */
public class LibUtils {

    public static boolean isDarkColor(String colorAsString) {
        int color = Color.parseColor(colorAsString);
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return !(darkness < 0.4); // It's a light color
    }

    public static Uri getUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,
                Long.toString(Calendar.getInstance().getTimeInMillis()), null);
        return Uri.parse(path);
    }

    public static Bitmap getBitmap(Context context, Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        return bitmap;
    }
}
