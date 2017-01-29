package com.luna1970.qingmumusic.util;

import java.text.MessageFormat;

/**
 * Created by Yue on 1/29/2017.
 *
 */

public class UriUtils {
    private static final String newSong = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.billboard.billList&format=json&type={0}&offset={1}&size={2}";
    public static String getRecommendUri (int type, int offset, int size) {
        MessageFormat messageFormat = new MessageFormat(newSong);
        return messageFormat.format(new String[]{type + "", offset + "", size + ""});
    }

    public static String getCustomImageSize (String oldUri, int size) {
        StringBuilder stringBuilder = new StringBuilder(oldUri);
        return stringBuilder.delete(stringBuilder.length()-3, stringBuilder.length()).append(size).toString();
    }
}
