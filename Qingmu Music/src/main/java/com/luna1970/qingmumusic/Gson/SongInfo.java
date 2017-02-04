package com.luna1970.qingmumusic.Gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yue on 1/28/2017.
 *
 */

public class SongInfo {
    private int id;
    @SerializedName("song_id")
    private int SongId;
    @SerializedName("file_link")
    public String FileLink;

    @Override
    public String toString() {
        return "SongInfo{" +
                "id=" + id +
                ", SongId=" + SongId +
                ", FileLink='" + FileLink + '\'' +
                '}';
    }
}
