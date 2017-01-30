package com.luna1970.qingmumusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.widget.PlayListDialog;

/**
 * Created by Yue on 1/31/2017.
 *
 */

public class PlayControlFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_control, container, false);
        initView(view, inflater);
        return view;
    }

    private void initView(View view, LayoutInflater layoutInflater) {
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.playOrPause);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayListDialog playListDialog = new PlayListDialog(getActivity());
                playListDialog.getWindow().setWindowAnimations(R.style.dialogAnimation);
                playListDialog.show();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
