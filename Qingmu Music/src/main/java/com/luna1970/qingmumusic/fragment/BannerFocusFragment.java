package com.luna1970.qingmumusic.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
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
import com.luna1970.qingmumusic.activity.MusicPlayActivity;
import com.luna1970.qingmumusic.adapter.NewSongListAdapter;
import com.luna1970.qingmumusic.application.MusicApplication;
import com.luna1970.qingmumusic.listener.CustomRecyclerItemOnClickListener;
import com.luna1970.qingmumusic.util.DataCentral;
import com.luna1970.qingmumusic.util.GsonUtils;
import com.luna1970.qingmumusic.util.HttpUtils;
import com.luna1970.qingmumusic.util.GlobalConst;
import com.luna1970.qingmumusic.util.UriUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.luna1970.qingmumusic.application.MusicApplication.currentNetType;
import static com.luna1970.qingmumusic.application.MusicApplication.playState;

/**
 * Created by Yue on 1/30/2017.
 * 
 */

public class BannerFocusFragment extends Fragment {
    private static final String TAG = "MainRecommendListFrag";
    private List<Song> songList;
    private NewSongListAdapter newSongListAdapter;
    private ImageView backgroundIv;
    private LocalBroadcastManager localBroadcastManager;
    private boolean prefNet_onlyWifi;
    private boolean prefNet_playOnCellular;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banner_focus, container, false);
        setView(view);
        return view;
    }

    public void setView(View view) {
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        songList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        newSongListAdapter = new NewSongListAdapter(songList, new CustomRecyclerItemOnClickListener() {
            @Override
            public void onClick(final int position) {
                checkUserPreferences();
                if (currentNetType != ConnectivityManager.TYPE_WIFI && prefNet_onlyWifi) {
                    return;
                }
                if (currentNetType == ConnectivityManager.TYPE_MOBILE && !prefNet_playOnCellular) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("确定打开2G/3G/4G网络播放开关吗?")
                            .setMessage("2G/3G/4G网络下会消耗流量, 请参考你的话费套餐情况进行设置!")
                            .setPositiveButton("打开播放开关", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences.Editor editor =  PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                                    editor.putBoolean("prefNet_onlyWifi", false);
                                    editor.putBoolean("prefNet_playOnCellular", true);
                                    editor.apply();
                                    preparePlay(position);
                                }
                            })
                            .setNegativeButton(R.string.userAction_cancel, null)
                            .show();
                    return;
                }
                preparePlay(position);
            }

            @Override
            public void onPopMenuOnClick(View view, final int position) {
                super.onPopMenuOnClick(view, position);
                final PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.getMenuInflater().inflate(R.menu.pop_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return false;
                    }
                });
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.download:
                                break;
                            case R.id.play_next:
                                playState.insertPlayNext(songList.get(position));
                                break;
                            case R.id.favorite:
                                break;
                        }
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
    public void checkUserPreferences() {
        prefNet_onlyWifi = PreferenceManager.getDefaultSharedPreferences(MusicApplication.getInstance().getApplicationContext()).getBoolean("prefNet_onlyWifi", false);
        prefNet_playOnCellular = PreferenceManager.getDefaultSharedPreferences(MusicApplication.getInstance().getApplicationContext()).getBoolean("prefNet_playOnCellular", true);
    }
    @Override
    public void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop() called");
        super.onStop();
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart() called");
        super.onStart();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause() called");
        super.onPause();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestSongInfoList();
        initBackGround();
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
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
                final String backgroundPath = GsonUtils.getBannerUri(response.body().string());
                Log.i(TAG, "onResponse: " + backgroundPath);
                Log.i(TAG, "setView: " + (backgroundIv == null));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(getContext()).load(backgroundPath).into(backgroundIv);
                    }
                });
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
                songList.addAll(GsonUtils.handlerSongListByRequestDailyRecommend(response.body().string()));
                Runnable runnable = new Runnable() {
                    public void run() {
                        newSongListAdapter.notifyDataSetChanged();
                    }
                };
                getActivity().runOnUiThread(runnable);
            }
        });
    }

    private void preparePlay(final int position) {
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
                Intent intent = new Intent();
                intent.setAction(GlobalConst.ACTION_REFRESH_PLAY_LIST);
                localBroadcastManager.sendBroadcast(intent);
                playState.updatePlayList(GsonUtils.handlerSongListByRequestDailyRecommend(response.body().string()));
                intent = new Intent();
                intent.setAction(GlobalConst.ACTION_PLAY_SPECIFIC);
                int index = position - 1;
                intent.putExtra(GlobalConst.ACTION_PLAY_SPECIFIC, index);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
        if (playState.getListSize() == 0) {
            Intent intent = new Intent(getContext(), MusicPlayActivity.class);
            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(R.anim.start_activity_translate_in_bottom2top, R.anim.none);
        }
    }
}
