package com.luna1970.qingmumusic.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

import com.luna1970.qingmumusic.application.MusicApplication;
import com.luna1970.qingmumusic.entity.UpdateInfo;
import com.luna1970.qingmumusic.util.GlobalConst;
import com.luna1970.qingmumusic.util.HttpUtils;
import com.luna1970.qingmumusic.util.ParseUtils;
import com.luna1970.qingmumusic.util.SharedPreferencesUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DownloadIntentService extends IntentService {
    private static final String TAG = "DownloadIntentService";
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_UPDATE = "com.luna1970.qingmumusic.service.action.UPDATE";
    private static final String ACTION_BAZ = "com.luna1970.qingmumusic.service.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.luna1970.qingmumusic.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.luna1970.qingmumusic.service.extra.PARAM2";
    private static final String EXTRA_PARAM_CHECK_ONLY_ON_WIFI = "DownloadIntentService.extra.PARAM_checkOnlyOnWifi";
    private static final String EXTRA_PARAM_START_UPDATE = "DownloadIntentService.extra.PARAM_startUpdate";

    private static CheckForUpdateListener checkForUpdateListener;

    public DownloadIntentService() {
        super("DownloadIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DownloadIntentService.class);
        intent.setAction(ACTION_UPDATE);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action CheckUpdate with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionCheckForUpdate(Context context, boolean checkOnlyOnWifi, boolean startUpdate, CheckForUpdateListener checkForUpdateListener) {
        Intent intent = new Intent(context, DownloadIntentService.class);
        intent.putExtra(EXTRA_PARAM_CHECK_ONLY_ON_WIFI, checkOnlyOnWifi);
        intent.putExtra(EXTRA_PARAM_START_UPDATE, startUpdate);
        intent.setAction(ACTION_UPDATE);
        DownloadIntentService.checkForUpdateListener = checkForUpdateListener;
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE.equals(action)) {
                final boolean checkOnlyOnWifi = intent.getBooleanExtra(EXTRA_PARAM1, true);
                final boolean startUpdate = intent.getBooleanExtra(EXTRA_PARAM2, true);
                handleActionUpdate(checkOnlyOnWifi, startUpdate);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionDownloadMusic(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionUpdate(final boolean checkOnlyOnWifi, final boolean startUpdate) {
        // 初始化工作
        File file = new File(getExternalCacheDir() + File.separator + "update");
        boolean dirIsMade = true;
        if (!file.exists()) {
            dirIsMade = file.mkdirs();
        }
        if (!dirIsMade) {
//            if (checkForUpdateListener != null) {
//                checkForUpdateListener.onFailed(CheckForUpdateListener.TYPE_DIR_CANT_MAKE);
//            }
//            return;
            file = getCacheDir();
        }

        if (checkOnlyOnWifi) {
            boolean updateOnWifi = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SharedPreferencesUtils.PREF_KEY_UPDATE_PACKAGE_ON_WIFI, true);
            if (!updateOnWifi && MusicApplication.currentNetType != ConnectivityManager.TYPE_WIFI) {
                return;
            }
        }

        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            final int versionCode = packageInfo.versionCode;
            final File finalFile = file;
            HttpUtils.sendHttpRequest(GlobalConst.UPDATE_WEBSITE_PACKAGE_INFO, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (checkForUpdateListener != null) {
                        checkForUpdateListener.onFailed(CheckForUpdateListener.TYPE_CHECK_FAILED);
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    UpdateInfo updateInfo = ParseUtils.parseUpdateInfo(response.body().string());
                    if (updateInfo.getVersionCode() > versionCode) {
                        if (checkForUpdateListener != null) {
                            checkForUpdateListener.onCheckResult(true);
                        }
                        if (startUpdate) {
                            startDownloadUpdatePackage(updateInfo.getUpdateUrl(), finalFile);
                        }
                    } else {
                        checkForUpdateListener.onCheckResult(false);
                    }
                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 开始下载升级包
     *
     * @param uri 升级安装包下载地址
     */
    private void startDownloadUpdatePackage(String uri, File file) {
        String filename = uri.substring(uri.lastIndexOf("/")+1);
        final File newFile = new File(file, filename);
        if (newFile.exists()) {
            if (checkForUpdateListener != null) {
                checkForUpdateListener.onDownLoadComplete();
            }
            return;
        }
        if (MusicApplication.currentNetType == ConnectivityManager.TYPE_WIFI) {
            HttpUtils.sendHttpRequest(uri, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    FileOutputStream fileOutputStream = null;
                    long len = response.body().contentLength();
                    InputStream inputStream = null;
                    long temp = 0;
                    try {
                        inputStream = response.body().byteStream();
                        byte[] cache = new byte[8 * 1024];
                        int tag;
                        fileOutputStream = new FileOutputStream(newFile, false);
                        while ((tag = inputStream.read(cache)) != -1) {
                            fileOutputStream.write(cache, 0, tag);
                            temp += tag;
                        }
                        fileOutputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    // 下载结果
                    if (len == temp) {
                        SharedPreferencesUtils.save(DownloadIntentService.this, SharedPreferencesUtils.PREF_KEY_DOWNLOAD_UPDATE_PACKAGE_TIME, System.currentTimeMillis());
                        if (checkForUpdateListener != null) {
                            checkForUpdateListener.onDownLoadComplete();
                        }
                    } else {
                        newFile.delete();
                        if (checkForUpdateListener != null) {
                            checkForUpdateListener.onFailed(CheckForUpdateListener.TYPE_DOWNLOAD_FAILED);
                        }
                    }
                    checkForUpdateListener = null;
                }
            });
        }
    }

    /**
     * Handle action DownloadMusic in the provided background thread with the provided
     * parameters.
     */
    private void handleActionDownloadMusic(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public interface CheckForUpdateListener {
        int TYPE_DIR_CANT_MAKE = 0;
        int TYPE_CHECK_FAILED = 1;
        int TYPE_DOWNLOAD_FAILED = 2;

        void onCheckResult(boolean result);

        void onFailed(int type);

        void onDownLoadComplete();
    }
}
