package com.luna1970.qingmumusic.entity;

/**
 * Created by Yue on 2/5/2017.
 *
 */

public class LrcRow {
    private String content;
    private int time;

    @Override
    public String toString() {
        return "LrcRow{" +
                "content='" + content + '\'' +
                ", time=" + time +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public LrcRow() {

    }

    public LrcRow(String content, int time) {

        this.content = content;
        this.time = time;
    }
}
