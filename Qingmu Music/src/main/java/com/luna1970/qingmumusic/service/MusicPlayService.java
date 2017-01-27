package com.luna1970.qingmumusic.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import static com.luna1970.qingmumusic.application.MusicApplication.position;
import static com.luna1970.qingmumusic.application.MusicApplication.prevPosition;

import com.luna1970.qingmumusic.application.MusicApplication;
import com.luna1970.qingmumusic.entity.Music;
import com.luna1970.qingmumusic.util.GlobalMusicPlayConst;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by Yue on 1/9/2017.
 *
 */

public class MusicPlayService extends Service {
    private static final String TAG = "MusicPlayService";
    private List<Music> musics;
    private IntentFilter intentFilter;
    private Intent intent;
    private boolean hasPlayed;
    private BroadcastReceiver broadcastReceiver;
    private MediaPlayer mediaPlayer;
    private String[] playModeContainer;
    private String currentPlayMode;
    private boolean controlWorkThread;

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: " + position + "  --> "+ MusicApplication.position);
        mediaPlayer = new MediaPlayer();
        controlWorkThread = true;
        musics = MusicApplication.musicLists;
        Log.i(TAG, "onCreate: " + musics.size());
        intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_OR_PAUSE);
        intentFilter.addAction(GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_NEXT);
        intentFilter.addAction(GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_PREV);
        intentFilter.addAction(GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_SPECIFIC);

        intentFilter.addAction(GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_MODE_REPEAT_ALL);
        intentFilter.addAction(GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_MODE_REPEAT_ONCE);
        intentFilter.addAction(GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_MODE_SHUFFLE);
        intentFilter.addAction(GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_MODE_ORDER);

        intentFilter.addAction(GlobalMusicPlayConst.ACTION_ACTIVITY_SEEK_BAR_PROGRESS_CHANGED);

        playModeContainer = new String[]{GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_MODE_REPEAT_ALL, GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_MODE_REPEAT_ONCE, GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_MODE_SHUFFLE, GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_MODE_ORDER};
        currentPlayMode = GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_MODE_REPEAT_ALL;
        new Thread() {
            @Override
            public void run() {
                while (controlWorkThread) {
                    if (mediaPlayer.isPlaying()) {
                        int currentPosition = mediaPlayer.getCurrentPosition();
                        intent = new Intent();
                        intent.putExtra(GlobalMusicPlayConst.ACTION_SERVICE_UPDATE_SEEK_BAR_PROGRESS, currentPosition);
                        intent.setAction(GlobalMusicPlayConst.ACTION_SERVICE_UPDATE_SEEK_BAR_PROGRESS);
                        sendBroadcast(intent);
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
        super.onCreate();
        setListeners();
    }

    private void setListeners() {
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mp.reset();
                return false;
            }
        });
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                prevPosition = position;
                if (currentPlayMode.equals(playModeContainer[0])) {
                    position = ++position>=musics.size() ? 0 : position;
                } else if (currentPlayMode.equals(playModeContainer[1])) {
                    mediaPlayer.start();
                    return;
                } else if (currentPlayMode.equals(playModeContainer[2])) {
                    position = getShufflePosition(position);
                } else if (currentPlayMode.equals(playModeContainer[3])) {
                    ++position;
                    if (position>=musics.size()) {
                        mediaPlayer.stop();
                        MusicPlayService.this.intent = new Intent();
                        MusicPlayService.this.intent.setAction(GlobalMusicPlayConst.ACTION_SERVICE_PAUSE);
                        sendBroadcast(MusicPlayService.this.intent);
                        return;
                    }
                }
                playMusic(position, GlobalMusicPlayConst.ACTION_SERVICE_PLAYING);
            }
        });
        Log.i(TAG, "onStartCommand: ");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "onReceive: " + intent.getAction());
                prevPosition = position;
                switch (intent.getAction()) {
                    case GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_OR_PAUSE:
                        if (hasPlayed) {
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.pause();
                                intent = new Intent();
                                intent.setAction(GlobalMusicPlayConst.ACTION_SERVICE_PAUSE);
                                sendBroadcast(intent);
                            } else {
                                mediaPlayer.start();
                                sendCustomBroadcast(GlobalMusicPlayConst.ACTION_SERVICE_PLAY_CONTINUE);
                            }
                        } else {
                            hasPlayed = true;
                            playMusic(position, GlobalMusicPlayConst.ACTION_SERVICE_PLAYING);
                            mediaPlayer.seekTo(intent.getIntExtra("seekBarProgress", 0));
                        }
                        break;
                    case GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_NEXT:
                        if (currentPlayMode.equals(playModeContainer[2])) {
                            position = getShufflePosition(position);
                        } else {
                            position = ++position>=musics.size() ? 0 : position;
                        }
                        hasPlayed = true;
                        playMusic(position, GlobalMusicPlayConst.ACTION_SERVICE_PLAYING);
                        break;
                    case GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_PREV:
                        if (currentPlayMode.equals(playModeContainer[2])) {
                            position = getShufflePosition(position);
                        } else {
                            position = --position<0 ? musics.size()-1 : position;
                        }
                        hasPlayed = true;
                        playMusic(position, GlobalMusicPlayConst.ACTION_SERVICE_PLAYING);
                        break;
                    case GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_SPECIFIC:
                        int newPosition = intent.getIntExtra("position", 0);
                        if (newPosition != position) {
                            position = newPosition;
                            playMusic(newPosition, GlobalMusicPlayConst.ACTION_SERVICE_PLAYING);
                        } else {
                            if (!mediaPlayer.isPlaying()) {
                                mediaPlayer.start();
                                sendCustomBroadcast(GlobalMusicPlayConst.ACTION_SERVICE_PLAY_CONTINUE);
                            } else {
                                Toast.makeText(context, "当前正在播放该曲目", Toast.LENGTH_SHORT).show();
                            }
                        }
                        hasPlayed =true;
                        break;
                    case GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_MODE_REPEAT_ALL:
                        currentPlayMode = GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_MODE_REPEAT_ALL;
                        break;
                    case GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_MODE_REPEAT_ONCE:
                        currentPlayMode = GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_MODE_REPEAT_ONCE;
                        break;
                    case GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_MODE_SHUFFLE:
                        currentPlayMode = GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_MODE_SHUFFLE;
                        break;
                    case GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_MODE_ORDER:
                        currentPlayMode = GlobalMusicPlayConst.ACTION_ACTIVITY_PLAY_MODE_ORDER;
                        break;
                    case GlobalMusicPlayConst.ACTION_ACTIVITY_SEEK_BAR_PROGRESS_CHANGED:
                        int currentPosition = intent.getIntExtra(GlobalMusicPlayConst.ACTION_ACTIVITY_SEEK_BAR_PROGRESS_CHANGED, 0);
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.seekTo(currentPosition);
                        }
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
        return super.onStartCommand(intent, flags, startId);
    }

    private int getShufflePosition(int position) {
        int newPosition;
        do {
            newPosition = new Random().nextInt(musics.size());
        } while (position == newPosition);
        return newPosition;
    }

    private void playMusic(int position, String action) {
        if (MusicApplication.isPlaying) {
            mediaPlayer.stop();
        }
        if (musics.size()<=0) {
            return;
        }
        String path = musics.get(position).getData();
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        sendCustomBroadcast(action);
    }

    private void sendCustomBroadcast(String action) {
        intent = new Intent();
        intent.setAction(action);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        controlWorkThread = false;
        mediaPlayer.release();
        stopSelf();
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }
}
