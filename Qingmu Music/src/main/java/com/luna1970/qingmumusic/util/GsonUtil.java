package com.luna1970.qingmumusic.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luna1970.qingmumusic.Gson.Album;
import com.luna1970.qingmumusic.Gson.AlbumInfo;
import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.Gson.SongInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yue on 1/27/2017.
 *
 */

public class GsonUtil {
    private static final String TAG = "GsonUtil";
    public static List<Album> handleAlbumList(String json) {
        List<Album> albumList = new ArrayList<>();
        try {
            json = new JSONObject(json).getJSONObject("plaze_album_list").getJSONObject("RM").getJSONObject("album_list").getJSONArray("list").toString();
            albumList = new Gson().fromJson(json, new TypeToken<List<Album>>(){}.getType());
            Log.d(TAG, "okhttp3 onResponse: albumList size : " + albumList.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return albumList;
    }

    public static AlbumInfo handlerAlbumInfo(String json) {
        AlbumInfo albumInfo = null;
        try {
            String albumInfoString = new JSONObject(json).getJSONObject("albumInfo").toString();
            albumInfo = new Gson().fromJson(albumInfoString, AlbumInfo.class);
            String songListString = new JSONObject(json).getJSONArray("songlist").toString();
            List<Song> songList = new Gson().fromJson(songListString, new TypeToken<List<Song>>(){}.getType());
            if (albumInfo != null) {
                albumInfo.songList = songList;
                Log.d(TAG, "handlerAlbumInfo: songList size " + albumInfo.songList.size());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return albumInfo;
    }
    public static SongInfo handlerSongInfoByRequestPlay(String json) {
        SongInfo songInfo = null;
        try {
            String albumInfoString = new JSONObject(json).getJSONObject("bitrate").toString();
            songInfo = new Gson().fromJson(albumInfoString, SongInfo.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return songInfo;
    }

    public static List<Song> handlerSongInfoListByRequestDailyRecommend(String json) {
        List<Song> songInfoList = null;
        try {
            String songListString = new JSONObject(json).getJSONArray("song_list").toString();
            songInfoList = new Gson().fromJson(songListString, new TypeToken<List<Song>>(){}.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return songInfoList;
    }
}
