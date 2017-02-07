package com.luna1970.qingmumusic.application;

import android.content.Context;
import android.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by Yue on 2/7/2017.
 *
 */

public class CustomGlideModule implements GlideModule {
    private static final String PREF_CACHE_USE_EXTERNAL_CACHE = "prefCache_useExternalCache";
    private static final String PREF_CACHE_CUSTOM_CACHE_SIZE = "prefCache_customCacheSize";

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
//        int maxCacheSize = (int) (Runtime.getRuntime().maxMemory()/8);
//        builder.setBitmapPool(new LruBitmapPool(maxCacheSize));
//        builder.setMemoryCache(new LruResourceCache(maxCacheSize));
//        // 设置是否使用外部存储保存图片缓存
//        boolean userExternal = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_CACHE_USE_EXTERNAL_CACHE, true);
//        // 缓存大小设置
//        int cacheSize = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_CACHE_CUSTOM_CACHE_SIZE, "200"))*1024*1024;
//        if (userExternal) {
//            builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, cacheSize));
//        } else {
//            builder.setDiskCache(new InternalCacheDiskCacheFactory(context, cacheSize));
//        }
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
