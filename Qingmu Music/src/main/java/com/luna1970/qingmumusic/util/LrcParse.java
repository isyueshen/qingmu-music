package com.luna1970.qingmumusic.util;

import android.text.TextUtils;

import com.luna1970.qingmumusic.entity.Lrc;
import com.luna1970.qingmumusic.entity.LrcRow;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * lrc parse tool
 */

public class LrcParse {
    public static Lrc parseLrc(String raw) {
        Lrc lrc = new Lrc();
        lrc.setLrcRowList(new ArrayList<LrcRow>());
        BufferedReader bufferedReader = new BufferedReader(new StringReader(raw));
        String line;
        LrcRow lrcRow;
        try {
            String prevLine = null;
            int prevTime = 0;
            boolean merge = false;
            while ((line = bufferedReader.readLine()) != null) {
                // 如果是空行
                if (TextUtils.isEmpty(line)) {
                    continue;
                }
                // 如果是非法数据
                String check = line.substring(line.indexOf("[") + 1, line.indexOf(":"));
                if (!check.matches("[0-9]+")) {
                    continue;
                }
                // 单行歌词内容
                String content = line.substring(line.lastIndexOf("]") + 1).trim();
                // 分
                String minute = line.substring(line.indexOf("[") + 1, line.indexOf(":"));
                // 秒
                String second = line.substring(line.indexOf(":") + 1, 6);
                // 毫秒
                String milliSecond = line.substring(7, line.lastIndexOf("]"));
                // 时间(毫秒)
                int time = Integer.parseInt(minute) * 60 * 1000 + Integer.parseInt(second) * 1000
                        + Integer.parseInt(milliSecond) * 10;
                // 空行则直接添加
                if (TextUtils.isEmpty(content)) {
                    lrcRow = new LrcRow(content, time);
                    lrc.getLrcRowList().add(lrcRow);
                    continue;
                }
                // 内容过短需要合并
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
