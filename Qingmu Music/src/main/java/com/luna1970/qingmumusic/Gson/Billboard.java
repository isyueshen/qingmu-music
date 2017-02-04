package com.luna1970.qingmumusic.Gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yue on 2/3/2017.
 *
 */

public class Billboard {
    @SerializedName("billboard_type")
    public String Type;

    @SerializedName("update_date")
    public String time;

    @SerializedName("billboard_songnum")
    public String totalNum;

    @SerializedName("havemore")
    public int haveMore;

    public String name;

    public String comment;

    @SerializedName("pic_s640")
    public String picBigSquare;

    @SerializedName("pic_s444")
    public String picBigRect;

    @SerializedName("pic_s260")
    public String picMiniSquare;

    @SerializedName("pic_s210")
    public String picMiniRect;

    @SerializedName("web_url")
    public String webContentUri;
}
