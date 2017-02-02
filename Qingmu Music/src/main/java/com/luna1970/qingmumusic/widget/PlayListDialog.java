package com.luna1970.qingmumusic.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.adapter.PlayListAdapter;
import com.luna1970.qingmumusic.listener.CustomRecyclerItemOnClickListener;
import com.luna1970.qingmumusic.util.PlayController;

import static com.luna1970.qingmumusic.application.MusicApplication.playState;

/**
 * Created by Yue on 1/31/2017.
 *
 */

public class PlayListDialog extends Dialog {
    private Context mContext;
    private TextView playListTitleTv;
    private Button clearBtn;
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private LocalBroadcastManager localBroadcastManager;

    public PlayListDialog(Context context) {
        super(context, R.style.MyDialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_play_list_dialog);
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        initView();
        setListeners();
    }

    private void initView() {
        playListTitleTv = (TextView) findViewById(R.id.play_list_title);
        playListTitleTv.setText("播放列表 (" + playState.getListSize() + ")");

        clearBtn = (Button) findViewById(R.id.clear_btn);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        setRecyclerView();

    }

    private void setRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        PlayListAdapter playListAdapter = new PlayListAdapter(playState.getPlayList(), new CustomRecyclerItemOnClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                intent.setAction(PlayController.ACTION_PLAY_SPECIFIC);
                intent.putExtra(PlayController.ACTION_PLAY_SPECIFIC, position);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
        recyclerView.setAdapter(playListAdapter);
    }

    private void setListeners() {

    }

    @Override
    public void show() {
        super.show();
        // 设置宽度全屏，要设置在show的后面
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (layoutParams != null) {
            layoutParams.gravity= Gravity.BOTTOM;
            layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height= WindowManager.LayoutParams.WRAP_CONTENT;
            // 边距
            window.getDecorView().setPadding(0, 0, 0, 0);
            // 设置
            window.setAttributes(layoutParams);
        }
    }

}
