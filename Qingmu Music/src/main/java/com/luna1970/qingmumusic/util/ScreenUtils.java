package com.luna1970.qingmumusic.util;

import android.content.res.Resources;

import com.luna1970.qingmumusic.application.MusicApplication;

/**
 * Created by Yue on 2/1/2017.
 *
 */

public class ScreenUtils {
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static float getScreenXdpi() {
        return Resources.getSystem().getDisplayMetrics().xdpi;
    }

    public static float getScreenYdpi() {
        return Resources.getSystem().getDisplayMetrics().ydpi;
    }

    public static int getStatusBarSize() {
        int resourceId = MusicApplication.getInstance().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId != 0) {
            return MusicApplication.getInstance().getResources().getDimensionPixelSize(resourceId);
        }
        return -1;
    }

    public static float dp2px(int dp) {
        float dpi = MusicApplication.getInstance().getResources().getDisplayMetrics().densityDpi;
        return dpi * dp / 160;
    }

}
