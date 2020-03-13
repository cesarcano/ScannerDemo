package com.scanlibrary;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.scanlibrary.commons.BaseActivity;
import com.scanlibrary.commons.LibUtils;
import com.scanlibrary.dialogs.AlertDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by cecano@compartamos.com
 * on 10/03/2020.
 */

public class ScanActivity extends BaseActivity implements IScannCallback, ComponentCallbacks2 {

    private final String TAG = getClass().getSimpleName();

    public static String LIB_COLOR = "lib_color";

    private AlertDialog alertDialog;

    private Fragment currentFragment;

    private Uri fileUri;

    private BottomNavigationView bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sl_activity_scanner);
        initLibEnvironment(getIntent().getExtras());

        bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setItemIconTintList(
                ColorStateList.valueOf(Color.parseColor(LibEnvironment.getInstance().getColorTheme().getColorAccent())));
        bottom_navigation.setItemTextColor(
                ColorStateList.valueOf(Color.parseColor(LibEnvironment.getInstance().getColorTheme().getColorAccent())));
        bottom_navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initToolbar(toolbar, "Scanner");
        getPermission();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == R.id.op_camera) {
                        openCamera();
                        return true;
                    } else if (itemId == R.id.op_files) {
                        openMediaContent();
                        return true;
                    }
                    return false;
                }
            };

    private void initToolbar(View view, String title) {
        TextView toolbar_title = view.findViewById(R.id.toolbar_title);
        ImageView toolbar_left_ico = view.findViewById(R.id.toolbar_left_ico);
        view.setBackgroundColor(Color.WHITE);

        toolbar_left_ico.setVisibility(View.VISIBLE);
        toolbar_left_ico.setImageDrawable(getResources().getDrawable(R.drawable.sl_ic_back_arrow));
        ImageViewCompat.setImageTintList(toolbar_left_ico, ColorStateList.valueOf(Color.parseColor(LibEnvironment.getInstance().getColorTheme().getColorAccent())));

        toolbar_title.setVisibility(View.VISIBLE);
        toolbar_title.setText(title);
        toolbar_title.setTextColor(
                Color.parseColor(LibEnvironment.getInstance().getColorTheme().getColorAccent()));

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
            if (environment.containsKey(LIB_COLOR))
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
        if (currentFragment != null)
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
        currentFragment = new ScannFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LibUtils.ScannConstants.SELECTED_BITMAP, uri);
        currentFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().
                add(R.id.fl_fragment_container, currentFragment, currentFragment.getClass().getName()).
                commit();
    }

    @Override
    public void onScanFinish(Uri uri) {
        if (currentFragment != null)
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
        currentFragment = new PreviewImgFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LibUtils.ScannConstants.SCANNED_RESULT, uri);
        currentFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().
                add(R.id.fl_fragment_container, currentFragment, currentFragment.getClass().getName()).
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

    private void clearTempImages() {
        try {
            File tempFolder = new File(LibUtils.ScannConstants.IMAGE_PATH);
            for (File f : tempFolder.listFiles())
                f.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openMediaContent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, LibUtils.ScannConstants.PICKFILE_REQUEST_CODE);
    }

    public void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = createImageFile();
        boolean isDirectoryCreated = file.getParentFile().mkdirs();
        Log.d("", "openCamera: isDirectoryCreated: " + isDirectoryCreated);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri tempFileUri = FileProvider.getUriForFile(this,
                    "com.scanlibrary.fileProviderScanner", // As defined in Manifest
                    file);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempFileUri);
        } else {
            Uri tempFileUri = Uri.fromFile(file);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempFileUri);
        }
        startActivityForResult(cameraIntent, LibUtils.ScannConstants.START_CAMERA_REQUEST_CODE);
    }

    private File createImageFile() {
        clearTempImages();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new
                Date());
        File file = new File(LibUtils.ScannConstants.IMAGE_PATH, "IMG_" + timeStamp +
                ".jpg");
        fileUri = Uri.fromFile(file);
        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("", "onActivityResult" + resultCode);
        Bitmap bitmap = null;
        if (resultCode == Activity.RESULT_OK) {
            try {
                switch (requestCode) {
                    case LibUtils.ScannConstants.START_CAMERA_REQUEST_CODE:
                        bitmap = getBitmap(fileUri);
                        break;

                    case LibUtils.ScannConstants.PICKFILE_REQUEST_CODE:
                        bitmap = getBitmap(data.getData());
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            finish();
        }
        if (bitmap != null) {
            postImagePick(bitmap);
        }
    }

    protected void postImagePick(Bitmap bitmap) {
        Uri uri = LibUtils.getUri(this, bitmap);
        bitmap.recycle();
        onBitmapSelect(uri);
    }

    private Bitmap getBitmap(Uri selectedimg) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        AssetFileDescriptor fileDescriptor = null;
        fileDescriptor =
                this.getContentResolver().openAssetFileDescriptor(selectedimg, "r");
        Bitmap original
                = BitmapFactory.decodeFileDescriptor(
                fileDescriptor.getFileDescriptor(), null, options);
        return original;
    }

    @Override
    public void onBackPressed() {
        // nothing...
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 125: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPermission();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    getPermission();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void getPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    125);
        } else {
            openCamera();
        }
    }
}
