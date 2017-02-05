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
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.application.PlayMode;
import com.luna1970.qingmumusic.entity.Lrc;
import com.luna1970.qingmumusic.listener.LrcViewSingleTapUpListener;
import com.luna1970.qingmumusic.service.MusicPlayService;
import com.luna1970.qingmumusic.util.HttpUtils;
import com.luna1970.qingmumusic.util.LrcParse;
import com.luna1970.qingmumusic.util.PlayController;
import com.luna1970.qingmumusic.util.ScreenUtils;
import com.luna1970.qingmumusic.util.UriUtils;
import com.luna1970.qingmumusic.widget.LrcView;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.luna1970.qingmumusic.application.MusicApplication.playState;

/**
 * 歌曲播放详情页, 包含歌曲播放控制, 播放模式控制, 歌单列表, Infinity转动封面, 可拖动的自滚动的自定义歌词控件
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
    private String prevSongCover;
    private LrcView lrcView;
    private GestureDetector gestureDetector;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        Glide.get(this).clearMemory();
//
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // make navigation bar color to transparent
            Window window = getWindow();
            window.setNavigationBarColor(Color.TRANSPARENT);
        }

        // 绑定播放服务
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
        // 设置标题栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        // 整体向下移动状态栏高度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.content_ll);
            linearLayout.setPadding(0, ScreenUtils.getStatusBarSize(), 0, 0);
        }
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
        lrcView = (LrcView) findViewById(R.id.lrc);
        // 默认隐藏
        lrcView.setVisibility(View.GONE);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);

        // 开始转动
        startSongCoverAnimation();
        // 根据屏幕动态设置ImageView的位置, Y -> status bar + 2 * action bar, X -> Screen width / 2 - image radius
//        Message.obtain(new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        int oldHeight = songCoverIv.getHeight();
//                        int oldWidth = songCoverIv.getWidth();
//                        songCoverIv.scrollTo(0, oldHeight / 2 - actionBar.getHeight() - oldWidth / 2);
//                    }
//                });
//                return false;
//            }
//        })).sendToTarget();
    }

    private void setListeners() {
        // 上一曲
        prevIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playControlBinder.playPrev();
            }
        });
        // 播放或暂停
        playOrPauseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playControlBinder.playOfPause();
                playOrPauseIv.setSelected(true);
            }
        });
        // 下一曲
        nextIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playControlBinder.playNext();
            }
        });
        // 播放模式
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
        // 进度条
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
                // 指定播放位置
                playControlBinder.seekToPosition(seekBar.getProgress());
            }
        });
    }

    @Override
    protected void onStart() {
        // 初始化LrcView单击监听器
        lrcView.setLrcViewSingleTapUpListener(new LrcViewSingleTapUpListener() {
            @Override
            public boolean onSingleTapUp() {
                lrcView.setVisibility(View.GONE);
                songCoverIv.setVisibility(View.VISIBLE);
                Song song = playState.getSong();
                // 设置歌曲封面
                String imageUri = UriUtils.getCustomImageSize(song.songCoverPath, 1000);
                Glide.with(MusicPlayActivity.this).load(imageUri)
                        .crossFade(300)
                        .bitmapTransform(new CropCircleTransformation(MusicPlayActivity.this))
                        .into(songCoverIv);
                startSongCoverAnimation();
                return true;
            }
        });
        // 主页面单击监听器, 交替显示
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                lrcView.setVisibility(View.VISIBLE);
                songCoverIv.setVisibility(View.INVISIBLE);
                // 降低CPU使用率 & 内存资源
                songCoverIv.clearAnimation();
                Glide.clear(songCoverIv);
                return true;
            }
        });
        // 重新启动动画
        if (songCoverIv.getVisibility() != View.INVISIBLE) {
            startSongCoverAnimation();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        // 释放资源
        gestureDetector = null;
        lrcView.clearAnimation();
        super.onStop();
    }

    private void setViewInfo() {
        // 清理图片内存
        Glide.get(MusicPlayActivity.this).clearMemory();
        Song song = playState.getSong();
        // 设置标题栏信息
        songTitleTv.setText(song.title);
        songAuthorTv.setText(song.author);
        Logger.d(song.lrcPath);
        // 设置高斯模糊背景 & 歌曲封面
        String imageUri = UriUtils.getCustomImageSize(song.songCoverPath, 1000);
        Glide.with(this).load(imageUri)
                .load(imageUri)
                .bitmapTransform(new BlurTransformation(this, 100))
                .into(backgroundIv);
        if (lrcView.getVisibility() == View.GONE) {
            Glide.with(this).load(imageUri)
                    .crossFade(300)
                    .animate(new ViewPropertyAnimation.Animator() {
                        @Override
                        public void animate(View view) {
                            RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
                            rotateAnimation.setRepeatCount(Animation.INFINITE);
                            rotateAnimation.setDuration(25000);
                            rotateAnimation.setInterpolator(new LinearInterpolator());
                            rotateAnimation.setRepeatMode(Animation.RESTART);
                            view.startAnimation(rotateAnimation);
                        }
                    })
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(songCoverIv);
        } else {
            songCoverIv.clearAnimation();
        }
        // 初始化总进度
        seekBar.setMax(song.duration * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.CHINA);
        Date date = new Date();
        date.setTime(song.duration * 1000);
        String total = simpleDateFormat.format(date);
        totalTimeTv.setText(total);
        prevSongCover = song.songCoverPath;
    }

    /**
     * 歌曲封面Infinity旋转动画
     */
    public void startSongCoverAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setDuration(25000);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatMode(Animation.RESTART);
        songCoverIv.startAnimation(rotateAnimation);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initBroadCastReceiver();
    }

    @Override
    protected void onResume() {
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    private void initBroadCastReceiver() {
        intentFilter = new IntentFilter();
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
                    // 即将播放
                    case PlayController.STATE_SERVICE_PLAYING:
                        playOrPauseIv.setSelected(true);
                        setViewInfo();
                        // 设置歌词
                        String uri = playState.getSong().lrcPath;
                        lrcView.setLrc(null);
                        if (TextUtils.isEmpty(uri)) {
                            lrcView.drawTint("暂无歌词");
                            break;
                        }
                        lrcView.drawTint("正在加载...");
                        HttpUtils.sendHttpRequest(playState.getSong().lrcPath, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                lrcView.drawTint("加载歌词失败");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Lrc lrc = LrcParse.parseLrc(response.body().string());
                                lrcView.setLrc(lrc);
                            }
                        });
                        break;
                    // 更新进度
                    case PlayController.STATE_SERVICE_UPDATE_SEEK_BAR_PROGRESS:
                        // 当前播放进度
                        int progress = intent.getIntExtra(PlayController.STATE_SERVICE_UPDATE_SEEK_BAR_PROGRESS, 0);
                        seekBar.setProgress(progress);
                        String current = new SimpleDateFormat("mm:ss").format(new Date(progress));
                        currentTimeTv.setText(current);
                        // 如果LrcView当前处于隐藏状态, 则取消更新歌词时间
                        if (lrcView.getVisibility() == View.VISIBLE) {
                            lrcView.refreshLrc(progress);
                        }
                        break;
                    case PlayController.STATE_SERVICE_UPDATE_BUFFER_PROGRESS:
                        // 歌曲文件缓冲状态
                        int bufferProgress = intent.getIntExtra(PlayController.STATE_SERVICE_UPDATE_BUFFER_PROGRESS, 0);
                        seekBar.setSecondaryProgress(bufferProgress * playState.getDuration() * 10);
                        break;
                    case PlayController.STATE_SERVICE_PAUSE:
                        // 更新按钮样式
                        playOrPauseIv.setSelected(false);
                        break;
                }
            }
        };
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
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
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent: ");
        if (gestureDetector != null) {
            gestureDetector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.exit_activity_translate_out_top2bottom);
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }
}
