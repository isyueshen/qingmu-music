package com.luna1970.qingmumusic.util;

import android.text.TextUtils;

import com.luna1970.qingmumusic.entity.Lrc;
import com.luna1970.qingmumusic.entity.LrcRow;
import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by Yue on 2/5/2017.
 * lrc parse tool
 */

public class LrcParse {
    public static Lrc parseLrc(String raw) {
        Lrc lrc = new Lrc();
        lrc.setLrcRowList(new ArrayList<LrcRow>());
        BufferedReader bufferedReader = new BufferedReader(new StringReader(raw));
        String line;
        LrcRow lrcRow;
        Logger.d(raw);
        try {
            String prevLine = null;
            int prevTime = 0;
            boolean merge = false;
            while ((line = bufferedReader.readLine()) != null) {
                if (TextUtils.isEmpty(line)) {
                    continue;
                }

                String check = line.substring(line.indexOf("[") + 1, line.indexOf(":"));
                if (!check.matches("[0-9]+")) {
                    continue;
                }
                // 单行歌词内容
                String content = line.substring(line.lastIndexOf("]") + 1).trim();
                // 分
                String minute = line.substring(line.indexOf("[") + 1, line.indexOf(":"));
                // 秒
                String second = line.substring(line.indexOf(":") + 1, line.indexOf("."));
                // 毫秒
                String milliSecond = line.substring(line.indexOf(".") + 1, line.lastIndexOf("]"));
                // 时间(毫秒)
                int time = Integer.parseInt(minute) * 60 * 1000 + Integer.parseInt(second) * 1000
                        + Integer.parseInt(milliSecond) * 10;
                if (merge) {
                    content = prevLine + content;
                    time = prevTime;
                }
                if (content.length()<3) {
                    prevLine = content;
                    prevTime = time;
                    merge = true;
                    continue;
                } else {
                    merge = false;
                }
                // 赋值
                lrcRow = new LrcRow(content, time);
                lrc.getLrcRowList().add(lrcRow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lrc;
    }


    public static String getLrcContent(String path) {
        String raw = "";
        if (path.startsWith("http://")) {
            requestLrcByNet();
        } else {
            requestLrcByFile();
        }
        return raw;
    }

    private static void requestLrcByFile() {

    }

    private static void requestLrcByNet() {
    }
}
