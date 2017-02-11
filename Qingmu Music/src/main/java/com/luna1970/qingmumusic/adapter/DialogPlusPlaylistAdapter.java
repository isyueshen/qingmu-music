package com.luna1970.qingmumusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.entity.Playlist;
import com.luna1970.qingmumusic.util.ToastUtils;

import java.util.List;

/**
 * Created by Yue on 2/11/2017.
 *
 */

public class DialogPlusPlaylistAdapter extends BaseAdapter{
    private Context context;
    private List<Playlist> playlistData;

    public DialogPlusPlaylistAdapter(List<Playlist> playlistData) {
        if (playlistData == null) {
            throw new IllegalArgumentException("List can not be none!");
        }
        this.playlistData = playlistData;
    }

    @Override
    public int getCount() {
        return playlistData.size();
    }

    @Override
    public Playlist getItem(int position) {
        return playlistData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (context == null) {
            context = parent.getContext();
        }
        ViewHolder viewHolder;
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_play_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.listCover = (ImageView) view.findViewById(R.id.list_cover);
            viewHolder.listName = (TextView) view.findViewById(R.id.list_name);
            viewHolder.view = view;
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Playlist playlist = playlistData.get(position);
        viewHolder.listName.setText("122fd");
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show(position + "");
            }
        });
        Glide.with(context).load(playlist.coverPath).into(viewHolder.listCover);
        return view;
    }

    public class ViewHolder {
        View view;
        ImageView listCover;
        TextView listName;
    }
}
