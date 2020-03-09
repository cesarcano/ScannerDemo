package com.compartamos.scannerdemo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.compartamos.scannerlibrary.app.ScannerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, ScannerActivity.class);
        intent.putExtra(ScannerActivity.LIB_COLOR, "#652D89");
        startActivity(intent);
    }
}
