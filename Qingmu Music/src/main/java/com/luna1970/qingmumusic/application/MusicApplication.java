package com.luna1970.qingmumusic.application;

import android.app.Application;
import android.content.Intent;

import com.luna1970.qingmumusic.service.MusicPlayService;

/**
 * Created by Yue on 1/9/2017.
 *
 */

public class MusicApplication extends Application {

    public static PlayState playState;
    public static MusicApplication musicApplication;

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
    }

    public static MusicApplication getInstance() {
        return musicApplication;
    }
}
