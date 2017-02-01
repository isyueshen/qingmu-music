package com.luna1970.qingmumusic.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Yue on 2/1/2017.
 *
 */

public class RequestJsonDataService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RequestJsonDataService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
