package com.scanlibrary;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.scanlibrary.commons.BaseFragment;
import com.scanlibrary.commons.LibUtils;

import java.io.IOException;

/**
 * Created by cecano@compartamos.com
 * on 10/03/2020.
 */
public class PreviewImgFragment extends BaseFragment {

    private View view;
    private ImageView scannedImageView;
    private Bitmap original;
    private Bitmap transformed;
    private BottomNavigationView edit_navigation;

    public PreviewImgFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sl_fragment_preview_img, container,false);
        scannedImageView = view.findViewById(R.id.iv_scann_image);
        edit_navigation = view.findViewById(R.id.edit_navigation);
        edit_navigation.setItemIconTintList(
                ColorStateList.valueOf(Color.parseColor(LibEnvironment.getInstance().getColorTheme().getColorAccent())));
        edit_navigation.setItemTextColor(
                ColorStateList.valueOf(Color.parseColor(LibEnvironment.getInstance().getColorTheme().getColorAccent())));
        edit_navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        init();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void init() {
        Bitmap bitmap = getBitmap();
        setScannedImage(bitmap);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.sl_menu_preview, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.op_aceptar) {
            processImg();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void processImg() {
        showLoading(R.string.sl_loading, getChildFragmentManager());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent data = new Intent();
                    Bitmap bitmap = transformed;
                    if (bitmap == null) {
                        bitmap = original;
                    }
                    Uri uri = LibUtils.getUri(getContext(), bitmap);
                    data.putExtra(ScanActivity.ScannConstants.SCANNED_RESULT, uri);
                    getActivity().setResult(Activity.RESULT_OK, data);
                    original.recycle();
                    System.gc();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideLoading();
                            getActivity().finish();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == R.id.op_original) {
                        setOriginalSettings();
                        return true;
                    }
                    if (itemId == R.id.op_auto_fix) {
                        setAutoFixColors();
                        return true;
                    }
                    if (itemId == R.id.op_gray_scale) {
                        setGrayScaleColors();
                        return true;
                    }
                    if (itemId == R.id.op_black_white) {
                        setBWcolors();
                        return true;
                    }
                    return false;
                }
            };

    private void setBWcolors() {
        showLoading(R.string.sl_apply_filter, getChildFragmentManager());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    transformed = ((ScanActivity) getActivity()).getBWBitmap(original);
                } catch (final OutOfMemoryError e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            transformed = original;
                            scannedImageView.setImageBitmap(original);
                            e.printStackTrace();
                            hideLoading();
                        }
                    });
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scannedImageView.setImageBitmap(transformed);
                        hideLoading();
                    }
                });
            }
        });
    }

    private void setGrayScaleColors() {
        showLoading(R.string.sl_apply_filter, getChildFragmentManager());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    transformed = ((ScanActivity) getActivity()).getGrayBitmap(original);
                } catch (final OutOfMemoryError e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            transformed = original;
                            scannedImageView.setImageBitmap(original);
                            e.printStackTrace();
                            hideLoading();
                        }
                    });
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scannedImageView.setImageBitmap(transformed);
                        hideLoading();
                    }
                });
            }
        });
    }

    private void setAutoFixColors() {
        showLoading(R.string.sl_apply_filter, getChildFragmentManager());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    transformed = ((ScanActivity) getActivity()).getMagicColorBitmap(original);
                } catch (final OutOfMemoryError e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            transformed = original;
                            scannedImageView.setImageBitmap(original);
                            e.printStackTrace();
                            hideLoading();
                        }
                    });
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scannedImageView.setImageBitmap(transformed);
                        hideLoading();
                    }
                });
            }
        });
    }

    private void setOriginalSettings() {
        try {
            showLoading(R.string.sl_apply_filter, getChildFragmentManager());
            transformed = original;
            scannedImageView.setImageBitmap(original);
            hideLoading();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            hideLoading();
        }
    }

    private Bitmap getBitmap() {
        Uri uri = getUri();
        try {
            original = LibUtils.getBitmap(getContext(), uri);
            getActivity().getContentResolver().delete(uri, null, null);
            return original;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Uri getUri() {
        Uri uri = getArguments().getParcelable(ScanActivity.ScannConstants.SCANNED_RESULT);
        return uri;
    }

    public void setScannedImage(Bitmap scannedImage) {
        scannedImageView.setImageBitmap(scannedImage);
    }

}