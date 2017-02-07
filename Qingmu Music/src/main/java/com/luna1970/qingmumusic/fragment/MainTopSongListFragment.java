package com.luna1970.qingmumusic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.activity.MusicPlayActivity;
import com.luna1970.qingmumusic.activity.TopBillboardActivity;
import com.luna1970.qingmumusic.adapter.RecommendListAdapter;
import com.luna1970.qingmumusic.listener.CustomRecyclerItemOnClickListener;
import com.luna1970.qingmumusic.util.DataCentral;
import com.luna1970.qingmumusic.util.GsonUtils;
import com.luna1970.qingmumusic.util.HttpUtils;
import com.luna1970.qingmumusic.util.PlayController;
import com.luna1970.qingmumusic.util.UriUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.luna1970.qingmumusic.application.MusicApplication.playState;

/**
 * Created by Yue on 1/29/2017.
 *
 */

public class MainTopSongListFragment extends Fragment {
    private static final String TAG = "MainRecommendListFrag";

    private List<Song> songList;
    private RecommendListAdapter recommendListAdapter;
    private Bundle bundle;
    private int type;
    private LocalBroadcastManager localBroadcastManager;
    private LinearLayout linearLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend_list, container, false);
        setView(view);
        return view;
    }

    public void setView(View view) {
        TextView titleTv= (TextView) view.findViewById(R.id.title_tv);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        bundle = getArguments();
//        Log.i(TAG, "setView: " + getArguments());
        if (bundle != null) {
            titleTv.setText(bundle.getString("title"));
        }

        linearLayout = (LinearLayout) view.findViewById(R.id.start_top_billboard_detail_area);
        songList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recommendListAdapter = new RecommendListAdapter(songList, new CustomRecyclerItemOnClickListener() {
            @Override
            public void onClick(int position) {
                preparePlay(position);
                if (playState.getListSize() == 0) {
                    Intent intent = new Intent(getContext(), MusicPlayActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.start_activity_translate_in_bottom2top, R.anim.none);
                }
            }
        });
        recyclerView.setAdapter(recommendListAdapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setNestedScrollingEnabled(false);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TopBillboardActivity.class);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        type = 1;
        bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt("type");
        }
        requestSongInfoList(type);
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
    }

    private void requestSongInfoList(int type) {
        DataCentral.requestTopSongList(type, 20, new DataCentral.ResponseSongListListener() {
            @Override
            public void responseSongList(final List<Song> songList) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainTopSongListFragment.this.songList.addAll(songList);
                        recommendListAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    /**
     * 外部传参实例化
     * @param type 榜单类型
     * @param title 榜单标题
     * @return 实例
     */
    public static Fragment newInstance(int type, String title) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putInt("type", type);
        MainTopSongListFragment mainTopSongListFragment = new MainTopSongListFragment();
        mainTopSongListFragment.setArguments(bundle);
        return mainTopSongListFragment;
    }

    private void preparePlay(final int position) {
        DataCentral.requestTopSongToPlay(type, position);
    }
}
