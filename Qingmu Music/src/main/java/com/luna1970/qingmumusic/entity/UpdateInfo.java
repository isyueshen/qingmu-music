package com.luna1970.qingmumusic.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yue on 2/10/2017.
 *
 */

public class UpdateInfo implements Parcelable {
    private int versionCode;
    private String versionName;
    private String updateUrl;
    private String tip;

    public int getVersionCode() {

        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public UpdateInfo() {
    }

    public UpdateInfo(int versionCode, String versionName, String updateUrl, String tip) {
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.updateUrl = updateUrl;
        this.tip = tip;
    }

    protected UpdateInfo(Parcel in) {
        this.versionCode = in.readInt();
        this.versionName = in.readString();
        this.updateUrl = in.readString();
        this.tip = in.readString();
    }

    public static final Creator<UpdateInfo> CREATOR = new Creator<UpdateInfo>() {
        @Override
        public UpdateInfo createFromParcel(Parcel source) {
            return new UpdateInfo(source);
        }

        @Override
        public UpdateInfo[] newArray(int size) {
            return new UpdateInfo[size];
        }
    };

    @Override
    public String toString() {
        return "UpdateInfo{" +
                "versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", updateUrl='" + updateUrl + '\'' +
                ", tip='" + tip + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(versionCode);
        dest.writeString(versionName);
        dest.writeString(updateUrl);
        dest.writeString(tip);
    }
}