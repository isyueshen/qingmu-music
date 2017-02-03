package com.luna1970.qingmumusic.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.application.PlayMode;
import com.luna1970.qingmumusic.service.MusicPlayService;
import com.luna1970.qingmumusic.util.PlayController;
import com.luna1970.qingmumusic.util.ScreenUtils;
import com.luna1970.qingmumusic.util.UriUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.luna1970.qingmumusic.application.MusicApplication.playState;

/**
 * Created by Yue on 2/1/2017.
 *
 */

public class MusicPlayActivity extends BaseActivity {
    private static final String TAG = "MusicPlayActivity";
    private ImageView backgroundIv;
    private ImageView songCoverIv;
    private TextView songTitleTv;
    private TextView songAuthorTv;
    private ActionBar actionBar;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver broadcastReceiver;
    private SeekBar seekBar;
    private ServiceConnection serviceConnection;
    private MusicPlayService.PlayControlBinder playControlBinder;
    private ImageView playOrPauseIv;
    private ImageView nextIv;
    private ImageView prevIv;
    private ImageView playModeIv;
    private TextView totalTimeTv;
    private TextView currentTimeTv;

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

        Intent intent = new Intent(this, MusicPlayService.class);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                playControlBinder = (MusicPlayService.PlayControlBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i(TAG, "onServiceDisconnected: ");
                playControlBinder = null;
            }
        };
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        setToolbar();
        initView();
        setViewInfo();
        setListeners();
    }

    /**
     * 设置toolbar
     */
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
        playModeIv = (ImageView) findViewById(R.id.play_mode);
        prevIv = (ImageView) findViewById(R.id.prev_iv);
        nextIv = (ImageView) findViewById(R.id.next_iv);
        playOrPauseIv = (ImageView) findViewById(R.id.play_or_pause_iv);
        currentTimeTv = (TextView) findViewById(R.id.current_time);
        totalTimeTv = (TextView) findViewById(R.id.total_time_tv);


        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        // 根据屏幕动态设置ImageView的位置, Y -> status bar + 2 * action bar, X -> Screen width / 2 - image radius
        Message.obtain(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int oldHeight = songCoverIv.getHeight();
                        int oldWidth = songCoverIv.getWidth();
                        songCoverIv.scrollTo(0, oldHeight / 2 - actionBar.getHeight() - oldWidth / 2);
                    }
                });
                return false;
            }
        })).sendToTarget();
    }


    private void setListeners() {
        prevIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playControlBinder.playPrev();
            }
        });
        playOrPauseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playControlBinder.playOfPause();
                playOrPauseIv.setSelected(true);
            }
        });
        nextIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playControlBinder.playNext();
            }
        });
        playModeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentMode = playState.getCurrentPlayMode();
                switch (currentMode) {
                    case PlayMode.REPEAT_ALL:
                        currentMode = PlayMode.REPEAT_ONE;
                        break;
                    case PlayMode.REPEAT_ONE:
                        currentMode = PlayMode.MODE_SHUFFLE;
                        break;
                    case PlayMode.MODE_SHUFFLE:
                        currentMode = PlayMode.REPEAT_ALL;
                        break;
                }
                playState.setCurrentPlayMode(currentMode);
                playControlBinder.changePlayMode(currentMode);
                playModeIv.setImageLevel(currentMode);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                Log.i(TAG, "onProgressChanged: ");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i(TAG, "onStartTrackingTouch: ");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playControlBinder.seekToPosition(seekBar.getProgress());
            }
        });

    }

    private void setViewInfo() {
        Song song = playState.getSong();
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
        seekBar.setMax(song.duration * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.CHINA);
        Date date = new Date();
        date.setTime(song.duration * 1000);
        String total = simpleDateFormat.format(date);
        totalTimeTv.setText(total);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setBroadCastReceiver();
    }

    private void setBroadCastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PlayController.STATE_SERVICE_PLAYING);
        intentFilter.addAction(PlayController.STATE_SERVICE_UPDATE_SEEK_BAR_PROGRESS);
        intentFilter.addAction(PlayController.STATE_SERVICE_UPDATE_BUFFER_PROGRESS);
        intentFilter.addAction(PlayController.STATE_SERVICE_PAUSE);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                Log.d(TAG, "onReceive() called with: context = [" + context + "], intent = [" + intent + "]");

                String action = intent.getAction();
                switch (action) {
                    case PlayController.STATE_SERVICE_PLAYING:
                        playOrPauseIv.setSelected(true);
                        setViewInfo();
                        break;
                    case PlayController.STATE_SERVICE_UPDATE_SEEK_BAR_PROGRESS:
                        int progress = intent.getIntExtra(PlayController.STATE_SERVICE_UPDATE_SEEK_BAR_PROGRESS, 0);
                        seekBar.setProgress(progress);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.CHINA);
                        Date date = new Date();
                        date.setTime(progress);
                        String current = simpleDateFormat.format(date);
                        currentTimeTv.setText(current);
                        break;
                    case PlayController.STATE_SERVICE_UPDATE_BUFFER_PROGRESS:
                        int bufferProgress = intent.getIntExtra(PlayController.STATE_SERVICE_UPDATE_BUFFER_PROGRESS, 0);
                        seekBar.setSecondaryProgress(bufferProgress/100 * playState.getDuration() * 1000);
                        break;
                    case PlayController.STATE_SERVICE_PAUSE:
                        playOrPauseIv.setSelected(false);
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
        overridePendingTransition(0, R.anim.exit_activity_translate_out_top2bottom);
    }

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
        unbindService(serviceConnection);
        super.onDestroy();
    }
}
