package com.luna1970.qingmumusic.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.R;

import java.util.List;

/**
 * Created by Yue on 2/9/2017.
 *
 */

public class SearchResultListAdapter extends RecyclerView.Adapter<SearchResultListAdapter.ViewHolder> {
    private static final String TAG = "SearchResultListAdapter";
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
    private MenuOnClickListener menuOnClickListener;
    private ItemOnClickListener itemOnClickListener;


    public SearchResultListAdapter(List<Song> songList) {
        // 非法传值
        if (songList == null) {
            throw new IllegalArgumentException("SongList conn't be null!");
        }
        this.songList = songList;
    }

    public void setMenuOnClickListener(MenuOnClickListener menuOnClickListener) {
        this.menuOnClickListener = menuOnClickListener;
    }

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle;
        TextView songAlbum;
        ImageButton menuBtn;
        LinearLayout linearLayout;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            // 初始化视图控件
            view = itemView;
            songTitle = (TextView) itemView.findViewById(R.id.song_title_tv);
            songAlbum = (TextView) itemView.findViewById(R.id.song_album_tv);
            menuBtn = (ImageButton) itemView.findViewById(R.id.menu_view);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.click_area_ll);
        }
    }

    @Override
    public SearchResultListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 生成视图
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_list_item, parent, false);
        if (mContext == null) {
            mContext = parent.getContext();
        }
        return new SearchResultListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchResultListAdapter.ViewHolder holder, final int position) {
        // 获得当前视图对应的song object
        final Song song = songList.get(position);
        // 设置标题
        String startFlag = "<em>";
        String endFlag = "</em>";

        String title = song.title;
        SpannableStringBuilder titleSSB = new SpannableStringBuilder(title);
        Log.d(TAG, "onBindViewHolder: " + titleSSB + " --> " + position);
        if (TextUtils.isEmpty(song.title)) {
            titleSSB.clear();
        } else {
            titleSSB.setSpan(new ForegroundColorSpan(Color.BLACK), 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (title.contains(startFlag)) {
                int start = title.indexOf(startFlag);
                int end = title.indexOf(endFlag);
                titleSSB.setSpan(new ForegroundColorSpan(0xFF0F9D58), start + 4, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                titleSSB.replace(start, start + 4, "");
                titleSSB.replace(end-4, end+1, "");
            }
        }

        String author = " - " + song.author;
        SpannableStringBuilder authorSSB = new SpannableStringBuilder(author);
        Log.d(TAG, "onBindViewHolder: " + authorSSB + " --> " + position);
        if (TextUtils.isEmpty(song.author)) {
            authorSSB.clear();
        } else {
            authorSSB.setSpan(new AbsoluteSizeSpan(12, true), 0, author.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            authorSSB.setSpan(new ForegroundColorSpan(0xFFAAAAAA), 0, author.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (author.contains(startFlag)) {
                int start = author.indexOf(startFlag);
                int end = author.indexOf(endFlag);
                authorSSB.setSpan(new ForegroundColorSpan(0xFF0F9D58), start + 4, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                authorSSB.replace(start, start + 4, "");
                authorSSB.replace(end-4, end+1, "");
            }
        }
        holder.songTitle.setText(titleSSB.append(authorSSB));

        String album = "专辑: " + song.albumTitle;
        SpannableStringBuilder albumSSB = new SpannableStringBuilder(album);
        Log.d(TAG, "onBindViewHolder: " + albumSSB + " --> " + position);
        if (TextUtils.isEmpty(song.albumTitle)) {
            holder.songAlbum.setVisibility(View.GONE);
            albumSSB.clear();
        } else {
            holder.songAlbum.setVisibility(View.VISIBLE);
            albumSSB.setSpan(new ForegroundColorSpan(0xFFAAAAAA), 0, album.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (album.contains(startFlag)) {
                int start = album.indexOf(startFlag);
                int end = album.indexOf(endFlag);
                albumSSB.setSpan(new ForegroundColorSpan(0xFF0F9D58), start + 4, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                albumSSB.replace(start, start + 4, "");
                albumSSB.replace(end-4, end+1, "");
            }
        }
        holder.songAlbum.setText(albumSSB);

        holder.menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuOnClickListener != null) {
                    menuOnClickListener.onClick(position);
                }
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemOnClickListener != null) {
                    itemOnClickListener.onClick(position);
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

    public interface MenuOnClickListener {
        void onClick(int position);
    }

    public interface ItemOnClickListener {
        void onClick(int position);
    }
}
