package com.compartamos.scannerlibrary.app.commons;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.compartamos.scannerlibrary.app.dialogs.LoadingDialog;

/**
 * Created by cecano@compartamos.com
 * on 21/11/2019.
 */
public class BaseActivity extends AppCompatActivity implements IActionsUI {

    public final static String SERIALIZED_DATA_FLAG = "DATA";
    private LoadingDialog loadingDialog;

    @Override
    public void showLoading(int message, FragmentManager fragmentManager) {
        if (loadingDialog == null)
            loadingDialog = LoadingDialog.getInstance(message);
        loadingDialog.showNow(fragmentManager, LoadingDialog.class.getName());
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null)
            loadingDialog.dismiss();
    }

    /**
     * Show a toast message.
     *
     * @param s the message to show.
     */
    @Override
    public void showToastMsg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSnackMsg(String s) {

    }


    /**
     * Launch an activity
     *
     * @param bundle   bundle data to send
     * @param activity the activity class.
     */
    public void launchActivity(Bundle bundle, Class activity) {
        Intent intent = new Intent(this, activity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void initToolbar(View view) {

    }
}
