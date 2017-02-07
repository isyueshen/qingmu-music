package com.luna1970.qingmumusic.fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.util.GlideCacheUtil;
import com.luna1970.qingmumusic.util.ToastUtils;

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
        initPreferenceData();
        sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                switch (key) {
                    case "":
                        break;
                }
                Preference preference = findPreference("prefEasy_lowBattery");
                if (key.equals(preference.getKey())) {
                    preference.setSummary("百分之5");
                }
            }
        };
    }

    private void initPreferenceData() {
        final Preference imageCacheSize = findPreference("prefCache_clearDiskCache");
        imageCacheSize.setSummary(GlideCacheUtil.getInstance().getExternalCacheSize());
        imageCacheSize.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("操作提示")
                        .setMessage("确认清空图片缓存吗?")
                        .setPositiveButton(R.string.userAction_confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ToastUtils.makeText("正在清理").show();
                                GlideCacheUtil.getInstance().clearImageAllCache();
                                imageCacheSize.setSummary("0.0Byte");
                                ToastUtils.makeText("清理完成").show();
                            }
                        })
                        .setNegativeButton(R.string.userAction_cancel, null)
                        .show();
                return false;
            }
        });
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
