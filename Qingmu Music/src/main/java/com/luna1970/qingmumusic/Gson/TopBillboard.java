package com.luna1970.qingmumusic.Gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Yue on 2/3/2017.
 *
 */

public class TopBillboard {
    @SerializedName("song_list")
    public List<Song> songList;

    @SerializedName("billboard")
    public Billboard billboard;
}
