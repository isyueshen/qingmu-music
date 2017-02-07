package com.luna1970.qingmumusic.Gson;

import android.os.Parcel;
import android.os.Parcelable;

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
public class Song implements Parcelable {
    @SerializedName("id")
    public int id;
    @SerializedName("artist_id")
    public int artistId;

    public String country;
    public String language;

    @SerializedName("publishtime")
    public String publishTime;


    @SerializedName("album_no")
    public int albumNo;

    @SerializedName("pic_big")
    public String songCoverPath;


    @SerializedName("file_duration")
    public int duration;

    @SerializedName("lrclink")
    public String lrcPath;

    @SerializedName("song_id")
    public int songId;

    public String title;

    public String author;
    @SerializedName("total_listen_nums")
    public int listeneNum;

    @SerializedName("album_id")
    public int AlbumId;

    @SerializedName("album_title")
    public String albumTitle;

    @SerializedName("has_mv")
    public int hasMv;

    @SerializedName("style")
    public String style;

    @SerializedName("file_link")
    public String FileLink;

    public int playListId;

    public Song() {
    }

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
        playListId = in.readInt();
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
        dest.writeInt(playListId);
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
