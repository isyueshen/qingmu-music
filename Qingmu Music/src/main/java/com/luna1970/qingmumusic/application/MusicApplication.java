package com.luna1970.qingmumusic.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.dao.MusicDao;
import com.luna1970.qingmumusic.dao.impl.MusicCursorDaoImpl;
import com.luna1970.qingmumusic.service.MusicPlayService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yue on 1/9/2017.
 *
 */

public class MusicApplication extends Application {
    private static final String TAG = "MusicApplication";
    public static List<Song> playList;
    public static boolean isPlaying;
    public static int songId;
    public static int prevPosition;
    public static int currentPosition = -1;
    public static MediaPlayer mediaPlayer;
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        MusicDao musicDao = new MusicCursorDaoImpl(this);
        playList = new ArrayList<>();
        mediaPlayer = new MediaPlayer();
//        Intent intent = new Intent(getApplicationContext(), MusicPlayService.class);
//        startService(intent);

        // Start service
        Intent intent = new Intent();
        intent.setClass(this, MusicPlayService.class);
        startService(intent);
        mContext = getApplicationContext();
    }

    public static void refreshPlayList(List<Song> data) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.reset();
        }
        playList.clear();
        playList.addAll(data);
    }

    public static void playNext(Song song) {
        Log.i(TAG, "playNext: ");
        playList.add(currentPosition + 1, song);
    }
}
