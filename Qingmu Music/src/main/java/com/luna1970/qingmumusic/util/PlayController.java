package com.luna1970.qingmumusic.util;

/**
 * Created by Yue on 1/9/2017.
 *
 */

public final class PlayController {
    // play button selected
    public static final String ACTION_PLAY_OR_PAUSE = "PLAY_OR_PAUSE";
    public static final String ACTION_PLAY_NEXT = "PLAY_NEXT";
    public static final String ACTION_PLAY_PREV = "PLAY_PREV";
    public static final String ACTION_PLAY_SPECIFIC = "PLAY_SPECIFIC";

    // playing mode
    public static final String ACTION_PLAY_MODE_CHANGED = "PLAY_MODE_CHANGED";

    // seek bar progress
    public static final String ACTION_SEEK_BAR_PROGRESS_CHANGED = "SEEK_BAR_PROGRESS_CHANGED";

    // service state
    public static final String STATE_SERVICE_PLAYING = "SERVICE_PLAYING";
    public static final String STATE_SERVICE_PLAY_CONTINUE = "SERVICE_PLAY_CONTINUE";
    public static final String STATE_SERVICE_PAUSE = "SERVICE_PAUSE";
    public static final String STATE_SERVICE_STOP = "SERVICE_STOP";
    public static final String STATE_SERVICE_UPDATE_SEEK_BAR_PROGRESS = "SERVICE_UPDATE_SEEK_BAR_PROGRESS";
    public static final String STATE_SERVICE_UPDATE_BUFFER_PROGRESS = "STATE_SERVICE_UPDATE_BUFFER_PROGRESS";

    // fragment
    public static final String ACTION_REFRESH_PLAY_LIST = "REFRESH_PLAY_LIST";

}
