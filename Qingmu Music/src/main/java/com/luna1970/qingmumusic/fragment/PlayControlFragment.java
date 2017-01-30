package com.luna1970.qingmumusic.fragment;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.application.MusicApplication;
import com.luna1970.qingmumusic.util.GlobalMusicPlayControllerConst;
import com.luna1970.qingmumusic.widget.PlayListDialog;

import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Yue on 1/31/2017.
 *
 */

public class PlayControlFragment extends Fragment {
    private static final String TAG = "PlayControlFragment";
    private ImageView playOrPause;
    private ImageButton playList;
    private ImageButton playNext;
    private TextView musicTitleTV;
    private TextView musicArtistTV;
    private CircleImageView miniAlbumPic;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver broadcastReceiver;
    private ProgressBar progressBar;
    private Song song;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_control, container, false);
        initView(view, inflater);
        setData();
        setListener();
        return view;
    }

    // 初始化控件
    private void initView(View view, LayoutInflater layoutInflater) {
        miniAlbumPic = (CircleImageView) view.findViewById(R.id.miniAlbumPic);
        playOrPause = (ImageView) view.findViewById(R.id.playOrPause);
        playList = (ImageButton) view.findViewById(R.id.play_list);
        playNext = (ImageButton) view.findViewById(R.id.playNext);
        musicTitleTV = (TextView) view.findViewById(R.id.music_title_tv);
        musicArtistTV = (TextView) view.findViewById(R.id.musicArtistTV);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    }

    private void setData() {
        if (MusicApplication.playList.size() != 0) {
            song = MusicApplication.playList.get(MusicApplication.currentPosition);
            Glide.with(getActivity()).load(song.songCoverPath).into(miniAlbumPic);
            musicTitleTV.setText(song.title);
            musicArtistTV.setText(song.author);
        }
    }

    private void setListener() {
        playList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayListDialog playListDialog = new PlayListDialog(getActivity());
                playListDialog.show();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setBroadcastReceiver();
    }

    /**
     * 注册本地广播接收器
     */
    private void setBroadcastReceiver() {
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalMusicPlayControllerConst.ACTION_SERVICE_PLAYING);
        intentFilter.addAction(GlobalMusicPlayControllerConst.ACTION_SERVICE_PAUSE);
        intentFilter.addAction(GlobalMusicPlayControllerConst.ACTION_SERVICE_PLAY_CONTINUE);
        intentFilter.addAction(GlobalMusicPlayControllerConst.ACTION_SERVICE_UPDATE_SEEK_BAR_PROGRESS);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "onReceive: " + intent.getAction());
                switch (intent.getAction()) {
                    case GlobalMusicPlayControllerConst.ACTION_SERVICE_PLAYING:
                        Log.i(TAG, "position:  " + MusicApplication.currentPosition);
                        MusicApplication.isPlaying = true;
                        initMusicInfo();
                        break;
                    case GlobalMusicPlayControllerConst.ACTION_SERVICE_PLAY_CONTINUE:
                        playOrPause.setImageResource(R.drawable.pause);
                        MusicApplication.isPlaying = true;
                        break;
                    case GlobalMusicPlayControllerConst.ACTION_SERVICE_PAUSE:
                        playOrPause.setImageResource(R.drawable.play);
                        MusicApplication.isPlaying = false;
                        break;
                    case GlobalMusicPlayControllerConst.ACTION_SERVICE_UPDATE_SEEK_BAR_PROGRESS:
                        int currentPosition = intent.getIntExtra(GlobalMusicPlayControllerConst.ACTION_SERVICE_UPDATE_SEEK_BAR_PROGRESS, 0);
                        Log.i(TAG, "onReceive: " + currentPosition);
                        progressBar.setProgress(currentPosition/10/song.duration);
                        break;
                }
            }
        };
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }



    private void initMusicInfo() {
        if (MusicApplication.isPlaying) {
            playOrPause.setImageResource(R.drawable.pause);
        }

        // setting screen bottom bar music info
        song = MusicApplication.playList.get(MusicApplication.currentPosition);
        if (song != null) {
            musicTitleTV.setText(song.title);
            musicArtistTV.setText(song.author);
            Glide.with(getActivity()).load(song.songCoverPath).into(miniAlbumPic);
            Log.i(TAG, "initMusicInfo: " + song.duration);
        }

    }

}
