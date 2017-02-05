package com.luna1970.qingmumusic.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.luna1970.qingmumusic.Gson.SongInfo;
import com.luna1970.qingmumusic.application.PlayMode;
import com.luna1970.qingmumusic.util.GsonUtils;
import com.luna1970.qingmumusic.util.HttpUtils;
import com.luna1970.qingmumusic.util.PlayController;
import com.luna1970.qingmumusic.util.ToastUtils;
import com.luna1970.qingmumusic.util.UriUtils;

import java.io.IOException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.luna1970.qingmumusic.application.MusicApplication.playState;

/**
 * Created by Yue on 1/9/2017.
 *
 */

public class MusicPlayService extends Service {
    private static final String TAG = "MusicPlayService";
    private IntentFilter intentFilter;
    private BroadcastReceiver broadcastReceiver;
    private int currentPlayMode;
    private boolean controlProgressUpdateWorkThread;
    private LocalBroadcastManager localBroadcastManager;
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        controlProgressUpdateWorkThread = true;
        // 初始化intentFilter
        intentFilter = new IntentFilter();
        intentFilter.addAction(PlayController.ACTION_PLAY_OR_PAUSE);
        intentFilter.addAction(PlayController.ACTION_PLAY_NEXT);
        intentFilter.addAction(PlayController.ACTION_PLAY_PREV);
        intentFilter.addAction(PlayController.ACTION_PLAY_SPECIFIC);
        intentFilter.addAction(PlayController.ACTION_PLAY_MODE_CHANGED);
        intentFilter.addAction(PlayController.ACTION_REFRESH_PLAY_LIST);
        intentFilter.addAction(PlayController.ACTION_SEEK_BAR_PROGRESS_CHANGED);
        mediaPlayer = new MediaPlayer();
        currentPlayMode = PlayMode.REPEAT_ALL;
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

        new Thread() {
            @Override
            public void run() {
                while (controlProgressUpdateWorkThread) {
                    if (mediaPlayer.isPlaying()) {
                        int currentSeekPosition = mediaPlayer.getCurrentPosition();
                        Intent intent = new Intent();
                        intent.putExtra(PlayController.STATE_SERVICE_UPDATE_SEEK_BAR_PROGRESS, currentSeekPosition);
                        intent.setAction(PlayController.STATE_SERVICE_UPDATE_SEEK_BAR_PROGRESS);
                        localBroadcastManager.sendBroadcast(intent);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                super.run();
            }
        }.start();
        setListeners();
        super.onCreate();
    }

    private void setListeners() {
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mp.reset();
                ToastUtils.makeText("播放错误, 请重试!").show();
                sendBroadcastControlCenter(PlayController.STATE_SERVICE_STOP);
                return false;
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (playState.getCurrentPlayMode() != PlayMode.ORDER) {
                    if (playState.getListSize() == 1) {
                        mediaPlayer.setLooping(true);
                        mediaPlayer.seekTo(0);
                        mediaPlayer.start();
                        return;
                    }
                    if (playState.getCurrentPlayMode() == PlayMode.REPEAT_ONE) {
                        mediaPlayer.setLooping(true);
                        mediaPlayer.seekTo(0);
                        mediaPlayer.start();
                    }
                } else if (playState.getCurrentPlayMode() == playState.getListSize()) {
                    return;
                }
                playNext();
                Log.i(TAG, "onCompletion: ");
            }
        });
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Intent intent = new Intent();
                intent.putExtra(PlayController.STATE_SERVICE_UPDATE_BUFFER_PROGRESS, percent);
                intent.setAction(PlayController.STATE_SERVICE_UPDATE_BUFFER_PROGRESS);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                Log.d(TAG, "onSeekComplete() called with: mp = [" + mp + "]");
            }
        });
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d(TAG, "onPrepared() called with: mp = [" + mp + "]");
            }
        });
    }

    public class PlayControlBinder extends Binder{
        public void playPrev() {
            MusicPlayService.this.playPrevious();
        }

        public void playNext() {
            MusicPlayService.this.playNext();
        }

        public void playOfPause() {
            MusicPlayService.this.playOrPause();
        }

        public void seekToPosition(int timeMillis) {
            if (mediaPlayer != null) {
                mediaPlayer.seekTo(timeMillis);
            }
        }

        public void changePlayMode(int mode) {
            currentPlayMode = mode;
        }
    }
private PlayControlBinder playControlBinder;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ");
        playControlBinder = new PlayControlBinder();
        return playControlBinder;
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
//                Log.i(TAG, "onReceive: " + action);
                switch (action) {
                    case PlayController.ACTION_PLAY_SPECIFIC:
                        int position = intent.getIntExtra(PlayController.ACTION_PLAY_SPECIFIC, 0);
                        playSpecific(position);
                        break;
                    case PlayController.ACTION_PLAY_NEXT:
                        playNext();
                        break;
                    case PlayController.ACTION_PLAY_PREV:
                        playPrevious();
                        break;
                    case PlayController.ACTION_REFRESH_PLAY_LIST:
                        prepareUpdatePlayList();
                        break;
                    case PlayController.ACTION_PLAY_OR_PAUSE:
                        playOrPause();
                        break;
                    case PlayController.ACTION_SEEK_BAR_PROGRESS_CHANGED:
                        int seekIndex = intent.getIntExtra(PlayController.ACTION_SEEK_BAR_PROGRESS_CHANGED, 0);
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.seekTo(seekIndex);
                        }
                    case PlayController.ACTION_PLAY_MODE_CHANGED:
                        currentPlayMode = intent.getIntExtra(PlayController.ACTION_PLAY_MODE_CHANGED, 0);
                        break;
                }
            }
        };
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 播放或暂停
     */
    public void playOrPause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playState.setPlaying(false);
            sendBroadcastControlCenter(PlayController.STATE_SERVICE_PAUSE);
        } else {
            mediaPlayer.start();
            playState.setPlaying(true);
            sendBroadcastControlCenter(PlayController.STATE_SERVICE_PLAYING);
        }
    }

    /**
     * 请求网络, 准备播放
     */
    public void playSpecific(int specificIndex) {
        if (specificIndex == -1) {
            return;
        }
        // 如果列表仅有一首歌, 则循环播放
        if (playState.getListSize() == 1) {
            mediaPlayer.setLooping(true);
        } else {
            mediaPlayer.setLooping(false);
        }
        // 如果不是点击当前歌曲才换歌
        if (playState.getCurrentPosition() != specificIndex) {
            // 更新全局播放索引
            playState.setCurrentPosition(specificIndex);
            sendBroadcastControlCenter(PlayController.STATE_SERVICE_PLAYING);
            // 网络歌曲uri
            String songUri = UriUtils.getSongFile(playState.getSongID());
            preparePlay(songUri);
        }
    }

    /**
     * 请求网络, 准备播放
     */
    private void preparePlay(String songUri) {
        Log.d(TAG, "preparePlay() called with: songUri = [" + songUri + "]");
        HttpUtils.sendHttpRequest(songUri, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                playState.setPlaying(false);
                sendBroadcastControlCenter(PlayController.STATE_SERVICE_STOP);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final SongInfo song = GsonUtils.handlerSongInfoByRequestPlay(response.body().string());
                Log.i(TAG, "onResponse: " + song);
                if (mediaPlayer.isPlaying()) {
                }
                mediaPlayer.reset();
                mediaPlayer.setDataSource(song.FileLink);
                mediaPlayer.prepare();
                mediaPlayer.start();
                playState.setPlaying(true);
            }
        });
    }

    /**
     * 播放下一曲
     */
    public void playNext() {
        int index = -1;
        int currentPosition = playState.getCurrentPosition();
        int size = playState.getListSize();
        switch (currentPlayMode) {
            case PlayMode.REPEAT_ALL:
            case PlayMode.REPEAT_ONE:
                index = ++currentPosition >= size ? 0 : currentPosition;
                break;
            case PlayMode.MODE_SHUFFLE:
                index = getShuffleIndex();
                break;
            case PlayMode.ORDER:
                index++;
                if (index >= playState.getListSize()) {
                    sendBroadcastControlCenter(PlayController.STATE_SERVICE_STOP);
                    return;
                }
                break;
        }
        playSpecific(index);
    }

    /**
     * 播放上一曲
     */
    public void playPrevious() {
        int index = -1;
        int currentPosition = playState.getCurrentPosition();
        switch (currentPlayMode) {
            case PlayMode.REPEAT_ALL:
            case PlayMode.ORDER:
            case PlayMode.REPEAT_ONE:
                index = --currentPosition < 0 ? playState.getListSize() - 1 : currentPosition;
                break;
        }
        playSpecific(index);
    }

    /**
     * 即将更新播放列表
     */
    public void prepareUpdatePlayList() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.reset();
        }
    }

    public void updatePlayInfo() {

    }

    /**
     * 发送广播工具方法
     *
     * @param action Intent Action
     */
    public void sendBroadcastControlCenter(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("position", playState.getCurrentPosition());
        localBroadcastManager.sendBroadcast(intent);
        Log.d(TAG, "sendBroadcastControlCenter() called with: action = [" + action + "]");
    }

    /**
     * 获得随机位置
     *
     * @return 新生成的随机
     */
    private int getShuffleIndex() {
        int index;
        int currentIndex = playState.getCurrentPosition();
        do {
            index = new Random().nextInt(playState.getListSize());
        } while (currentIndex == index);
        return index;
    }

    /**
     * 打乱播放列表
     */
    private void getSufflePlaylist() {

    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        // 停止更新播放进度
        controlProgressUpdateWorkThread = false;
        // 释放资源, 解除广播接收注册
        mediaPlayer.release();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
        // 停止service
        stopSelf();
        super.onDestroy();
    }
}
