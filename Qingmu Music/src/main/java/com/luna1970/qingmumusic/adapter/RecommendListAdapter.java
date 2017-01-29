package com.luna1970.qingmumusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.listener.CustomRecyclerItemOnClickListener;
import com.luna1970.qingmumusic.util.UriUtils;

import java.util.List;

/**
 * Created by Yue on 1/29/2017.
 *
 */

public class RecommendListAdapter extends RecyclerView.Adapter<RecommendListAdapter.MViewHolder> {
    private List<Song> songList;
    private Context mContext;
    private CustomRecyclerItemOnClickListener customRecyclerItemOnClickListener;

    public RecommendListAdapter(List<Song> songList, CustomRecyclerItemOnClickListener customRecyclerItemOnClickListener) {
        if (songList == null) {
            throw new IllegalArgumentException("SongList conn't be null!");
        }
        this.songList = songList;
        this.customRecyclerItemOnClickListener = customRecyclerItemOnClickListener;
    }

    public static class MViewHolder extends RecyclerView.ViewHolder {
        ImageView songCoverIv;
        TextView authorTv;
        TextView titleTv;
        View view;

        public MViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            songCoverIv = (ImageView) itemView.findViewById(R.id.song_cover_iv);
            authorTv = (TextView) itemView.findViewById(R.id.author_tv);
            titleTv = (TextView) itemView.findViewById(R.id.title_tv);
        }
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_list_item, parent, false);
        if (mContext == null) {
            mContext = parent.getContext();
        }
        return new MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MViewHolder holder, int position) {
        final Song song = songList.get(position);
        Glide.with(mContext).load(UriUtils.getCustomImageSize(song.songCoverPath, 300)).into(holder.songCoverIv);
        holder.titleTv.setText(song.title);
        holder.authorTv.setText(song.author);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customRecyclerItemOnClickListener.onClick(song.songId);
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
