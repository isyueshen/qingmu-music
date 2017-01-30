package com.luna1970.qingmumusic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.adapter.NewSongListAdapter;
import com.luna1970.qingmumusic.application.MusicApplication;
import com.luna1970.qingmumusic.listener.CustomRecyclerItemOnClickListener;
import com.luna1970.qingmumusic.util.GlobalMusicPlayControllerConst;
import com.luna1970.qingmumusic.util.GsonUtil;
import com.luna1970.qingmumusic.util.HttpUtils;
import com.luna1970.qingmumusic.util.UriUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.luna1970.qingmumusic.application.MusicApplication.mediaPlayer;
import static com.luna1970.qingmumusic.application.MusicApplication.position;
/**
 * Created by Yue on 1/30/2017.
 *
 */

public class BannerFocusFragment extends Fragment {
    private static final String TAG = "MainRecommendListFrag";
    private List<Song> songList;
    private NewSongListAdapter newSongListAdapter;
    private int songId;
    private Bundle bundle;
    private ImageView backgroundIv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banner_focus, container, false);
        setView(view);
        return view;
    }

    public void setView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        songList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        newSongListAdapter = new NewSongListAdapter(songList, new CustomRecyclerItemOnClickListener() {
            @Override
            public void onClick(int position) {
                songId = position;
                preparePlay(position);
            }

            @Override
            public void onPopMenuOnClick(View view) {
                super.onPopMenuOnClick(view);
                final PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.getMenuInflater().inflate(R.menu.pop_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        recyclerView.setAdapter(newSongListAdapter);
//        SnapHelper snapHelper = new LinearSnapHelper();
//        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setNestedScrollingEnabled(false);

        backgroundIv = (ImageView) view.findViewById(R.id.background);
        Log.i(TAG, "setView: " + (backgroundIv == null));

        recyclerView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestSongInfoList();
        initBackGround();
    }

    private void initBackGround() {
        String apiUri = UriUtils.getBannerFocusImagePath(1);
        HttpUtils.sendHttpRequest(apiUri, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String backgroundPath = GsonUtil.getBannerUri(response.body().string());
                Log.i(TAG, "onResponse: " + backgroundPath);
                Log.i(TAG, "setView: " + (backgroundIv == null));
                Runnable runnable = new Runnable() {
                    public void run() {
                        Glide.with(getContext()).load(backgroundPath).into(backgroundIv);
                    }
                };
                getActivity().runOnUiThread(runnable);
            }
        });
    }

    private void requestSongInfoList() {
        String apiUri = UriUtils.getNewSong(20);
        HttpUtils.sendHttpRequest(apiUri, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                songList.addAll(GsonUtil.handlerSongListByRequestDailyRecommend(response.body().string()));
                Runnable runnable = new Runnable() {
                    public void run() {
                        newSongListAdapter.notifyDataSetChanged();
                    }
                };
                getActivity().runOnUiThread(runnable);
            }
        });
    }

    private void preparePlay(final int posi) {
        String apiUri = UriUtils.getNewSong(100);
        HttpUtils.sendHttpRequest(apiUri, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                MusicApplication.refreshPlayList(GsonUtil.handlerSongListByRequestDailyRecommend(response.body().string()));
                Intent intent = new Intent();
                intent.setAction(GlobalMusicPlayControllerConst.ACTION_FRAGMENT_PREPARE_PLAY);
                position = posi;
                Log.i(TAG, "onResponse: " + songList);
                Log.i(TAG, "onResponse: " + posi);
                getActivity().sendBroadcast(intent);
            }
        });
    }
}
