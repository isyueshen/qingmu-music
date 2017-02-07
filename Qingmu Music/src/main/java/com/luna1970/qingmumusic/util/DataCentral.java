package com.luna1970.qingmumusic.util;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;

import com.luna1970.qingmumusic.Gson.Song;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.luna1970.qingmumusic.application.MusicApplication.getInstance;
import static com.luna1970.qingmumusic.application.MusicApplication.playState;

/**
 * Created by Yue on 2/8/2017.
 *
 */

public class DataCentral {
    private static LocalBroadcastManager localBroadcastManager;
    static {
        localBroadcastManager = LocalBroadcastManager.getInstance(getInstance());
    }
    public static void requestTopSongToPlay(int type, final int position) {
        String apiUri = UriUtils.getRecommendUri(type, 0, 100);
        HttpUtils.sendHttpRequest(apiUri, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message.obtain(new Handler(Looper.getMainLooper()), new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.makeText("failed").show();
                    }
                }).sendToTarget();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Intent intent = new Intent();
                intent.setAction(PlayController.ACTION_REFRESH_PLAY_LIST);
                localBroadcastManager.sendBroadcast(intent);
                playState.updatePlayList(GsonUtils.handlerSongListByRequestDailyRecommend(response.body().string()));
                intent = new Intent();
                intent.setAction(PlayController.ACTION_PLAY_SPECIFIC);
                int index = position;
                intent.putExtra(PlayController.ACTION_PLAY_SPECIFIC, index);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
    }

    public static void requestTopSongList(int type, int size, final ResponseSongListListener responseSongListListener) {
        String apiUri = UriUtils.getRecommendUri(type, 0, size);
        HttpUtils.sendHttpRequest(apiUri, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message.obtain(new Handler(Looper.getMainLooper()), new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.makeText("failed").show();
                    }
                }).sendToTarget();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<Song> songList = GsonUtils.handlerSongListByRequestDailyRecommend(response.body().string());
                responseSongListListener.responseSongList(songList);
            }
        });
    }

    public interface ResponseSongListListener {
        void responseSongList(List<Song> songList);
    }

}
