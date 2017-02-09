package com.luna1970.qingmumusic.Gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Yue on 2/9/2017.
 *
 */

public class QueryResult {
    public String query;

    @SerializedName("is_artist")
    public int isArtist;

    @SerializedName("is_album")
    public int isAlbum;

    @SerializedName("song_list")
    public List<Song> songList;

}
