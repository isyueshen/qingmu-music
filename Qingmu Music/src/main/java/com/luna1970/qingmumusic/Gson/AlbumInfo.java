package com.luna1970.qingmumusic.Gson;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Yue on 1/27/2017.
 *
 */

public class AlbumInfo implements Parcelable{
    private int id;
    @SerializedName("album_id")
    private int AlbumId;
    private String author;
    private String title;
    @SerializedName("publishcompany")
    private String publishCompany;
    private String country;
    private String language;
    @SerializedName("songs_total")
    private int totalCount;
    private String info;
    private String style;
    @SerializedName("publishtime")
    private String publishTime;
    @SerializedName("artist_id")
    public int artistId;
    @SerializedName("pic_s1000")
    public String albumPicPath;
    @SerializedName("listen_num")
    public int listenNum;

    @SerializedName("songlist")
    public List<Song> songList;

    protected AlbumInfo(Parcel in) {
        id = in.readInt();
        AlbumId = in.readInt();
        author = in.readString();
        title = in.readString();
        publishCompany = in.readString();
        country = in.readString();
        language = in.readString();
        totalCount = in.readInt();
        info = in.readString();
        style = in.readString();
        publishTime = in.readString();
        artistId = in.readInt();
        albumPicPath = in.readString();
        listenNum = in.readInt();
        songList = in.createTypedArrayList(Song.CREATOR);
    }

    public static final Creator<AlbumInfo> CREATOR = new Creator<AlbumInfo>() {
        @Override
        public AlbumInfo createFromParcel(Parcel in) {
            return new AlbumInfo(in);
        }

        @Override
        public AlbumInfo[] newArray(int size) {
            return new AlbumInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(AlbumId);
        dest.writeString(author);
        dest.writeString(title);
        dest.writeString(publishCompany);
        dest.writeString(country);
        dest.writeString(language);
        dest.writeInt(totalCount);
        dest.writeString(info);
        dest.writeString(style);
        dest.writeString(publishTime);
        dest.writeInt(artistId);
        dest.writeString(albumPicPath);
        dest.writeInt(listenNum);
        dest.writeTypedList(songList);
    }

    @Override
    public String toString() {
        return "AlbumInfo{" +
                "id=" + id +
                ", AlbumId=" + AlbumId +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", publishCompany='" + publishCompany + '\'' +
                ", country='" + country + '\'' +
                ", language='" + language + '\'' +
                ", totalCount=" + totalCount +
                ", info='" + info + '\'' +
                ", style='" + style + '\'' +
                ", publishTime='" + publishTime + '\'' +
                ", artistId=" + artistId +
                ", albumPicPath='" + albumPicPath + '\'' +
                ", listenNum=" + listenNum +
                ", songList=" + songList +
                '}';
    }
}
