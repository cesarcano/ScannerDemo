package com.scanlibrary.commons;

import androidx.fragment.app.FragmentManager;

/**
 * Created by cecano@compartamos.com
 * on 21/11/2019.
 */
public interface IActionsUI {
    void showLoading(int message, FragmentManager fragmentManager);
    void hideLoading();
    void showToastMsg(String s);
    void showSnackMsg(String s);
}
