package com.luna1970.qingmumusic.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.luna1970.qingmumusic.Gson.Album;
import com.luna1970.qingmumusic.Gson.AlbumInfo;
import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.Gson.SongInfo;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.adapter.AlbumDetailSongListAdapter;
import com.luna1970.qingmumusic.application.MusicApplication;
import com.luna1970.qingmumusic.listener.CustomRecyclerItemOnClickListener;
import com.luna1970.qingmumusic.util.GsonUtil;
import com.luna1970.qingmumusic.util.HttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AlbumDetailActivity extends BaseActivity {
    private static final String TAG = "AlbumDetailActivity";
    private Album album;
    private List<Song> songList;
    private AlbumDetailSongListAdapter albumDetailSongListAdapter;
    private int songId;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
        mediaPlayer = ((MusicApplication)getApplication()).mediaPlayer;
        songList = new ArrayList<>();
        getIntentData();
        setToolbar();
        setViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (songList.size() == 0) {
            sendRequest();
        }
    }

    public void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        album = bundle.getParcelable("album");
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setViews() {
        ImageView toolbarAlbumIv = (ImageView) findViewById(R.id.toolbar_album_iv);
        Glide.with(this).load(album.albumPicPath).into(toolbarAlbumIv);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(album.author + " - " + album.title);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        albumDetailSongListAdapter = new AlbumDetailSongListAdapter(songList, new CustomRecyclerItemOnClickListener() {
            @Override
            public void onClick(int id) {
                songId = id;
                getFile();
            }
        });
        recyclerView.setAdapter(albumDetailSongListAdapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    public void getFile() {
        String apiPath = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&format=json&from:webapp_music&method=baidu.ting.song.play&songid=" + songId;
        HttpUtils.sendHttpRequest(apiPath, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final SongInfo song = GsonUtil.handlerSongInfoByRequestPlay(response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mediaPlayer.reset();
                        try {
                            mediaPlayer.setDataSource(song.FileLink);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void sendRequest() {
        String apiPath = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.album.getAlbumInfo&format=json&album_id=" + album.albumId;
        HttpUtils.sendHttpRequest(apiPath, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final AlbumInfo albumInfo = GsonUtil.handlerAlbumInfo(response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        songList.addAll(albumInfo.songList);
                        albumDetailSongListAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
