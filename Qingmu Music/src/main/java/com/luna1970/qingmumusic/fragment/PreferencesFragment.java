package com.luna1970.qingmumusic.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.luna1970.qingmumusic.R;

/**
 * Created by Yue on 2/7/2017.
 *
 */

public class PreferencesFragment extends PreferenceFragment {

    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Preference preference = findPreference("prefEasy_lowBattery");
                if (key.equals(preference.getKey())) {
                    preference.setSummary("百分之5");
                }
            }
        };
    }

    @Override
    public void onResume() {
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
        super.onResume();
    }

    @Override
    public void onPause() {
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
        super.onPause();
    }
}
