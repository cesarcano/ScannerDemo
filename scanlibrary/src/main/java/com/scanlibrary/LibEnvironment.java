package com.scanlibrary;

import com.scanlibrary.commons.LibUtils;

/**
 * Created by cecano@compartamos.com
 * on 09/03/2020.
 */
public class LibEnvironment {
    private ColorTheme colorTheme;
    private String hostId;
    private static LibEnvironment _instance;

    public static void initialize(LibEnvironment libEnvironment) {
        _instance = libEnvironment;
    }

    public static LibEnvironment getInstance() {
        if (_instance == null)
            throw new IllegalStateException(
                    String.valueOf(R.string.sl_error_environment_inizialized));
        return _instance;
    }

    public ColorTheme getColorTheme() {
        return colorTheme;
    }

    public void setColorTheme(ColorTheme colorTheme) {
        this.colorTheme = colorTheme;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public static class ColorTheme {
        private String colorPrimary;
        private String colorAccent;
        private String basicTextColor;


        public String getColorPrimary() {
            return colorPrimary;
        }

        public void setColorPrimary(String colorPrimary) {
            this.colorPrimary = colorPrimary;
        }

        public String getColorAccent() {
            return colorAccent;
        }

        public void setColorAccent(String colorAccent) {
            this.colorAccent = colorAccent;
            if (colorAccent.equals("#FFFFFF"))
                this.colorAccent = "#212121";
        }

        public String getBasicTextColor() {
            basicTextColor = "#212121";
            if (LibUtils.isDarkColor(colorPrimary))
                basicTextColor = "#ffffff";
            return basicTextColor;
        }
    }
}
