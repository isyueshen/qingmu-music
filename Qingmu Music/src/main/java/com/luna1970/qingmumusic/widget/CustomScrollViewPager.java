package com.luna1970.qingmumusic.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Yue on 1/27/2017.
 *
 */

public class CustomScrollViewPager extends ViewPager {
    public CustomScrollViewPager(Context context) {
        super(context);
    }

    public CustomScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean canScroll = false;

    public boolean isCanScroll() {
        return canScroll;
    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (canScroll) {
            return super.onTouchEvent(ev);
        } else {
            return true;
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        if (canScroll) {
            super.scrollTo(x, y);
        }
    }
}
