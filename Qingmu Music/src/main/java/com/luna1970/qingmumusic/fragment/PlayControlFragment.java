package com.luna1970.qingmumusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.widget.PlayListDialog;

/**
 * Created by Yue on 1/31/2017.
 *
 */

public class PlayControlFragment extends Fragment {

    private ImageButton playOrPause;
    private ImageButton playList;
    private ImageButton playNext;
    private TextView musicTitleTV;
    private TextView musicArtistTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_control, container, false);
        initView(view, inflater);
        setListener();
        return view;
    }

    private void initView(View view, LayoutInflater layoutInflater) {
        playOrPause = (ImageButton) view.findViewById(R.id.playOrPause);
        playList = (ImageButton) view.findViewById(R.id.play_list);
        playNext = (ImageButton) view.findViewById(R.id.playNext);
        musicTitleTV = (TextView) view.findViewById(R.id.music_title_tv);
        musicArtistTv = (TextView) view.findViewById(R.id.musicArtistTV);
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
    }
}
