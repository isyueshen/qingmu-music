package com.luna1970.qingmumusic.dao;

import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.Gson.Song$Table;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.SaveModelTransaction;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

/**
 * Created by Yue on 2/8/2017.
 *
 */

public class SongDao {
    public static void saveAll(List<Song> songList) {
//        deleteAll();
//        new SaveModelTransaction<>(ProcessModelInfo.withModels(songList)).onExecute();
    }
    public static List<Song> getSongList() {
        return new Select().from(Song.class).where(Condition.column(Song$Table.PLAYLISTID).is(0)).queryList();
    }
    public static void deleteAll() {
//        Delete.table(Song.class, Condition.column(Song$Table.PLAYLISTID).is(0));
    }
}
