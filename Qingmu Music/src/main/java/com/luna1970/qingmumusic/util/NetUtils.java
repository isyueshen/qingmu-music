package com.luna1970.qingmumusic.util;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.luna1970.qingmumusic.application.MusicApplication;

/**
 * 网络检查工具类
 *
 */

public class NetUtils {

    public static void openWifi() {
        WifiManager wifiManager = (WifiManager) MusicApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager!=null && !wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
    }
}
