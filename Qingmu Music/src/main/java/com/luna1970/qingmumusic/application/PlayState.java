package com.luna1970.qingmumusic.application;

import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.dao.SongDao;
import com.luna1970.qingmumusic.dao.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yue on 2/2/2017.
 *
 */

public class PlayState {
    private int currentPosition = -1;
    private List<Song> playList;
    private boolean isPlaying;
    private int currentPlayMode;

    private PlayState() {
    }

    public static PlayState getInstance() {
        PlayState playState = new PlayState();
        playState.currentPlayMode = PlayMode.REPEAT_ALL;
        playState.playList = new ArrayList<>();
        return playState;
    }

    /**
     * 更新播放列表
     * @param data 数据
     */
    public void updatePlayList(List<Song> data) {
        if (data == null) {
            return;
        }
        playList.clear();
        playList.addAll(data);
        SongDao.saveAll(getPlayList());
    }

    public void setPlayList(List<Song> data) {
        if (data == null) {
            return;
        }
        playList.clear();
        playList.addAll(data);
    }
    /**
     * 在列表指定位置插入歌曲
     * @param song Song Object
     */
    public void insertPlayNext(Song song) {
        playList.add(currentPosition + 1, song);
    }

    /**
     * 移除指定位置的歌曲
     *
     * @param index 索引
     * @return 返回移除的song object
     */
    public Song removeSongAt(int index) {
        return playList.remove(index);
    }

    /**
     * 清空列表
     */
    public void clearSong() {
        if (getListSize() > 0) {
            playList.clear();
        }
    }

    /**
     * 获得当前播放列表
     * @return 播放列表
     */
    public List<Song> getPlayList() {
        return playList;
    }

    /**
     * 获得当前播放列表的歌曲数目
     *
     * @return 歌曲数目
     */
    public int getListSize() {
        if (playList != null) {
            return playList.size();
        }
        return 0;
    }

    /**
     * 设置当前播放索引
     *
     * @param currentPosition 索引
     */
    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        UserInfo.saveCurrentPosition(currentPosition);
    }

    /**
     * 获得当前索引
     * @return 索引
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    /**
     * 获得当前播放的歌曲对象
     *
     * @return song object
     */
    public Song getSong() {
        if (currentPosition < 0) {
            return null;
        }
        return playList.get(currentPosition);
    }

    /**
     * 是否处于播放状态
     * @return 播放状态
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * 设置是否开始进入播放状态
     *
     * @param playing 状态
     */
    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    /**
     * 获得当前播放歌曲的song id
     *
     * @return
     */
    public int getSongID() {
        return playList.get(currentPosition).songId;
    }

    /**
     * 获得当前播放模式
     * @return 播放模式常量
     */
    public int getCurrentPlayMode() {
        return currentPlayMode;
    }

    /**
     * 设置播放模式
     * @param currentPlayMode 播放模式常量
     */
    public void setCurrentPlayMode(int currentPlayMode) {
        this.currentPlayMode = currentPlayMode;
    }

    /**
     * 返回当前播放歌曲的时间
     *
     * @return 歌曲时间
     */
    public int getDuration() {
        return getSong().duration;
    }
}
