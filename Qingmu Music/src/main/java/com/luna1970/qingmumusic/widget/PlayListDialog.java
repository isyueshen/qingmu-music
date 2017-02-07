package com.luna1970.qingmumusic.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.adapter.PlayListAdapter;
import com.luna1970.qingmumusic.application.MusicApplication;
import com.luna1970.qingmumusic.listener.PlayListDialogOnClickListener;
import com.luna1970.qingmumusic.listener.PlayListDialogOnDeleteListener;
import com.luna1970.qingmumusic.util.GlobalConst;

import static com.luna1970.qingmumusic.application.MusicApplication.playState;

/**
 * Created by Yue on 1/31/2017.
 *
 */

public class PlayListDialog extends Dialog {
    private TextView playListTitleTv;
    private Button clearBtn;
    private RecyclerView recyclerView;
    private LocalBroadcastManager localBroadcastManager;

    public PlayListDialog(Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_play_list_dialog);
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        initView();
        setListeners();
        setRecyclerView();
    }

    private void initView() {
        playListTitleTv = (TextView) findViewById(R.id.play_list_title);
        playListTitleTv.setText("播放列表 (" + playState.getListSize() + ")");
        clearBtn = (Button) findViewById(R.id.clear_btn);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

    }

    private void setListeners() {
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext()).setTitle("操作提示")
                        .setMessage("确认清空列表吗?")
                        .setPositiveButton(R.string.userAction_confirm, new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                playState.clearSong();
                                Intent intent = new Intent();
                                intent.setAction(GlobalConst.ACTION_PLAY_LIST_CLEAR);
                                localBroadcastManager.sendBroadcast(intent);
                                intent = new Intent();
                                intent.setAction(GlobalConst.ACTION_PLAY_STOP);
                                localBroadcastManager.sendBroadcast(intent);
                            }
                        })
                        .setNegativeButton(R.string.userAction_cancel, null)
                        .show();
            }
        });
    }

    private void setRecyclerView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.scrollToPosition(playState.getCurrentPosition());
        final PlayListAdapter playListAdapter = new PlayListAdapter(playState.getPlayList());
        playListAdapter.setPlayListDialogOnClickListener(new PlayListDialogOnClickListener() {
            @Override
            public void onClick(int index) {
                playState.setCurrentPosition(index);
                playListAdapter.notifyItemRangeChanged(linearLayoutManager.findFirstVisibleItemPosition(), linearLayoutManager.findLastVisibleItemPosition());
                Intent intent = new Intent();
                intent.setAction(GlobalConst.ACTION_PLAY_SPECIFIC);
                intent.putExtra(GlobalConst.ACTION_PLAY_SPECIFIC, index);
                localBroadcastManager.sendBroadcast(intent);

            }
        });
        playListAdapter.setPlayListDialogOnDeleteListener(new PlayListDialogOnDeleteListener() {
            @Override
            public void onDelete(int index) {
//                Logger.d(index);
                playState.removeSongAt(index);
                playListAdapter.notifyItemRemoved(index);
                playListAdapter.notifyItemRangeChanged(index, playState.getListSize()+1-index);
                playListTitleTv.setText("播放列表 (" + playState.getListSize() + ")");
                if (playState.getCurrentPosition() == index) {
                    Intent intent = new Intent();
                    intent.setAction(GlobalConst.ACTION_PLAY_SPECIFIC);
                    intent.putExtra(GlobalConst.ACTION_PLAY_SPECIFIC, index);
                    localBroadcastManager.sendBroadcast(intent);
                }
            }
        });
        recyclerView.setAdapter(playListAdapter);
    }

    @Override
    public void show() {
        super.show();
        // 设置宽度全屏，要设置在show的后面
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (layoutParams != null) {
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            // 边距
            window.getDecorView().setPadding(0, 0, 0, 0);
            // 设置
            window.setAttributes(layoutParams);
        }
    }

}
