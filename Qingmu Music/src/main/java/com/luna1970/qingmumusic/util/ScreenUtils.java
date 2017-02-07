package com.luna1970.qingmumusic.util;

import android.content.res.Resources;

import com.luna1970.qingmumusic.application.MusicApplication;

/**
 * 屏幕尺寸工具类
 *
 */

public class ScreenUtils {
    /**
     * 获得屏幕的宽度
     * @return 宽度px
     */
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * 获得屏幕的高度
     * @return 高度px
     */
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /**
     * 获得x方向dpi
     * @return xdpi
     */
    public static float getScreenXdpi() {
        return Resources.getSystem().getDisplayMetrics().xdpi;
    }

    /**
     * 获得y方向dpi
     * @return ydpi
     */
    public static float getScreenYdpi() {
        return Resources.getSystem().getDisplayMetrics().ydpi;
    }

    /**
     * 获得状态栏高度
     * @return px
     */
    public static int getStatusBarSize() {
        int resourceId = MusicApplication.getInstance().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId != 0) {
            return MusicApplication.getInstance().getResources().getDimensionPixelSize(resourceId);
        }
        return -1;
    }

    /**
     * dp转px
     * @param dp dp
     * @return px
     */
    public static float dp2px(int dp) {
        float dpi = MusicApplication.getInstance().getResources().getDisplayMetrics().densityDpi;
        return dpi * dp / 160;
    }

}
