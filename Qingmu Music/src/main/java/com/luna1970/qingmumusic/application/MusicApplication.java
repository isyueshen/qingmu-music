package com.luna1970.qingmumusic.application;

import android.app.Activity;
import android.app.Application;
import android.app.usage.NetworkStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import com.bumptech.glide.Glide;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.dao.UserInfo;
import com.luna1970.qingmumusic.service.MusicPlayService;
import com.luna1970.qingmumusic.util.GlideCacheUtil;
import com.luna1970.qingmumusic.util.ToastUtils;
import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by Yue on 1/9/2017.
 *
 */

public class MusicApplication extends Application {

    public static PlayState playState;
    public static MusicApplication musicApplication;
    private BroadcastReceiver broadcastReceiver;
    public static int currentNetType;
    private Activity activity;

    @Override
    public void onCreate() {
        super.onCreate();
        playState = PlayState.getInstance();
//        MusicDao musicDao = new MusicCursorDaoImpl(this);
        musicApplication = this;
        // Start service
        Intent intent = new Intent();
        intent.setClass(this, MusicPlayService.class);
        startService(intent);
        FlowManager.init(getApplicationContext());
        initUserInfo();
        setBroadcastReceiver();
    }

    private void initUserInfo() {
        playState.setCurrentPosition(UserInfo.getCurrentPosition());
    }

    private void setBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    // 检查网络
                    case "android.net.conn.CONNECTIVITY_CHANGE":
                        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                        if (networkInfo != null) {
                            Logger.d(networkInfo.getType() + networkInfo.getTypeName() + networkInfo.getExtraInfo() + networkInfo.getTypeName() + networkInfo.getReason()  + networkInfo.getState());
                            switch (networkInfo.getType()) {
                                // mobile
                                case ConnectivityManager.TYPE_MOBILE:
                                    currentNetType = ConnectivityManager.TYPE_MOBILE;
                                    ToastUtils.makeText("当前为移动网络, 请注意您的流量!").show();
                                    break;
                                // wifi
                                case ConnectivityManager.TYPE_WIFI:
                                    currentNetType = ConnectivityManager.TYPE_WIFI;
                                    break;
                            }
                        } else {
                            // no connection
                            currentNetType = -1;
                        }
                        break;
                    case "s":
                        break;
                    case "":
                        break;
                }
                Logger.d(intent.getAction());
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    public static MusicApplication getInstance() {
        return musicApplication;
    }

    @Override
    public void onLowMemory() {
        GlideCacheUtil.getInstance().clearImageMemoryCache();
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        unregisterReceiver(broadcastReceiver);
        super.onTerminate();
    }
}
