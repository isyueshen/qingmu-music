package com.luna1970.qingmumusic.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.luna1970.qingmumusic.adapter.DialogPlusPlaylistAdapter;
import com.luna1970.qingmumusic.entity.Playlist;
import com.luna1970.qingmumusic.entity.Playlist$Table;
import com.luna1970.qingmumusic.widget.DialogPlusPlayListHeader;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.raizlabs.android.dbflow.sql.language.Select;

/**
 * Created by Yue on 2/11/2017.
 *
 */

public class DialogUtil {
    public static void addSongToPlayList(Context context) {
        TextView textView = new TextView(context);
        textView.setText("dfsadfsdafsda");
        DialogPlus dialogPlus = DialogPlus.newDialog(context)
                .setHeader(new DialogPlusPlayListHeader(context))
                .setAdapter(new DialogPlusPlaylistAdapter(new Select(Playlist$Table.NAME).from(Playlist.class).queryList()))
                .setContentHolder(new ListHolder())
                .setExpanded(true)
                .setGravity(Gravity.CENTER)
                .create();
        dialogPlus.show();
    }
}
