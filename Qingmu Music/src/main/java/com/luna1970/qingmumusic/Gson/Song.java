package com.luna1970.qingmumusic.Gson;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yue on 1/27/2017.
 *
 */

public class Song implements Parcelable{
    public int id;
    @SerializedName("artist_id")
    public int artistId;
    public String language;

    @SerializedName("publishtime")
    public String publishTime;

    @SerializedName("album_no")
    public int albumNo;


    @SerializedName("pic_big")
    public String songCoverPath;

    @SerializedName("file_duration")
    public int duration;


    public String country;

    @SerializedName("lrclink")
    public String lrcPath;

    @SerializedName("song_id")
    public int songId;

    public String title;

    public String author;


    @SerializedName("album_id")
    public int AlbumId;

    @SerializedName("album_title")
    public String albumTitle;

    @SerializedName("has_mv")
    public int hasMv;

    protected Song(Parcel in) {
        id = in.readInt();
        artistId = in.readInt();
        language = in.readString();
        publishTime = in.readString();
        albumNo = in.readInt();
        songCoverPath = in.readString();
        duration = in.readInt();
        country = in.readString();
        lrcPath = in.readString();
        songId = in.readInt();
        title = in.readString();
        author = in.readString();
        AlbumId = in.readInt();
        albumTitle = in.readString();
        hasMv = in.readInt();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(artistId);
        dest.writeString(language);
        dest.writeString(publishTime);
        dest.writeInt(albumNo);
        dest.writeString(songCoverPath);
        dest.writeInt(duration);
        dest.writeString(country);
        dest.writeString(lrcPath);
        dest.writeInt(songId);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeInt(AlbumId);
        dest.writeString(albumTitle);
        dest.writeInt(hasMv);
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", artistId=" + artistId +
                ", language='" + language + '\'' +
                ", publishTime='" + publishTime + '\'' +
                ", albumNo=" + albumNo +
                ", songCoverPath='" + songCoverPath + '\'' +
                ", duration=" + duration +
                ", country='" + country + '\'' +
                ", lrcPath='" + lrcPath + '\'' +
                ", songId=" + songId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", AlbumId=" + AlbumId +
                ", albumTitle='" + albumTitle + '\'' +
                ", hasMv=" + hasMv +
                '}';
    }
}
