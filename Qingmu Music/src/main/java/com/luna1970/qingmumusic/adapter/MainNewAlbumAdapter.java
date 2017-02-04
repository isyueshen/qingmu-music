package com.luna1970.qingmumusic.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.luna1970.qingmumusic.Gson.Album;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.activity.AlbumDetailActivity;
import com.luna1970.qingmumusic.util.UriUtils;

import java.util.List;

/**
 * Created by Yue on 1/27/2017.
 *
 */

public class MainNewAlbumAdapter extends RecyclerView.Adapter<MainNewAlbumAdapter.MViewHolder> {
    private static final String TAG = "MainNewAlbumAdapter";
    private List<Album> albums;
    private final int width;
    private Context mContext;

    public MainNewAlbumAdapter(List<Album> albums) {
        width = Resources.getSystem().getDisplayMetrics().widthPixels;
        this.albums = albums;
    }

    public static class MViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView albumIv;


        public MViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            albumIv = (ImageView) view.findViewById(R.id.album_iv);
        }
    }



    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_album_item, parent, false);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        layoutParams.width = (int) (width * 0.8);
//        view.setLayoutParams(layoutParams);
        if (mContext == null) {
            mContext = parent.getContext();
        }
        return new MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MViewHolder holder, int position) {
        final Album album = albums.get(position);
        if (position==0) {
            holder.itemView.setPadding(25, 0, 0, 0);
            Log.d(TAG, "onBindViewHolder: " + position);
        } else if (position == getItemCount() - 1) {
            holder.itemView.setPadding(0, 0, 25, 0);
        } else {
            holder.itemView.setPadding(0, 0, 0, 0);
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(mContext, AlbumDetailActivity.class);
//                intent.putExtra("album", album);
//                mContext.startActivity(intent);
                AlbumDetailActivity.startCustomActivity((Activity) mContext, album, v);
            }
        });
        String path = UriUtils.getCustomImageSize(album.albumPicPath, 700);
        Glide.with(mContext).load(path).into(holder.albumIv);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    @Override
    public int getItemCount() {
        return albums.size();
    }

}
