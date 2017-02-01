package com.luna1970.qingmumusic.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.listener.CustomRecyclerItemOnClickListener;

import java.util.List;

/**
 * Created by Yue on 1/27/2017.
 *
 */

public class AlbumDetailSongListAdapter extends RecyclerView.Adapter<AlbumDetailSongListAdapter.MViewHolder> {

    private static final String TAG = "AlbumDetailSongListAdap";
    private List<Song> songList;
    private Context mContext;
    private int screenX;
    private CustomRecyclerItemOnClickListener customRecyclerItemOnClickListener;

    public AlbumDetailSongListAdapter(List<Song> songList, CustomRecyclerItemOnClickListener customRecyclerItemOnClickListener) {
        this.songList = songList;
        screenX = Resources.getSystem().getDisplayMetrics().widthPixels;
        this.customRecyclerItemOnClickListener = customRecyclerItemOnClickListener;
    }

    public static class MViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView songCoverIv;
        TextView songTitleTv;

        public MViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            songCoverIv = (ImageView) itemView.findViewById(R.id.song_cover_iv);
            songTitleTv = (TextView) itemView.findViewById(R.id.song_title_tv);
        }
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_song_list_item, parent, false);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        layoutParams.width = screenX / 3 - 30;
        return new MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MViewHolder holder, int position) {
        final Song song = songList.get(position);
        holder.songTitleTv.setText(song.title);
        Glide.with(mContext).load(song.songCoverPath).into(holder.songCoverIv);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                customRecyclerItemOnClickListener.onClick(holder.getAdapterPosition());
            }
        });
        Log.d(TAG, "onBindViewHolder: " + song.songId);
    }


    @Override
    public int getItemCount() {
        return songList.size();
    }
}
