package com.luna1970.qingmumusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.entity.Music;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Yue on 1/9/2017.
 *
 */

public class MusicListAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Music> musics;

    public MusicListAdapter(Context context, int layout, List<Music> musics) {
        this.context = context;
        this.layout = layout;
        this.musics = musics;
    }

    @Override
    public int getCount() {
        return musics.size();
    }

    @Override
    public Music getItem(int position) {
        return musics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view;
        if (convertView==null) {
            view = LayoutInflater.from(context).inflate(R.layout.music_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.musicTitleTV = (TextView) view.findViewById(R.id.music_title_tv);
            viewHolder.musicAlbumTV = (TextView) view.findViewById(R.id.musicAlbumTV);
            viewHolder.musicInfoTV = (TextView) view.findViewById(R.id.musicInfoTV);
            viewHolder.musicIndexTV = (TextView) view.findViewById(R.id.musicIndexTV);
            viewHolder.playTrumpet = (ImageView) view.findViewById(R.id.playingTrumpet);
            view.setTag(R.id.playingTrumpet, viewHolder.playTrumpet);
            view.setTag(R.id.musicIndexTV, viewHolder.musicIndexTV);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Music music = getItem(position);
        viewHolder.musicTitleTV.setText(music.getTitle());
//        viewHolder.musicAlbumTV.setText(music.getAlbum());

        viewHolder.musicAlbumTV.setText("  " + new SimpleDateFormat("m:ss").format(new Date(music.getDuration())));
        viewHolder.musicInfoTV.setText(new StringBuilder(music.getArtist()).append(" - ").append(music.getTitle()));
        viewHolder.musicIndexTV.setText(position + 1 + "");
//        if (position == MusicApplication.currentPosition) {
//            viewHolder.playTrumpet.setVisibility(View.VISIBLE);
//            viewHolder.musicIndexTV.setVisibility(View.GONE);
//        } else {
//            viewHolder.playTrumpet.setVisibility(View.GONE);
//            viewHolder.musicIndexTV.setVisibility(View.VISIBLE);
//        }

        return view;
    }


    private class ViewHolder {
        TextView musicTitleTV;
        TextView musicAlbumTV;
        TextView musicInfoTV;
        TextView musicIndexTV;
        ImageView playTrumpet;
    }
}
