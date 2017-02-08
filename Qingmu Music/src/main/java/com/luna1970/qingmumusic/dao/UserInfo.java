package com.luna1970.qingmumusic.dao;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.luna1970.qingmumusic.application.MusicApplication;
import com.luna1970.qingmumusic.util.GlobalConst;

/**
 * Created by Yue on 2/8/2017.
 *
 */

public class UserInfo {
    public static void saveCurrentPosition(int position) {
        SharedPreferences.Editor editor =  PreferenceManager.getDefaultSharedPreferences(MusicApplication.getInstance().getApplicationContext()).edit();
        editor.putInt(GlobalConst.CURRENT_POSITION, position);
        editor.apply();
    }
    public static int getCurrentPosition() {
        return PreferenceManager.getDefaultSharedPreferences(MusicApplication.getInstance()).getInt(GlobalConst.CURRENT_POSITION, -1);
    }
}
