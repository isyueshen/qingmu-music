package com.luna1970.qingmumusic.entity;

import com.luna1970.qingmumusic.dao.DBFlowDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Yue on 2/8/2017.
 *
 */

@ModelContainer
@Table(databaseName = DBFlowDatabase.NAME)
public class Playlist extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public String name;

    @Column
    public String info;

    @Column
    public long time;

    @Column
    public String tag;

    @Column
    public String coverPath;
}
