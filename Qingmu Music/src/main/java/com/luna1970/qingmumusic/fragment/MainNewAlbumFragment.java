package com.luna1970.qingmumusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luna1970.qingmumusic.Gson.Album;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.adapter.MainNewAlbumAdapter;
import com.luna1970.qingmumusic.util.GsonUtils;
import com.luna1970.qingmumusic.util.HttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Yue on 1/27/2017.
 */

public class MainNewAlbumFragment extends Fragment {
    private static final String TAG = "MainNewAlbumFragment";
    private View view;
    private List<Album> albumList;
    private MainNewAlbumAdapter mainNewAlbumAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.fragment_main_new_album, container, false);
        setView(view);
        return view;
    }

    public void setView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        albumList = new ArrayList<>();
        mainNewAlbumAdapter = new MainNewAlbumAdapter(albumList);
        recyclerView.setAdapter(mainNewAlbumAdapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String requestUrl = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.plaza.getRecommendAlbum&format=json&offset=0&limit=50";
        HttpUtils.sendHttpRequest(requestUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final List<Album> albumList = GsonUtils.handleAlbumList(response.body().string());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainNewAlbumFragment.this.albumList.clear();
                        MainNewAlbumFragment.this.albumList.addAll(albumList);
                        mainNewAlbumAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}
