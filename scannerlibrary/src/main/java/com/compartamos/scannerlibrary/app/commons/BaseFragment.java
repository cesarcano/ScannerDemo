package com.compartamos.scannerlibrary.app.commons;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.compartamos.scannerlibrary.app.dialogs.LoadingDialog;

/**
 * Created by cecano@compartamos.com
 * on 21/11/2019.
 */
public class BaseFragment extends Fragment implements IActionsUI {

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
        loadingDialog = null;
    }

    @Override
    public void showToastMsg(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSnackMsg(String s) {

    }

    @Nullable
    @Override
    public Context getContext() {
        return super.getContext();
    }
}
