package com.luna1970.qingmumusic.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Yue on 1/27/2017.
 *
 */

public class SnappyRecyclerView2 extends RecyclerView {
    private static final String TAG = "SnappyRecyclerView";
    private int toLeftMovePosition;
    private int toRightMovePosition;

    public SnappyRecyclerView2(Context context) {
        super(context);
    }

    public SnappyRecyclerView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SnappyRecyclerView2(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        Log.i(TAG, "fling: ");
        if (Math.abs(velocityX) > 3500) {
            if (velocityX > 0) {
                smoothScrollToPosition(toRightMovePosition);
            } else {
                smoothScrollToPosition(toLeftMovePosition);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        Log.i(TAG, "onScrollStateChanged: ");
        super.onScrollStateChanged(state);

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
        toLeftMovePosition = linearLayoutManager.findFirstVisibleItemPosition();
        toRightMovePosition = linearLayoutManager.findLastVisibleItemPosition();

        int totalCount = linearLayoutManager.getItemCount();
        if (toRightMovePosition - toLeftMovePosition == 4) {
            toRightMovePosition += 2;
            toLeftMovePosition -= 2;
        } else {
            toRightMovePosition += 3;
            toLeftMovePosition -= 3;
        }
        if (toRightMovePosition > totalCount) {
            toRightMovePosition = totalCount;
        }
        if (toLeftMovePosition < 0) {
            toLeftMovePosition = 0;
        }
    }
}