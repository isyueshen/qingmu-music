package com.luna1970.qingmumusic.application;

import com.luna1970.qingmumusic.Gson.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yue on 2/2/2017.
 *
 */

public class PlayState {
    private int currentPosition = 0;
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

    public void updatePlayList(List<Song> data) {
        playList.clear();
        playList.addAll(data);
    }

    public void setPlayList(List<Song> playList) {
        this.playList = playList;
    }

    public void insertPlayNext(Song song) {
        playList.add(currentPosition + 1, song);
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public List<Song> getPlayList() {
        return playList;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public int getSongID() {
        return playList.get(currentPosition).songId;
    }

    public int getCurrentPlayMode() {
        return currentPlayMode;
    }

    public void setCurrentPlayMode(int currentPlayMode) {
        this.currentPlayMode = currentPlayMode;
    }

    public int getListSize() {
        if (playList!=null) {
            return playList.size();
        }
        return 0;
    }

    public Song getSong() {
        return playList.get(currentPosition);
    }
    public int getDuration() {
        return getSong().duration;
    }

    public Song removeSongAt(int index) {
        return playList.remove(index);
    }

    public void clearSong() {
        if (getListSize() >0) {
            playList.clear();
        }
    }
}
