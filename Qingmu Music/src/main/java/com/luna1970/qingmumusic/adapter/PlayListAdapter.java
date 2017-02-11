package com.luna1970.qingmumusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.entity.Playlist;

import java.util.List;

/**
 * Created by Yue on 2/11/2017.
 *
 */

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {
    private Context context;
    private List<Playlist> playlistData;

    public PlayListAdapter(List<Playlist> playlistData) {
        if (playlistData == null) {
            throw new IllegalArgumentException("List can not be none!");
        }
        this.playlistData = playlistData;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView listCover;
        TextView listName;

        public ViewHolder(View itemView) {
            super(itemView);
            // 初始化视图控件
            view = itemView;
            listCover = (ImageView) itemView.findViewById(R.id.list_cover);
            listName = (TextView) itemView.findViewById(R.id.list_name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_main_play_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Playlist playlist = playlistData.get(position);
        holder.listName.setText(playlist.name);
        Glide.with(context).load(playlist.coverPath).into(holder.listCover);
    }

    @Override
    public int getItemCount() {
        return playlistData.size();
    }
}
