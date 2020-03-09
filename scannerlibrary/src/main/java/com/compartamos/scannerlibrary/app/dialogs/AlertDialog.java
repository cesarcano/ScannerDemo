package com.compartamos.scannerlibrary.app.dialogs;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.compartamos.scannerlibrary.LibEnvironment;
import com.compartamos.scannerlibrary.R;
import com.google.android.material.button.MaterialButton;

/**
 * Created by cecano@compartamos.com
 * on 06/02/2020.
 */
public class AlertDialog extends BaseDialogFragment {

    public interface ICallback {
        void onAccept();

        void onCancel();
    }

    private static final String TITLE = "BASIC_TITLE";
    private static final String BODY_MESSAGE = "BODY_MESSAGE";
    public ICallback callback;

    // UI
    private TextView tv_dialog_title;
    private TextView tv_dialog_body;
    private MaterialButton b_dialog_cancel_btn;
    private MaterialButton b_dialog_accept_btn;
    // Content
    private String title;
    private String bodyMessage;

    public static AlertDialog getInstance(@NonNull String title, @NonNull String bodyMessage) {
        AlertDialog alertDialog = new AlertDialog();
        Bundle bundleArgs = new Bundle();
        bundleArgs.putString(TITLE, title);
        bundleArgs.putString(BODY_MESSAGE, bodyMessage);
        bundleArgs.putInt(LAYOUT_ID, R.layout.sl_dialog_alert);
        alertDialog.setArguments(bundleArgs);
        return alertDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = new Bundle();
        bundle = getArguments();
        if (bundle != null) {
            this.title = bundle.getString(TITLE, "");
            this.bodyMessage = bundle.getString(BODY_MESSAGE, "");
        }
    }

    @Override
    public int getTheme() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ?
                R.style.Theme_MaterialComponents_Light_Dialog_Alert :
                R.style.Theme_AppCompat_Light_Dialog_Alert;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_dialog_title = view.findViewById(R.id.tv_dialog_title);
        tv_dialog_body = view.findViewById(R.id.tv_dialog_body);
        b_dialog_cancel_btn = view.findViewById(R.id.b_dialog_cancel_btn);
        b_dialog_accept_btn = view.findViewById(R.id.b_dialog_accept_btn);

        tv_dialog_title.setText(title);
        tv_dialog_body.setText(bodyMessage);

        b_dialog_accept_btn.setBackgroundColor(
                Color.parseColor(
                        LibEnvironment.getInstance().getColorTheme().getColorAccent()
                )
        );

        b_dialog_cancel_btn.setStrokeColor(
                ColorStateList.valueOf(
                        Color.parseColor(
                                LibEnvironment.getInstance().getColorTheme().getColorAccent()
                        )
                )
        );
        b_dialog_cancel_btn.setStrokeWidth(5);
        b_dialog_cancel_btn.setTextColor(
                Color.parseColor(
                        LibEnvironment.getInstance().getColorTheme().getColorAccent()
                )
        );
        b_dialog_cancel_btn.setBackgroundColor(
                Color.WHITE
        );

        b_dialog_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                callback.onCancel();

            }
        });
        b_dialog_accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onAccept();
            }
        });

    }

    public void setDialoagCallback(ICallback callback) {
        this.callback = callback;
    }
}
