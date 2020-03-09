package com.compartamos.scannerlibrary.app;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ImageViewCompat;

import com.compartamos.scannerlibrary.LibEnvironment;
import com.compartamos.scannerlibrary.R;
import com.compartamos.scannerlibrary.app.commons.BaseActivity;
import com.compartamos.scannerlibrary.app.dialogs.AlertDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ScannerActivity extends BaseActivity {

    private final String TAG = getClass().getSimpleName();

    public static String LIB_COLOR = "lib_color";

    private AlertDialog alertDialog;
    private BottomNavigationView bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sl_activity_scanner);

        bottom_navigation = findViewById(R.id.bottom_navigation);

        initLibEnvironment(getIntent().getExtras());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initToolbar(toolbar, "Scanner");

        bottom_navigation.setItemIconTintList(
                ColorStateList.valueOf(Color.parseColor(LibEnvironment.getInstance().getColorTheme().getColorPrimary())));
        bottom_navigation.setItemTextColor(
                ColorStateList.valueOf(Color.parseColor(LibEnvironment.getInstance().getColorTheme().getColorPrimary())));
        bottom_navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == R.id.op_camera) {
                        showToastMsg("Camera");
                        return true;
                    } else if (itemId == R.id.op_files) {
                        showToastMsg("Archivos");
                        return true;
                    }
                    return false;
                }
            };


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


}
