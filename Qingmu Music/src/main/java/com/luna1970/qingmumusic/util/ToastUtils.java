package com.luna1970.qingmumusic.util;

import android.widget.Toast;

import com.luna1970.qingmumusic.application.MusicApplication;

/**
 * Created by Yue on 1/10/2017.
 *
 */

public class ToastUtils {
    private static Toast toast;
    public static Toast makeText(CharSequence charSequence) {
        if (toast==null) {
            toast = Toast.makeText(MusicApplication.getInstance().getApplicationContext(), charSequence, Toast.LENGTH_SHORT);
        } else {
            toast.setText(charSequence);
        }
        return toast;
    }
    public static Toast makeText(int resId) {
        if (toast==null) {
            toast = Toast.makeText(MusicApplication.getInstance().getApplicationContext(), resId, Toast.LENGTH_SHORT);
        } else {
            toast.setText(resId);
        }
        return toast;
    }
}
