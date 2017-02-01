package com.luna1970.qingmumusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

public class NewSongListAdapter extends RecyclerView.Adapter<NewSongListAdapter.MViewHolder> {
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

    public NewSongListAdapter(List<Song> songList, CustomRecyclerItemOnClickListener customRecyclerItemOnClickListener) {
        // 非法传值
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
        TextView albumTitleTv;
        TextView styleTv;
        ImageView menuBtn;
        FrameLayout menuArea;
        View view;

        public MViewHolder(View itemView) {
            super(itemView);
            // 初始化视图控件
            view = itemView;
            menuBtn = (ImageView) itemView.findViewById(R.id.menu_btn);
            songCoverIv = (ImageView) itemView.findViewById(R.id.song_cover_iv);
            authorTv = (TextView) itemView.findViewById(R.id.author_tv);
            titleTv = (TextView) itemView.findViewById(R.id.title_tv);
            albumTitleTv = (TextView) itemView.findViewById(R.id.album_title_tv);
            styleTv = (TextView) itemView.findViewById(R.id.style_tv);
            menuArea = (FrameLayout) itemView.findViewById(R.id.menu_are);
        }
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 生成视图
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_focus_item, parent, false);
        if (mContext == null) {
            mContext = parent.getContext();
        }
        return new MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MViewHolder holder, int position) {
        // 设置滚动列表第一个为空
        if (position == 0) {
            holder.itemView.setVisibility(View.INVISIBLE);
            return;
        } else {
            // 防止重用视图时视图不可见
            holder.itemView.setVisibility(View.VISIBLE);
        }
        // 获得当前视图对应的song object
        final Song song = songList.get(position-1);
        // 加载图片
        Glide.with(mContext).load(UriUtils.getCustomImageSize(song.songCoverPath, 500)).into(holder.songCoverIv);
        // 设置标题
        holder.titleTv.setText(song.title);
        // 设置歌手名
        holder.authorTv.setText(song.author);
        // 设置视图点击事件
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customRecyclerItemOnClickListener.onClick(holder.getAdapterPosition());
            }
        });
        // 设置menu图标偏移, 获得更大的点击区域, 优化视觉效果
        holder.menuBtn.setTranslationX(27);
        holder.menuBtn.setTranslationY(-25);
        holder.menuBtn.setScaleX(0.5f);
        holder.menuBtn.setScaleY(0.5f);
        // menu 点击事件 PopMenu
        holder.menuArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customRecyclerItemOnClickListener.onPopMenuOnClick(v);
            }
        });
        // 设置专辑标题
        holder.albumTitleTv.setText("专辑: " + song.albumTitle);
        // 设置歌曲曲风
        holder.styleTv.setText(song.style);
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
