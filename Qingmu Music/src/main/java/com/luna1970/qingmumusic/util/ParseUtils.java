package com.luna1970.qingmumusic.util;

import android.text.TextUtils;

import com.luna1970.qingmumusic.entity.Lrc;
import com.luna1970.qingmumusic.entity.LrcRow;
import com.luna1970.qingmumusic.entity.UpdateInfo;
import com.orhanobut.logger.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * all kinds of parse tools
 */

public class ParseUtils {
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
                line = line.trim();
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
                String content = line.substring(line.indexOf("]") + 1).trim();
                // 分
                String minute = line.substring(line.indexOf("[") + 1, line.indexOf(":"));
                // 秒 (这里使用6, 是因为有的格式不符合规范, 如'[01:02:03]')
                String second = line.substring(line.indexOf(":") + 1, 6);
                // 毫秒
                String milliSecond = line.substring(7, line.indexOf("]"));
                // 总时间 (毫秒)
                int time = Integer.parseInt(minute) * 60 * 1000 + Integer.parseInt(second) * 1000
                        + Integer.parseInt(milliSecond) * 10;
                // 空行则直接添加
                if (TextUtils.isEmpty(content)) {
                    lrcRow = new LrcRow("♪", time);
                    lrc.getLrcRowList().add(lrcRow);
                    continue;
                }
                // 内容过短需要合并
                if (merge) {
                    content = prevLine + content;
                    time = prevTime;
                }
                if (content.length() < 3) {
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


    /**
     * 解析升级update.xml为UpdateInfo Object
     * @param raw update.xml 内容字符串
     * @return 解析完成的UpdateInfo Object
     */
    public static UpdateInfo parseUpdateInfo(String raw) {
        UpdateInfo updateInfo = null;
        try {
            XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
            Logger.d(raw);
            xmlPullParser.setInput(new StringReader(raw));
            int type = xmlPullParser.getEventType();
            while (type != XmlPullParser.END_DOCUMENT) {
                if (type == XmlPullParser.START_TAG) {
                    String tag = xmlPullParser.getName();
                    switch (tag) {
                        case "info":
                            updateInfo = new UpdateInfo();
                            break;
                        case "version_code":
                            int versionCode = Integer.parseInt(xmlPullParser.nextText());
                            updateInfo.setVersionCode(versionCode);
                            break;
                        case "version_name":
                            String versionName = xmlPullParser.nextText();
                            updateInfo.setVersionName(versionName);
                            break;
                        case "update_url":
                            String updateUrl = xmlPullParser.nextText();
                            updateInfo.setUpdateUrl(updateUrl);
                            break;
                        case "tip":
                            String tip = xmlPullParser.nextText();
                            updateInfo.setTip(tip);
                            break;
                    }
                }
                type = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateInfo;
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
