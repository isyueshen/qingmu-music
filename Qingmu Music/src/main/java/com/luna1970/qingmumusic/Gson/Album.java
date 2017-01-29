package com.luna1970.qingmumusic.Gson;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yue on 1/27/2017.
 *
 */

public class Album implements Parcelable{
    public int id;
    public String title;
    public String author;
    @SerializedName("artist_id")
    public int artistId;


    @SerializedName("album_id")
    public int albumId;
    @SerializedName("pic_radio")
    public String albumPicPath;

    @SerializedName("publishtime")
    public String publishTime;

    protected Album(Parcel in) {
        id = in.readInt();
        title = in.readString();
        author = in.readString();
        artistId = in.readInt();
        albumId = in.readInt();
        albumPicPath = in.readString();
        publishTime = in.readString();
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", artistId=" + artistId +
                ", albumId=" + albumId +
                ", albumPicPath='" + albumPicPath + '\'' +
                ", publishTime='" + publishTime + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeInt(artistId);
        dest.writeInt(albumId);
        dest.writeString(albumPicPath);
        dest.writeString(publishTime);
    }
}
