package com.luna1970.qingmumusic.application;

import android.app.Application;

import com.luna1970.qingmumusic.dao.MusicDao;
import com.luna1970.qingmumusic.dao.impl.MusicCursorDaoImpl;
import com.luna1970.qingmumusic.entity.Music;

import java.util.List;

/**
 * Created by Yue on 1/9/2017.
 *
 */

public class MusicApplication extends Application{
    public static List<Music> musicLists;
    public static boolean isPlaying;
    public static Integer position = 0;
    public static int prevPosition;

    @Override
    public void onCreate() {
        super.onCreate();
        MusicDao musicDao = new MusicCursorDaoImpl(this);
        musicLists = musicDao.findAll();
    }
}
