package com.compartamos.scannerlibrary.app.dialogs;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.compartamos.scannerlibrary.LibEnvironment;
import com.compartamos.scannerlibrary.R;


/**
 * Created by cecano@compartamos.com
 * on 21/11/2019.
 */
public class LoadingDialog extends BaseDialogFragment {
    public static String LOADING_MESSAGE = "loading_message";

    private TextView tv_message;
    private ProgressBar pb_progress;
    @StringRes
    private int mResourceMessage;

    public LoadingDialog() {
    }

    public static LoadingDialog getInstance(@StringRes int resourceMesssage) {
        Bundle bundle = new Bundle();
        bundle.putInt(LOADING_MESSAGE, resourceMesssage);
        bundle.putInt(LAYOUT_ID, R.layout.sl_dialog_loading);
        bundle.putInt(BACKGROUND_ID, new ColorDrawable(Color.WHITE).getColor());
        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.setArguments(bundle);
        return loadingDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mResourceMessage = bundle.getInt(LOADING_MESSAGE);
        }
        setCancelable(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_message = view.findViewById(R.id.tv_message);
        pb_progress = view.findViewById(R.id.pb_progress);

        pb_progress.getIndeterminateDrawable()
                .setColorFilter(Color.parseColor(LibEnvironment.getInstance().getColorTheme().getColorAccent()),
                        PorterDuff.Mode.SRC_IN);

        if (mResourceMessage != 0) {
            tv_message.setText(mResourceMessage);
        }
    }
}
