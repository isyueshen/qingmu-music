package com.luna1970.qingmumusic.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Yue on 1/27/2017.
 *
 */

public class SnappyRecyclerView extends RecyclerView {
    private static final String TAG = "SnappyRecyclerView";
    private int downPosition;

    public SnappyRecyclerView(Context context) {
        super(context);
    }

    public SnappyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SnappyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        if (Math.abs(velocityX) > 500) {
            if (velocityX > 0) {
                smoothScrollToPosition(downPosition + 1);
            } else {
                if (downPosition - 1 >= 0) {
                    smoothScrollToPosition(downPosition - 1);
                } else {
                    smoothScrollToPosition(0);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
        int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

        if (lastVisibleItemPosition - firstVisibleItemPosition == 2) {
            downPosition = firstVisibleItemPosition + 1;
        } else if (lastVisibleItemPosition - firstVisibleItemPosition == 1){
            downPosition = firstVisibleItemPosition;
            if (lastVisibleItemPosition == linearLayoutManager.getItemCount() - 1) {
                downPosition = firstVisibleItemPosition + 1;
            }
        } else if (lastVisibleItemPosition - firstVisibleItemPosition == 0) {
            downPosition = lastVisibleItemPosition;
        }
    }

}