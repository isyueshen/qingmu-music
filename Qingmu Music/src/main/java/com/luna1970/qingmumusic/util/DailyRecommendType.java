package com.luna1970.qingmumusic.util;

import java.util.HashMap;

/**
 * Created by Yue on 1/29/2017.
 *
 */

public class DailyRecommendType {

    public static final HashMap<Integer, String> ALL_TYPE = new HashMap<>();
    public static final int TYPE_NEW_SONG = 1;
    public static final int TYPE_HOT_SONG = 2;
    public static final int TYPE_EUROPE_AMERICA = 21;
    public static final int TYPE_ORIGINAL = 0;

    public static final int TYPE_POP_MUSIC = 16;
    public static final int TYPE_CHINESE_SONG = 0;
    public static final int TYPE_CLASSICAL_SONG = 22;
    public static final int TYPE_NET_WORK_SONGS = 25;
    public static final int TYPE_FILM_SONGS = 24;
    public static final int TYPE_LOVE_SONGS = 23;
    public static final int TYPE_BILLBOARD = 0;

    public static final int TYPE_ROCK = 11;
    public static final int TYPE_JAZZ = 12;

    static {
        ALL_TYPE.put(TYPE_NEW_SONG,         "新歌榜");
        ALL_TYPE.put(TYPE_HOT_SONG,         "热歌榜");
        ALL_TYPE.put(TYPE_EUROPE_AMERICA,   "欧美金曲");
        ALL_TYPE.put(TYPE_ORIGINAL,         "原创榜");
        ALL_TYPE.put(TYPE_POP_MUSIC,        "流行");
        ALL_TYPE.put(TYPE_CHINESE_SONG,     "华语金曲");
        ALL_TYPE.put(TYPE_CLASSICAL_SONG,   "金典老歌");
        ALL_TYPE.put(TYPE_NET_WORK_SONGS,   "网络歌曲");
        ALL_TYPE.put(TYPE_FILM_SONGS,       "影视金曲");
        ALL_TYPE.put(TYPE_LOVE_SONGS,       "情歌对唱");
        ALL_TYPE.put(TYPE_BILLBOARD,        "Billboard 公告牌");
        ALL_TYPE.put(TYPE_ROCK,             "摇滚");
        ALL_TYPE.put(TYPE_JAZZ,             "爵士");
    }

}
