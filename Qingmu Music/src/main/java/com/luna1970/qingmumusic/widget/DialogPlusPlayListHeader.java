package com.luna1970.qingmumusic.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.dao.PlaylistDao;
import com.luna1970.qingmumusic.entity.Playlist;

/**
 * Created by Yue on 2/11/2017.
 *
 */

public class DialogPlusPlayListHeader extends LinearLayout {

    private EditText editText;

    public DialogPlusPlayListHeader(Context context) {
        this(context, null);
    }

    public DialogPlusPlayListHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialogPlusPlayListHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_playlist_header, this);
        LinearLayout addNewPlaylistLl = (LinearLayout) view.findViewById(R.id.addNewPlaylistLl);
        final LinearLayout addNewPlaylistEditLl = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.dialog_add_new_playlist_view, this, false);
        if (editText == null) {
            editText = (EditText) addNewPlaylistEditLl.findViewById(R.id.editText);
        }
        addNewPlaylistLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("新建歌单")
                        .setView(addNewPlaylistEditLl)
                        .setPositiveButton(R.string.userAction_confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Playlist playlist = new Playlist();
                                playlist.name = editText.getText().toString();
                                playlist.time = System.currentTimeMillis();
                                new PlaylistDao().add(playlist);
                            }
                        })
                        .setNegativeButton(R.string.userAction_cancel, null)
                        .show();

            }
        });
    }



}
