package com.luna1970.qingmumusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.adapter.PlayListAdapter;
import com.luna1970.qingmumusic.entity.Playlist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yue on 2/11/2017.
 *
 */

public class PlayListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_play_list, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        List<Playlist> playlistData = new ArrayList<>();
        PlayListAdapter playListAdapter = new PlayListAdapter(playlistData);
        recyclerView.setAdapter(playListAdapter);
    }
}
