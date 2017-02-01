package com.luna1970.qingmumusic.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.application.MusicApplication;
import com.luna1970.qingmumusic.util.UriUtils;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by Yue on 2/1/2017.
 *
 */

public class MusicPlayActivity extends BaseActivity {
    private static final String TAG = "MusicPlayActivity";
    private Song song;
    private ImageView backgroundIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
//
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // make navigation bar color to transparent
            Window window = getWindow();
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setToolbar();
        initView();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        int systemBarHeight = 0;
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            systemBarHeight = this.getResources().getDimensionPixelSize(resourceId);
        }


        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        linearLayout.setPadding(0, systemBarHeight, 0, 0);

    }


    private void initView() {
        song = MusicApplication.playList.get(MusicApplication.currentPosition);
        backgroundIv = (ImageView) findViewById(R.id.background_iv);
        requestBackgroundImage();
    }

    private void requestBackgroundImage() {
        String imageUri = UriUtils.getCustomImageSize(song.songCoverPath, 1000);
        Glide.with(this).load(imageUri)
                .load(imageUri)
                .bitmapTransform(new BlurTransformation(this, 100))
                .into(backgroundIv);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.exit_activity_translate_in_left2right, R.anim.exit_activity_translate_out_left2right);
    }
}
