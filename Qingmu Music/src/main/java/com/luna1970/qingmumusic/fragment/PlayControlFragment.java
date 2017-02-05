package com.luna1970.qingmumusic.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.activity.MusicPlayActivity;
import com.luna1970.qingmumusic.util.PlayController;
import com.luna1970.qingmumusic.widget.PlayListDialog;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.luna1970.qingmumusic.application.MusicApplication.playState;

/**
 * Created by Yue on 1/31/2017.
 *
 */

public class PlayControlFragment extends Fragment {
    private static final String TAG = "PlayControlFragment";
    private ImageView playOrPause;
    private ImageView playList;
    private ImageView playNext;
    private TextView musicTitleTV;
    private TextView musicArtistTV;
    private CircleImageView miniAlbumPic;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver broadcastReceiver;
    private ProgressBar progressBar;
    private Intent intent;
    private LinearLayout linearLayout;
    private IntentFilter intentFilter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_control, container, false);
        initView(view);
        setData();
        setListener();
        return view;
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        miniAlbumPic = (CircleImageView) view.findViewById(R.id.miniAlbumPic);
        playOrPause = (ImageView) view.findViewById(R.id.playOrPause);
        playList = (ImageView) view.findViewById(R.id.play_list);
        playNext = (ImageView) view.findViewById(R.id.playNext);
        musicTitleTV = (TextView) view.findViewById(R.id.music_title_tv);
        musicArtistTV = (TextView) view.findViewById(R.id.musicArtistTV);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        linearLayout = (LinearLayout) view.findViewById(R.id.play_control_bar);
    }

    /**
     * 设置控件信息
     */
    private void setData() {
        if (playState.getListSize() != 0) {
            Song song = playState.getSong();
            Glide.with(getActivity()).load(song.songCoverPath).into(miniAlbumPic);
            musicTitleTV.setText(song.title);
            musicArtistTV.setText(song.author);
            if (playState.isPlaying()) {
                playOrPause.setImageResource(R.drawable.pause);
            }
        }
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        // 打开播放列表dialog
        playList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
                int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                int current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                Log.i(TAG, "show: " + max + " " + current);
                PlayListDialog playListDialog = new PlayListDialog(getActivity());
                playListDialog.show();

            }
        });
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        // Set play or pause button OnclickListener
        playOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.setAction(PlayController.ACTION_PLAY_OR_PAUSE);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
        playNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.setAction(PlayController.ACTION_PLAY_NEXT);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MusicPlayActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.start_activity_translate_in_bottom2top, R.anim.none);
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
        intentFilter = new IntentFilter();
        intentFilter.addAction(PlayController.STATE_SERVICE_PLAYING);
        intentFilter.addAction(PlayController.STATE_SERVICE_PAUSE);
        intentFilter.addAction(PlayController.STATE_SERVICE_PLAY_CONTINUE);
        intentFilter.addAction(PlayController.STATE_SERVICE_UPDATE_SEEK_BAR_PROGRESS);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "onReceive: " + intent.getAction());
                switch (intent.getAction()) {
                    case PlayController.STATE_SERVICE_PLAYING:
                        setData();
                        break;
                    case PlayController.STATE_SERVICE_PLAY_CONTINUE:
                        playOrPause.setImageResource(R.drawable.pause);
                        break;
                    case PlayController.STATE_SERVICE_PAUSE:
                        playOrPause.setImageResource(R.drawable.play);
                        break;
                    case PlayController.STATE_SERVICE_UPDATE_SEEK_BAR_PROGRESS:
                        int currentPosition = intent.getIntExtra(PlayController.STATE_SERVICE_UPDATE_SEEK_BAR_PROGRESS, 0);
                        progressBar.setProgress(currentPosition/10/playState.getDuration());
                        break;
                }
            }
        };
    }

    @Override
    public void onStart() {
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
        setData();
        super.onStart();
    }


    @Override
    public void onStop() {
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
