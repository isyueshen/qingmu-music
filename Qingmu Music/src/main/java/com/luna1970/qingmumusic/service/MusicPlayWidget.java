package com.luna1970.qingmumusic.service;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

/**
 * Created by Yue on 1/10/2017.
 *
 */

public class MusicPlayWidget extends AppWidgetProvider {
//    private int i;
    private static final String TAG = "MusicPlayWidget";
//    @Override
//    public void onEnabled(Context context) {
//        Log.i(TAG, "onEnabled: ");
//        super.onEnabled(context);
//    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        Log.i(TAG, "onUpdate: ");
//        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.activity_widget_play);
////        remoteViews.setImageViewResource(R.id.miniAlbumPic, R.mipmap.ic_launcher);
//        remoteViews.setTextViewText(R.id.musicInfoTV, "dfsadf");
//        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Log.i(TAG, "onReceive: ");
//        super.onReceive(context, intent);
//    }
//
//    @Override
//    public void onDeleted(Context context, int[] appWidgetIds) {
//        Log.i(TAG, "onDeleted: ");
//        super.onDeleted(context, appWidgetIds);
//    }
//
//    @Override
//    public void onDisabled(Context context) {
//        Log.i(TAG, "onDisabled: ");
//        super.onDisabled(context);
//    }
}
