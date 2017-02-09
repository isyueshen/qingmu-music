package com.luna1970.qingmumusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.luna1970.qingmumusic.Gson.Album;
import com.luna1970.qingmumusic.Gson.AlbumInfo;
import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.adapter.SongListAdapter;
import com.luna1970.qingmumusic.listener.CustomRecyclerItemOnClickListener;
import com.luna1970.qingmumusic.util.GlobalConst;
import com.luna1970.qingmumusic.util.GsonUtils;
import com.luna1970.qingmumusic.util.HttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.luna1970.qingmumusic.application.MusicApplication.playState;

public class AlbumDetailActivity extends BaseActivity {
    private static final String TAG = "AlbumDetailActivity";
    private Album album;
    private List<Song> songList;
    private SongListAdapter songListAdapter;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
        songList = new ArrayList<>();
        getIntentData();
        setToolbar();
        setViews();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
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
        ImageView toolbarAlbumIv = (ImageView) findViewById(R.id.toolbar_background_iv);

        ViewCompat.setTransitionName(toolbarAlbumIv, "luna");
        Glide.with(this).load(album.albumPicPath).into(toolbarAlbumIv);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(album.author + " - " + album.title);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        songListAdapter = new SongListAdapter(songList, new CustomRecyclerItemOnClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                intent.setAction(GlobalConst.ACTION_REFRESH_PLAY_LIST);
                localBroadcastManager.sendBroadcast(intent);
                playState.updatePlayList(songList);
                intent = new Intent();
                intent.setAction(GlobalConst.ACTION_PLAY_SPECIFIC);
                intent.putExtra(GlobalConst.ACTION_PLAY_SPECIFIC, position);
                localBroadcastManager.sendBroadcast(intent);

            }
        });
        recyclerView.setAdapter(songListAdapter);
        recyclerView.setNestedScrollingEnabled(false);
    }


    private void sendRequest() {
        String apiPath = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.album.getAlbumInfo&format=json&album_id=" + album.albumId;
        HttpUtils.sendHttpRequest(apiPath, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final AlbumInfo albumInfo = GsonUtils.handlerAlbumInfo(response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        songList.clear();
                        songList.addAll(albumInfo.songList);
                        songListAdapter.notifyDataSetChanged();
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void startCustomActivity(Activity activity, Album type, View view) {
        Intent intent = new Intent();
        intent.setClass(activity, AlbumDetailActivity.class);
        intent.putExtra("album", type);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, "luna");
        ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
    }
}
