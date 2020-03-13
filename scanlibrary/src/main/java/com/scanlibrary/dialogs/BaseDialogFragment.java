package com.scanlibrary.dialogs;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.scanlibrary.R;

/**
 * Created by cecano@compartamos.com
 * on 21/11/2019.
 */
public class BaseDialogFragment extends DialogFragment {

    public static final String LOCAL_ICON = "local_icon";
    public static final String BACKGROUND_ID = "background_id";
    public static final String REMOTE_ICON = "remote_icon";
    public static final String LAYOUT_ID = "layout_id";

    @DrawableRes
    private int mIcon;
    private String mRemoteIcon;
    @LayoutRes
    private int mLayoutRes;
    @DrawableRes
    private int mBackgroundDrawable;

    public BaseDialogFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mIcon = bundle.getInt(BaseDialogFragment.LOCAL_ICON);
            mRemoteIcon = bundle.getString(BaseDialogFragment.REMOTE_ICON);
            mLayoutRes = bundle.getInt(BaseDialogFragment.LAYOUT_ID);
            mBackgroundDrawable = bundle.getInt(BaseDialogFragment.BACKGROUND_ID);
        }

        if (mLayoutRes == 0) {
            throw new IllegalArgumentException("Required layout id to generateInstance this dialog fragment.");
        }
    }

    @Override
    public int getTheme() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ?
                R.style.Platform_MaterialComponents :
                R.style.Theme_AppCompat;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(mLayoutRes, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.sl_transparent20));
        return dialog;
    }
}
