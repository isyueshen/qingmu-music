package com.luna1970.qingmumusic.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.luna1970.qingmumusic.Gson.Album;
import com.luna1970.qingmumusic.Gson.AlbumInfo;
import com.luna1970.qingmumusic.Gson.Billboard;
import com.luna1970.qingmumusic.Gson.QueryResult;
import com.luna1970.qingmumusic.Gson.QuerySuggestion;
import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.Gson.SongInfo;
import com.luna1970.qingmumusic.Gson.TopBillboard;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yue on 1/27/2017.
 *
 */

public class GsonUtils {
    private static final String TAG = "GsonUtils";

    public static List<Album> handleAlbumList(String json) {
        List<Album> albumList = new ArrayList<>();
        try {
            json = new JSONObject(json).getJSONObject("plaze_album_list").getJSONObject("RM").getJSONObject("album_list").getJSONArray("list").toString();
            albumList = new Gson().fromJson(json, new TypeToken<List<Album>>() {
            }.getType());
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
            albumInfo = new GsonBuilder().setExclusionStrategies(new SpecificClassExclusionStrategy(null, BaseModel.class)).create().fromJson(albumInfoString, AlbumInfo.class);
            String songListString = new JSONObject(json).getJSONArray("songlist").toString();
            List<Song> songList = new GsonBuilder().setExclusionStrategies(new SpecificClassExclusionStrategy(null, BaseModel.class)).create().fromJson(songListString, new TypeToken<List<Song>>() {}.getType());
            if (albumInfo != null) {
                albumInfo.songList = songList;
                Log.d(TAG, "handlerAlbumInfo: playList size " + albumInfo.songList.size());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return albumInfo;
    }

    /**
     * request songList file path by songList id
     *
     * @param json original response json data
     * @return SongInfo Object
     */
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

    public static List<Song> handlerSongListByRequestDailyRecommend(String json) {
        List<Song> songList = null;
        try {
            String songListString = new JSONObject(json).getJSONArray("song_list").toString();
            songList = new GsonBuilder().setExclusionStrategies(new SpecificClassExclusionStrategy(null, BaseModel.class)).create().fromJson(songListString, new TypeToken<List<Song>>() {}.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return songList;
    }

    public static String getBannerUri(String json) {
        String path = null;
        try {
            JSONArray jsonArray = new JSONObject(json).getJSONObject("result").getJSONObject("focus").getJSONArray("result");
            JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length() - 1);
            path = jsonObject.getString("randpic");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static Billboard handlerBillboard(String json) {
        Billboard billboard = null;
        try {
            billboard = new Gson().fromJson(new JSONObject(json).getJSONObject("billboard").toString(), Billboard.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return billboard;
    }

    public static TopBillboard handlerTopBillboard(String json) {
        TopBillboard topBillboard = null;
        topBillboard = new GsonBuilder().setExclusionStrategies(new SpecificClassExclusionStrategy(null, BaseModel.class)).create().fromJson(json, TopBillboard.class);
        return topBillboard;
    }

    /**
     * 获得搜索建议(仅歌名)
     * @param json raw String
     * @return QuerySuggestion Object
     */
    public static QuerySuggestion getQuerySuggestion(String json) {
        return new Gson().fromJson(json, QuerySuggestion.class);
    }

    /**
     * 获得搜索结果
     * @param json raw String
     * @return QuerySuggestion Object
     */
    public static QueryResult getQueryResult(String json) {
//        try {
//            json = new JSONObject(json).getJSONObject("result").getJSONObject("song_info").toString();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return new GsonBuilder().setExclusionStrategies(new SpecificClassExclusionStrategy(null, BaseModel.class)).create().fromJson(json, QueryResult.class);
    }
}
