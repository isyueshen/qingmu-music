package com.luna1970.qingmumusic.dao;

import com.luna1970.qingmumusic.entity.Playlist;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

/**
 * Created by Yue on 2/11/2017.
 *
 */

public class PlaylistDao {
    public void add(Playlist playlist) {
        playlist.save();
    }
    public List<Playlist> findAll() {
        return new Select().from(Playlist.class).queryList();
    }
}
