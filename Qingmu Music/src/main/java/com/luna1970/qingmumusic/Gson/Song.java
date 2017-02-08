package com.luna1970.qingmumusic.Gson;

import com.google.gson.annotations.SerializedName;
import com.luna1970.qingmumusic.dao.DBFlowDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Yue on 1/27/2017.
 *
 */
@ModelContainer
@Table(databaseName = DBFlowDatabase.NAME)
public class Song extends BaseModel{
    @SerializedName("id")
    @Column
    @PrimaryKey(autoincrement = true)
    public int id;
    @SerializedName("artist_id")
    @Column
    public int artistId;

    @Column
    public String country;
    @Column
    public String language;

    @SerializedName("publishtime")
    @Column
    public String publishTime;


    @SerializedName("album_no")
    @Column
    public int albumNo;

    @SerializedName("pic_big")
    @Column
    public String songCoverPath;


    @SerializedName("file_duration")
    @Column
    public int duration;

    @SerializedName("lrclink")
    @Column
    public String lrcPath;

    @SerializedName("song_id")
    @Column
    public int songId;

    @Column
    public String title;

    @Column
    public String author;
    @SerializedName("total_listen_nums")
    @Column
    public int listeneNum;

    @SerializedName("album_id")
    @Column
    public int AlbumId;

    @SerializedName("album_title")
    @Column
    public String albumTitle;

    @SerializedName("has_mv")
    @Column
    public int hasMv;

    @SerializedName("style")
    @Column
    public String style;

    @SerializedName("file_link")
    @Column
    public String FileLink;

    @Column
    public int playListId;

    public Song() {
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", artistId=" + artistId +
                ", country='" + country + '\'' +
                ", language='" + language + '\'' +
                ", publishTime='" + publishTime + '\'' +
                ", albumNo=" + albumNo +
                ", songCoverPath='" + songCoverPath + '\'' +
                ", duration=" + duration +
                ", lrcPath='" + lrcPath + '\'' +
                ", songId=" + songId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", listeneNum=" + listeneNum +
                ", AlbumId=" + AlbumId +
                ", albumTitle='" + albumTitle + '\'' +
                ", hasMv=" + hasMv +
                ", style='" + style + '\'' +
                ", FileLink='" + FileLink + '\'' +
                ", playListId=" + playListId +
                '}';
    }
}
