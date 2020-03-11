package com.compartamos.scannerlibrary.app;

import android.content.ComponentCallbacks2;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ImageViewCompat;

import com.compartamos.scannerlibrary.LibEnvironment;
import com.compartamos.scannerlibrary.R;
import com.compartamos.scannerlibrary.app.commons.BaseActivity;
import com.compartamos.scannerlibrary.app.dialogs.AlertDialog;
import com.compartamos.scannerlibrary.commons.LibUtils;


/**
 * Created by cecano@compartamos.com
 * on 10/03/2020.
 */

public class ScannerActivity extends BaseActivity implements IScannCallback, ComponentCallbacks2 {

    private final String TAG = getClass().getSimpleName();

    public static String LIB_COLOR = "lib_color";

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sl_activity_scanner);


        initLibEnvironment(getIntent().getExtras());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initToolbar(toolbar, "Scanner");

        int defaultAction = LibUtils.ScannConstants.OPEN_CAMERA;

        ImgPickerFragment fragment = new ImgPickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(LibUtils.ScannConstants.OPEN_INTENT_PREFERENCE, defaultAction);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().
                add(R.id.fl_fragment_container, fragment, fragment.getClass().getName()).
                commit();
    }

    private void initToolbar(View view, String title) {
        TextView toolbar_title = view.findViewById(R.id.toolbar_title);
        ImageView toolbar_left_ico = view.findViewById(R.id.toolbar_left_ico);
        view.setBackgroundColor(Color.parseColor(
                LibEnvironment.getInstance().getColorTheme().getColorPrimary()));

        toolbar_left_ico.setVisibility(View.VISIBLE);
        toolbar_left_ico.setImageDrawable(getResources().getDrawable(R.drawable.sl_ic_back_arrow));
        ImageViewCompat.setImageTintList(toolbar_left_ico, ColorStateList.valueOf(Color.parseColor(LibEnvironment.getInstance().getColorTheme().getBasicTextColor())));

        toolbar_title.setVisibility(View.VISIBLE);
        toolbar_title.setText(title);
        toolbar_title.setTextColor(
                Color.parseColor(LibEnvironment.getInstance().getColorTheme().getBasicTextColor()));

        toolbar_left_ico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog == null)
                    alertDialog = AlertDialog.getInstance("Alerta", "¿Está seguro de que desea salir?");
                alertDialog.setDialoagCallback(new AlertDialog.ICallback() {
                    @Override
                    public void onAccept() {
                        finish();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                alertDialog.showNow(getSupportFragmentManager(), AlertDialog.class.getSimpleName());
            }
        });
    }

    private void initLibEnvironment(@Nullable Bundle environment) {
        LibEnvironment libEnvironment = new LibEnvironment();
        LibEnvironment.ColorTheme mColorTheme = new LibEnvironment.ColorTheme();
        String color = "#FFFFFF";
        if (environment != null) {
            color = environment.getString(LIB_COLOR, "#FFFFFF").length() == 7 ?
                    environment.getString(LIB_COLOR) : color;
        }
        mColorTheme.setColorPrimary(color);
        mColorTheme.setColorAccent(color);

        libEnvironment.setColorTheme(mColorTheme);
        LibEnvironment.initialize(libEnvironment);
    }

    @Override
    public void onBitmapSelect(Uri uri) {
        ScannFragment fragment = new ScannFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LibUtils.ScannConstants.SELECTED_BITMAP, uri);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().
                add(R.id.fl_fragment_container, fragment, fragment.getClass().getName()).
                commit();
    }

    @Override
    public void onScanFinish(Uri uri) {
        PreviewImgFragment fragment = new PreviewImgFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LibUtils.ScannConstants.SCANNED_RESULT, uri);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().
                add(R.id.fl_fragment_container, fragment, fragment.getClass().getName()).
                commit();
    }


    @Override
    public void onTrimMemory(int level) {
        switch (level) {
            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:
                /*
                   Release any UI objects that currently hold memory.

                   The user interface has moved to the background.
                */
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:
                /*
                   Release any memory that your app doesn't need to run.

                   The device is running low on memory while the app is running.
                   The event raised indicates the severity of the memory-related event.
                   If the event is TRIM_MEMORY_RUNNING_CRITICAL, then the system will
                   begin killing background processes.
                */
                break;
            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                /*
                   Release as much memory as the process can.

                   The app is on the LRU list and the system is running low on memory.
                   The event raised indicates where the app sits within the LRU list.
                   If the event is TRIM_MEMORY_COMPLETE, the process will be one of
                   the first to be terminated.
                */
                break;
            default:
                /*
                  Release any non-critical data structures.

                  The app received an unrecognized memory level value
                  from the system. Treat this as a generic low-memory message.
                */
                break;
        }
    }

    public native Bitmap getScannedBitmap(Bitmap bitmap, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4);

    public native Bitmap getGrayBitmap(Bitmap bitmap);

    public native Bitmap getMagicColorBitmap(Bitmap bitmap);

    public native Bitmap getBWBitmap(Bitmap bitmap);

    public native float[] getPoints(Bitmap bitmap);

    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("Scanner");
    }

}
