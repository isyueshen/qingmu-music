package com.luna1970.qingmumusic.dao.impl;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.luna1970.qingmumusic.dao.MusicDao;
import com.luna1970.qingmumusic.entity.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yue on 1/9/2017.
 *
 */

public class MusicCursorDaoImpl implements MusicDao {
    private Context context;

    public MusicCursorDaoImpl(Context context) {
        this.context = context;
    }

    @Override
    public List<Music> findAll() {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

        List<Music> musics = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            Music music;
            while (cursor.moveToNext()) {
                music = new Music();
                music.setId(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                music.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                music.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                music.setAlbumID(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                music.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                music.setDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                music.setData(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                music.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                musics.add(music);
            }
        }
        cursor.close();
        return musics;
    }
}
