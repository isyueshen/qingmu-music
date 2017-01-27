package com.luna1970.qingmumusic.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Yue on 1/10/2017.
 *
 */

public class ToastUtils {
    private static Toast toast;
    public static Toast makeText(Context context, CharSequence charSequence, int duration) {
        if (toast==null) {
            toast = Toast.makeText(context, charSequence, duration);
        } else {
            toast.setText(charSequence);
        }
        return toast;
    }
    public static Toast makeText(Context context, int resId, int duration) {
        if (toast==null) {
            toast = Toast.makeText(context, resId, duration);
        } else {
            toast.setText(resId);
        }
        return toast;
    }
}
