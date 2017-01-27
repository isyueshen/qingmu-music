package com.luna1970.qingmumusic.dao;

import com.luna1970.qingmumusic.entity.Music;

import java.util.List;

/**
 * Created by Yue on 1/9/2017.
 *
 */

public interface MusicDao {
    List<Music> findAll();
}
