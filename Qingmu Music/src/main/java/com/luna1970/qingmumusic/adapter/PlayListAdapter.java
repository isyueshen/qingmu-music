package com.luna1970.qingmumusic.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.listener.PlayListDialogOnClickListener;
import com.luna1970.qingmumusic.listener.PlayListDialogOnDeleteListener;

import java.util.List;
import static com.luna1970.qingmumusic.application.MusicApplication.playState;
/**
 * Created by Yue on 1/29/2017.
 *
 */

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.MViewHolder> {
    private static final String TAG = "PlayListAdapter";
    /**
     * 数据
     */
    private List<Song> songList;
    /**
     * context
     */
    private Context mContext;
    /**
     * 监听接口
     */
    private PlayListDialogOnDeleteListener playListDialogOnDeleteListener;
    private PlayListDialogOnClickListener playListDialogOnClickListener;


    public PlayListAdapter(List<Song> songList) {
        // 非法传值
        if (songList == null) {
            throw new IllegalArgumentException("SongList conn't be null!");
        }
        this.songList = songList;
    }

    public void setPlayListDialogOnDeleteListener(PlayListDialogOnDeleteListener playListDialogOnDeleteListener) {
        this.playListDialogOnDeleteListener = playListDialogOnDeleteListener;
    }

    public void setPlayListDialogOnClickListener(PlayListDialogOnClickListener playListDialogOnClickListener) {
        this.playListDialogOnClickListener = playListDialogOnClickListener;
    }

    public static class MViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle;
        TextView songAlbum;
        ImageView deleteIv;
        LinearLayout linearLayout;
        View view;

        public MViewHolder(View itemView) {
            super(itemView);
            // 初始化视图控件
            view = itemView;
            songTitle = (TextView) itemView.findViewById(R.id.song_title_tv);
            songAlbum = (TextView) itemView.findViewById(R.id.song_album_tv);
            deleteIv = (ImageView) itemView.findViewById(R.id.delete_iv);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.click_area_ll);
        }
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 生成视图
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_play_list_item, parent, false);
        if (mContext == null) {
            mContext = parent.getContext();
        }
        return new MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MViewHolder holder, final int position) {
        // 获得当前视图对应的song object
        final Song song = songList.get(position);
        // 设置标题
        String title = song.title;
        String author = song.author;
        String str = title + " - " + author;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(12, true), title.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        Log.i(TAG, "onBindViewHolder: " + spannableStringBuilder.toString());
        if (playState.getCurrentPosition() == position) {
            spannableStringBuilder.setSpan(new ForegroundColorSpan(0xFF0F9D58), title.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.songTitle.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            holder.songAlbum.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        } else {
            spannableStringBuilder.setSpan(new ForegroundColorSpan(0xFFAAAAAA), title.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.songTitle.setTextColor(Color.BLACK);
            holder.songAlbum.setTextColor(Color.DKGRAY);
        }
        holder.songTitle.setText(spannableStringBuilder);
        holder.songAlbum.setText("专辑: " + song.albumTitle);

        holder.deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playListDialogOnDeleteListener != null) {
                    playListDialogOnDeleteListener.onDelete(position);
                }
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playListDialogOnClickListener != null) {
                    playListDialogOnClickListener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }
}
