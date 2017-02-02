package com.luna1970.qingmumusic.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Yue on 1/27/2017.
 *
 */

public class HttpUtils {
    public static void sendHttpRequest(String url, okhttp3.Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

}
