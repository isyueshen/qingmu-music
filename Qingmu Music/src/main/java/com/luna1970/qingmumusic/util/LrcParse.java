package com.luna1970.qingmumusic.util;

import com.luna1970.qingmumusic.entity.Lrc;
import com.luna1970.qingmumusic.entity.LrcRow;

import java.io.BufferedReader;
import java.io.StringReader;

/**
 * Created by Yue on 2/5/2017.
 * lrc parse tool
 */

public class LrcParse {
    public static Lrc parseLrc(String raw) {
        Lrc lrc = new Lrc();
        BufferedReader bufferedReader = new BufferedReader(new StringReader(raw));
        String line;
        LrcRow lrcRow;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                // 单行歌词内容
                String content = line.substring(line.lastIndexOf("]") + 1);
                // 分
                String minute = line.substring(line.indexOf("[") + 1, line.indexOf(":"));
                // 秒
                String second = line.substring(line.indexOf(":") + 1, line.indexOf("."));
                // 毫秒
                String milliSecond = line.substring(line.indexOf(".") + 1, line.lastIndexOf("]"));
                // 时间(毫秒)
                int time = Integer.parseInt(minute) * 60 * 1000 + Integer.parseInt(second) * 1000
                        + Integer.parseInt(milliSecond) * 1000;
                // 赋值
                lrcRow = new LrcRow(content, time);
                lrc.getLrcRowList().add(lrcRow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lrc;
    }


}
