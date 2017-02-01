package com.luna1970.qingmumusic.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.application.MusicApplication;
import com.luna1970.qingmumusic.util.GlobalMusicPlayControllerConst;
import com.luna1970.qingmumusic.util.ScreenUtils;
import com.luna1970.qingmumusic.util.UriUtils;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Yue on 2/1/2017.
 *
 */

public class MusicPlayActivity extends BaseActivity {
    private static final String TAG = "MusicPlayActivity";
    private Song song;
    private ImageView backgroundIv;
    private ImageView songCoverIv;
    private TextView songTitleTv;
    private TextView songAuthorTv;
    private ActionBar actionBar;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver broadcastReceiver;

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
        if (MusicApplication.currentPosition != -1) {
            song = MusicApplication.playList.get(MusicApplication.currentPosition);
            setViewInfo();
        }
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.content_ll);
        linearLayout.setPadding(0, ScreenUtils.getStatusBarSize(), 0, 0);

    }


    private void initView() {
        songTitleTv = (TextView) findViewById(R.id.song_title_tv);
        songAuthorTv = (TextView) findViewById(R.id.song_author_tv);
        backgroundIv = (ImageView) findViewById(R.id.background_iv);
        songCoverIv = (ImageView) findViewById(R.id.song_cover_iv);

        // 根据屏幕动态设置ImageView的位置, Y -> status bar + 2 * action bar, X -> Screen width / 2 - image radius
        Message.obtain(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int oldHeight = songCoverIv.getHeight();
                        int oldWidth = songCoverIv.getWidth();
                        songCoverIv.scrollTo(0, oldHeight/2 - actionBar.getHeight() - oldWidth/2);
                    }
                });
                return false;
            }
        })).sendToTarget();
    }

    private void setViewInfo() {
        // 设置标题栏信息
        songTitleTv.setText(song.title);
        songAuthorTv.setText(song.author);

        // 设置高斯模糊背景 & 歌曲封面
        String imageUri = UriUtils.getCustomImageSize(song.songCoverPath, 1000);
        Glide.with(this).load(imageUri)
                .load(imageUri)
                .bitmapTransform(new BlurTransformation(this, 100))
                .into(backgroundIv);
        Glide.with(this).load(imageUri).bitmapTransform(new CropCircleTransformation(this)).into(songCoverIv);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setBroadCastReceiver();
    }

    private void setBroadCastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalMusicPlayControllerConst.ACTION_SERVICE_PLAYING);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive() called with: context = [" + context + "], intent = [" + intent + "]");

                String action = intent.getAction();
                switch (action) {
                    case GlobalMusicPlayControllerConst.ACTION_SERVICE_PLAYING:
                        song = MusicApplication.playList.get(MusicApplication.currentPosition);
                        setViewInfo();
                        break;
                }
            }
        };
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.exit_activity_translate_in_left2right, R.anim.exit_activity_translate_out_left2right);
    }

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}
