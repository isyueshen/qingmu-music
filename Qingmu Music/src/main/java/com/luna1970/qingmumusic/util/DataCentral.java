package com.luna1970.qingmumusic.util;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;

import com.luna1970.qingmumusic.Gson.QuerySuggestion;
import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.application.MusicApplication;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.luna1970.qingmumusic.application.MusicApplication.playState;

/**
 * Created by Yue on 2/8/2017.
 *
 */

public class DataCentral {
    private LocalBroadcastManager localBroadcastManager;
    private static DataCentral dataCentral;

    private DataCentral() {
        localBroadcastManager = LocalBroadcastManager.getInstance(MusicApplication.getInstance().getApplicationContext());
    }

    public static DataCentral getInstance() {
        if (dataCentral == null) {
            dataCentral = new DataCentral();
        }
        return dataCentral;
    }




    public void requestTopSongToPlay(final int type, final int position) {
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
                intent.setAction(GlobalConst.ACTION_REFRESH_PLAY_LIST);
                localBroadcastManager.sendBroadcast(intent);
                List<Song> songList = GsonUtils.handlerSongListByRequestDailyRecommend(response.body().string());
                playState.updatePlayList(songList);
                intent = new Intent();
                intent.setAction(GlobalConst.ACTION_PLAY_SPECIFIC);
                intent.putExtra(GlobalConst.ACTION_PLAY_SPECIFIC, position);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
    }

    public void requestTopSongList(int type, int size, final ResponseSongListListener responseSongListListener) {
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
                responseSongListListener.onResponse(songList);
            }
        });
    }

    public void querySuggestion(String query, final ResponseQuerySuggestionListener responseQuerySuggestionListener) {
        try {
            String apiUri = UriUtils.getQueryResultOnlySong(query);
//            Logger.d(apiUri);
            HttpUtils.sendHttpRequest(apiUri, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message.obtain(new Handler(Looper.getMainLooper()), new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.makeText("failed").show();
                        }
                    }).sendToTarget();
                    responseQuerySuggestionListener.onResponse(null);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    responseQuerySuggestionListener.onResponse(GsonUtils.getQuerySuggestion(response.body().string()));
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public interface ResponseSongListListener {
        void onResponse(List<Song> songList);
    }

    public interface ResponseQuerySuggestionListener {
        void onResponse(QuerySuggestion querySuggestion);
    }


}
