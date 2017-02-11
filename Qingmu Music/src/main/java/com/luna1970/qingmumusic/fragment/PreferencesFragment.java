package com.luna1970.qingmumusic.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;

import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.service.DownloadIntentService;
import com.luna1970.qingmumusic.util.GlideCacheUtil;
import com.luna1970.qingmumusic.util.ToastUtils;

import java.io.File;

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
        // 图片缓存大小
        final Preference imageCacheSize = findPreference("prefCache_clearDiskCache");
        imageCacheSize.setSummary("正在统计...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String size = GlideCacheUtil.getInstance().getExternalCacheSize();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageCacheSize.setSummary(size);
                    }
                });
            }
        }).start();
        // 清除图片缓存
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
        // 检查更新
        final Preference checkForUpdate = findPreference("prefEasy_checkForUpdate");
        checkForUpdate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ToastUtils.show("正在检测新版本...");
                DownloadIntentService.startActionCheckForUpdate(getActivity(), false, false, new DownloadIntentService.CheckForUpdateListener() {
                    @Override
                    public void onCheckResult(final boolean result) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (result) {
                                    new AlertDialog.Builder(getActivity())
                                            .setTitle("升级提示")
                                            .setMessage("检测到新版本, 确认开始下载升级安装包吗?")
                                            .setPositiveButton(R.string.userAction_confirm, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    ToastUtils.show("开始下载...");
                                                    DownloadIntentService.startActionCheckForUpdate(getActivity(), false, true, new DownloadIntentService.CheckForUpdateListener() {
                                                        @Override
                                                        public void onCheckResult(boolean result) {

                                                        }

                                                        @Override
                                                        public void onFailed(int type) {
                                                        }

                                                        @Override
                                                        public void onDownLoadComplete() {
                                                            File file = new File(getActivity().getExternalCacheDir() + File.separator + "update", "update.apk");
                                                            if (!file.exists()) {
                                                                file = new File(getActivity().getCacheDir(), "update.apk");
                                                            }
                                                            Intent intent = new Intent();
                                                            //执行动作
                                                            intent.setAction(Intent.ACTION_VIEW);
                                                            //执行的数据类型
                                                            Uri uri;
                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                                uri = FileProvider.getUriForFile(getActivity(), "com.luna1970.qingmumusic.provider", file);
                                                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                            } else {
                                                                uri = Uri.fromFile(file);
                                                            }
                                                            intent.setDataAndType(uri, "application/vnd.android.package-archive");
                                                            startActivity(intent);
                                                        }
                                                    });
                                                }
                                            })
                                            .setNegativeButton(R.string.userAction_cancel, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    ToastUtils.show("您已经取消升级!");
                                                }
                                            })
                                            .show();
                                } else {
                                    ToastUtils.show("没有新版本");
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailed(int type) {
                        switch (type) {
                            case DownloadIntentService.CheckForUpdateListener.TYPE_CHECK_FAILED:
                                ToastUtils.show("检查失败");
                                break;
                            case DownloadIntentService.CheckForUpdateListener.TYPE_DIR_CANT_MAKE:
                                ToastUtils.show("存储错误");
                                break;
                            case DownloadIntentService.CheckForUpdateListener.TYPE_DOWNLOAD_FAILED:
                                ToastUtils.show("下载失败");
                                break;
                        }
                    }

                    @Override
                    public void onDownLoadComplete() {
                    }
                });
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
