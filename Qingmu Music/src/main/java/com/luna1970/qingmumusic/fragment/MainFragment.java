package com.luna1970.qingmumusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.util.DailyRecommendType;

import java.util.HashMap;

/**
 * Created by Yue on 1/26/2017.
 *
 */

public class MainFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        startFragment();
        return view;
    }

    private void startFragment() {
        HashMap<Integer, String> allType = DailyRecommendType.ALL_TYPE;
        MainNewAlbumFragment mainNewAlbumFragment = new MainNewAlbumFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_new_album_fragment, mainNewAlbumFragment);
        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment TYPE_NEW_SONG = MainRecommendListFragment.newInstance(DailyRecommendType.TYPE_NEW_SONG,       allType.get(DailyRecommendType.TYPE_NEW_SONG));
        fragmentTransaction.replace(R.id.TYPE_NEW_SONG , TYPE_NEW_SONG);
        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment TYPE_HOT_SONG = MainRecommendListFragment.newInstance(DailyRecommendType.TYPE_HOT_SONG,       allType.get(DailyRecommendType.TYPE_HOT_SONG));
        fragmentTransaction.replace(R.id.TYPE_HOT_SONG, TYPE_HOT_SONG);
        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment TYPE_EUROPE_AMERICA = MainRecommendListFragment.newInstance(DailyRecommendType.TYPE_EUROPE_AMERICA, allType.get(DailyRecommendType.TYPE_EUROPE_AMERICA));
        fragmentTransaction.replace(R.id.TYPE_EUROPE_AMERICA, TYPE_EUROPE_AMERICA);
        fragmentTransaction.commit();

//        fragmentTransaction = fragmentManager.beginTransaction();
//        Fragment TYPE_ORIGINAL = MainRecommendListFragment.newInstance(DailyRecommendType.TYPE_ORIGINAL,       allType.get(DailyRecommendType.TYPE_ORIGINAL));
//        fragmentTransaction.replace(R.id.TYPE_ORIGINAL, TYPE_ORIGINAL);
//        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment TYPE_POP_MUSIC = MainRecommendListFragment.newInstance(DailyRecommendType.TYPE_POP_MUSIC,      allType.get(DailyRecommendType.TYPE_POP_MUSIC));
        fragmentTransaction.replace(R.id.TYPE_POP_MUSIC, TYPE_POP_MUSIC);
        fragmentTransaction.commit();

//        fragmentTransaction = fragmentManager.beginTransaction();
//        Fragment TYPE_CHINESE_SONG = MainRecommendListFragment.newInstance(DailyRecommendType.TYPE_CHINESE_SONG,   allType.get(DailyRecommendType.TYPE_CHINESE_SONG));
//        fragmentTransaction.replace(R.id.TYPE_CHINESE_SONG, TYPE_CHINESE_SONG);
//        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment TYPE_CLASSICAL_SONG = MainRecommendListFragment.newInstance(DailyRecommendType.TYPE_CLASSICAL_SONG, allType.get(DailyRecommendType.TYPE_CLASSICAL_SONG));
        fragmentTransaction.replace(R.id.TYPE_CLASSICAL_SONG, TYPE_CLASSICAL_SONG);
        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment TYPE_NET_WORK_SONGS = MainRecommendListFragment.newInstance(DailyRecommendType.TYPE_NET_WORK_SONGS, allType.get(DailyRecommendType.TYPE_NET_WORK_SONGS));
        fragmentTransaction.replace(R.id.TYPE_NET_WORK_SONGS, TYPE_NET_WORK_SONGS);
        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment TYPE_FILM_SONGS = MainRecommendListFragment.newInstance(DailyRecommendType.TYPE_FILM_SONGS,     allType.get(DailyRecommendType.TYPE_FILM_SONGS));
        fragmentTransaction.replace(R.id.TYPE_FILM_SONGS, TYPE_FILM_SONGS);
        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment TYPE_LOVE_SONGS = MainRecommendListFragment.newInstance(DailyRecommendType.TYPE_LOVE_SONGS,     allType.get(DailyRecommendType.TYPE_LOVE_SONGS));
        fragmentTransaction.replace(R.id.TYPE_LOVE_SONGS, TYPE_LOVE_SONGS);
        fragmentTransaction.commit();

//        fragmentTransaction = fragmentManager.beginTransaction();
//        Fragment TYPE_BILLBOARD = MainRecommendListFragment.newInstance(DailyRecommendType.TYPE_BILLBOARD,      allType.get(DailyRecommendType.TYPE_BILLBOARD));
//        fragmentTransaction.replace(R.id.TYPE_BILLBOARD, TYPE_BILLBOARD);
//        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment TYPE_ROCK = MainRecommendListFragment.newInstance(DailyRecommendType.TYPE_ROCK,           allType.get(DailyRecommendType.TYPE_ROCK));
        fragmentTransaction.replace(R.id.TYPE_ROCK, TYPE_ROCK);
        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment TYPE_JAZZ = MainRecommendListFragment.newInstance(DailyRecommendType.TYPE_JAZZ,           allType.get(DailyRecommendType.TYPE_JAZZ));
        fragmentTransaction.replace(R.id.TYPE_JAZZ, TYPE_JAZZ);
        fragmentTransaction.commit();

    }
}
