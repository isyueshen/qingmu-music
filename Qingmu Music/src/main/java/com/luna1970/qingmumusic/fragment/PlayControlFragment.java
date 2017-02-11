package com.luna1970.qingmumusic.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.activity.MusicPlayActivity;
import com.luna1970.qingmumusic.util.GlobalConst;
import com.luna1970.qingmumusic.widget.PlayListDialog;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.luna1970.qingmumusic.application.MusicApplication.playState;

/**
 * Created by Yue on 1/31/2017.
 *
 */

public class PlayControlFragment extends Fragment {
    private static final String TAG = "PlayControlFragment";
    private ImageButton playOrPause;
    private ImageButton playList;
    private ImageButton playNext;
    private TextView musicTitleTV;
    private TextView musicArtistTV;
    private CircleImageView miniAlbumPic;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver broadcastReceiver;
    private ProgressBar progressBar;
    private Intent intent;
    private LinearLayout linearLayout;
    private IntentFilter intentFilter;
    private PlayListDialog playListDialog;

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
        playOrPause = (ImageButton) view.findViewById(R.id.playOrPause);
        playList = (ImageButton) view.findViewById(R.id.play_list);
        playNext = (ImageButton) view.findViewById(R.id.playNext);
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
            if (song == null) {
                return;
            }
            Glide.with(getActivity()).load(song.songCoverPath).into(miniAlbumPic);
            musicTitleTV.setText(song.title);
            musicArtistTV.setText(song.author);
            if (playState.isPlaying()) {
                playOrPause.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.pause));
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
                playListDialog = new PlayListDialog(getActivity());
                playListDialog.show();

            }
        });
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        // Set play or pause button OnclickListener
        playOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.setAction(GlobalConst.ACTION_PLAY_OR_PAUSE);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
        playNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.setAction(GlobalConst.ACTION_PLAY_NEXT);
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
        intentFilter.addAction(GlobalConst.STATE_SERVICE_PLAYING);
        intentFilter.addAction(GlobalConst.STATE_SERVICE_PAUSE);
        intentFilter.addAction(GlobalConst.STATE_SERVICE_PLAY_CONTINUE);
        intentFilter.addAction(GlobalConst.STATE_SERVICE_UPDATE_SEEK_BAR_PROGRESS);
        intentFilter.addAction(GlobalConst.ACTION_PLAY_LIST_CLEAR);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                Log.i(TAG, "onReceive: " + intent.getAction());
                switch (intent.getAction()) {
                    case GlobalConst.STATE_SERVICE_PLAYING:
                        setData();
                        break;
                    case GlobalConst.STATE_SERVICE_PLAY_CONTINUE:
                        playOrPause.setImageResource(R.drawable.pause);
                        break;
                    case GlobalConst.STATE_SERVICE_PAUSE:
                        playOrPause.setImageResource(R.drawable.play);
                        break;
                    case GlobalConst.STATE_SERVICE_UPDATE_SEEK_BAR_PROGRESS:
                        int currentPosition = intent.getIntExtra(GlobalConst.STATE_SERVICE_UPDATE_SEEK_BAR_PROGRESS, 0);
                        progressBar.setProgress(currentPosition/10/playState.getDuration());
                        break;
                    case GlobalConst.ACTION_PLAY_LIST_CLEAR:
                        playListDialog.dismiss();
                        getActivity().getSupportFragmentManager().beginTransaction().remove(getActivity().getSupportFragmentManager().findFragmentByTag(GlobalConst.PLAY_CONTROL_BAR_FRAGMENT_TAG)).commit();
                        break;
                }
            }
        };
    }

    @Override
    public void onStart() {
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
        super.onStart();
    }

    @Override
    public void onResume() {
        setData();
        super.onResume();
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
