package com.luna1970.qingmumusic.fragment;

import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.activity.MusicPlayActivity;
import com.luna1970.qingmumusic.activity.TopBillboardActivity;
import com.luna1970.qingmumusic.adapter.TopSongListAdapter;
import com.luna1970.qingmumusic.listener.CustomRecyclerItemOnClickListener;
import com.luna1970.qingmumusic.util.DataCentral;

import java.util.ArrayList;
import java.util.List;

import static com.luna1970.qingmumusic.application.MusicApplication.playState;

/**
 * Created by Yue on 1/29/2017.
 *
 */

public class MainTopSongListFragment extends Fragment {
//    private static final String TAG = "MainRecommendListFrag";

    private List<Song> songList;
    private TopSongListAdapter topSongListAdapter;
    private Bundle bundle;
    private int type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend_list, container, false);
        setView(view);
        return view;
    }

    /**
     * 初始化View, 并设置数据
     * @param view 父View
     */
    public void setView(View view) {
        TextView titleTv= (TextView) view.findViewById(R.id.title_tv);
        // 设置标题
        bundle = getArguments();
//        Log.i(TAG, "setView: " + getArguments());
        if (bundle != null) {
            titleTv.setText(bundle.getString("title"));
        }
        // 设置数据
        songList = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        topSongListAdapter = new TopSongListAdapter(songList, new CustomRecyclerItemOnClickListener() {
            @Override
            public void onClick(int position) {
                preparePlay(position);
                // 若播放列表没有歌曲, 则启动播放页
                if (playState.getListSize() == 0) {
                    Intent intent = new Intent(getContext(), MusicPlayActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.start_activity_translate_in_bottom2top, R.anim.none);
                }
            }
        });
        recyclerView.setAdapter(topSongListAdapter);
        // 粘性滚动
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        // 防止粘滞
        recyclerView.setNestedScrollingEnabled(false);

        // 设置点击查看更多
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.start_top_billboard_detail_area);
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
    }

    /**
     * 展示数据
     * @param type 榜单类型
     */
    private void requestSongInfoList(int type) {
        DataCentral.requestTopSongList(type, 20, new DataCentral.ResponseSongListListener() {
            @Override
            public void responseSongList(final List<Song> songList) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainTopSongListFragment.this.songList.clear();
                        MainTopSongListFragment.this.songList.addAll(songList);
                        topSongListAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    /**
     * 用户点击播放
     * @param position 索引
     */
    private void preparePlay(final int position) {
        DataCentral.requestTopSongToPlay(type, position);
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
}
