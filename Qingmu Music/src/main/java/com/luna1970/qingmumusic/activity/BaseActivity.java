package com.luna1970.qingmumusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.luna1970.qingmumusic.application.MusicApplication;
import com.luna1970.qingmumusic.service.MusicPlayService;

/**
 * Created by Yue on 1/28/2017.
 *
 */

public class BaseActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start service
        if (!MusicApplication.isPlaying) {
            intent = new Intent();
            intent.setClass(this, MusicPlayService.class);
            startService(intent);
        }
    }

}
