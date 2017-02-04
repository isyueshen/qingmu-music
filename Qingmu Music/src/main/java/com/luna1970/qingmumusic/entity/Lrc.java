package com.luna1970.qingmumusic.entity;

import java.util.List;

/**
 * Created by Yue on 2/5/2017.
 *
 */

public class Lrc {
    private List<LrcRow> lrcRowList;

    public List<LrcRow> getLrcRowList() {
        return lrcRowList;
    }

    public void setLrcRowList(List<LrcRow> lrcRowList) {
        this.lrcRowList = lrcRowList;
    }

    public Lrc(List<LrcRow> lrcRowList) {

        this.lrcRowList = lrcRowList;
    }

    public Lrc() {
    }
}
