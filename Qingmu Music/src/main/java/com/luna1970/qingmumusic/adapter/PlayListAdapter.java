package com.luna1970.qingmumusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.listener.CustomRecyclerItemOnClickListener;

import java.util.List;

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
    private CustomRecyclerItemOnClickListener customRecyclerItemOnClickListener;

    public PlayListAdapter(List<Song> songList, CustomRecyclerItemOnClickListener customRecyclerItemOnClickListener) {
        // 非法传值
        if (songList == null) {
            throw new IllegalArgumentException("SongList conn't be null!");
        }
        this.songList = songList;
        this.customRecyclerItemOnClickListener = customRecyclerItemOnClickListener;
    }

    public static class MViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle;
        TextView songAlbum;
        View view;

        public MViewHolder(View itemView) {
            super(itemView);
            // 初始化视图控件
            view = itemView;
            songTitle = (TextView) itemView.findViewById(R.id.song_title_tv);
            songAlbum = (TextView) itemView.findViewById(R.id.song_album_tv);
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
    public void onBindViewHolder(final MViewHolder holder, int position) {
        // 获得当前视图对应的song object
        final Song song = songList.get(position);
        // 设置标题
        String title = song.title;
        String author = song.author;
        String str = title + " - "+ author;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(0xFFAAAAAA), title.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(12, true), title.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        Log.i(TAG, "onBindViewHolder: " + spannableStringBuilder.toString());
        holder.songTitle.setText(spannableStringBuilder);
        holder.songAlbum.setText("专辑: " + song.albumTitle);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customRecyclerItemOnClickListener.onClick(holder.getAdapterPosition());
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
