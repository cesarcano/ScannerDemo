package com.scanlibrary.control;

/**
 * Created by cecano@compartamos.com
 * on 09/03/2020.
 */
public enum ActionType {
    OPEN_CAMERA(0),// DEFAULT
    PICK_IMG(1);

    public int CODE;

    ActionType(int CODE) {
        this.CODE = CODE;
    }
}
