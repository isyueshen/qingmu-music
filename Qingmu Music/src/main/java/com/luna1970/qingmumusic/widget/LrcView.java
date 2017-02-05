package com.luna1970.qingmumusic.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import com.luna1970.qingmumusic.entity.Lrc;
import com.luna1970.qingmumusic.entity.LrcRow;
import com.luna1970.qingmumusic.listener.LrcViewSingleTapUpListener;
import com.luna1970.qingmumusic.util.ScreenUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Yue on 2/5/2017.
 *
 */

public class LrcView extends View implements View.OnTouchListener {
    private static final String TAG = "LrcView";
    private Lrc lrc;
    private Paint paint;
    private Scroller scroller;
    /**
     * 触摸事件监视器
     */
    private GestureDetector gestureDetector;
    private SimpleDateFormat simpleDateFormat;
    /**
     * 当前位置
     */
    private int position;
    /**
     * 下次歌词显示时间
     */
    private int nextTime;
    /**
     * 当前位置歌词显示时间
     */
    private int showTime;
    /**
     * 其它时间歌词文字大小
     */
    private int normalTextSize;
    /**
     * 当前时间歌词文字大小
     */
    private int currentTextSize;
    /**
     * 其它时间歌词文字颜色
     */
    private int normalTextColor;
    /**
     * 当前时间歌词文字颜色
     */
    private int currentTextColor;
    /**
     * 当没有歌词时的提示语
     */
    private String tint = "暂无歌词";
    /**
     * View总宽度
     */
    private int viewX;
    /**
     * View总高度
     */
    private int viewY;
    /**
     * View中心Y
     */
    private int centerY;
    /**
     * View中心X
     */
    private int centerX;
    /**
     * 行间距
     */
    private int dividerY;
    /**
     * 拖拽模式
     */
    private boolean TYPE_DRAG = false;
    /**
     * 拖拽总距离
     */
    private float dragDistanceY;
    /**
     * 拖拽结束时间
     */
    private long dragEndTime;
    /**
     * 拖拽开始位置
     */
    private int dragStartPosition;
    /**
     * 拖拽时的专用画笔
     */
    private Paint timeLinePaint;
    /**
     * 是否有 有效歌词
     */
    private boolean hasLrc;
    /**
     * 拖拽目标位置
     */
    private int dragPosition;
    private boolean needTint;
    private LrcViewSingleTapUpListener lrcViewSingleTapUpListener;

    public LrcView(Context context) {
        this(context, null);
    }

    public LrcView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LrcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化成员变量
     */
    private void init() {
        scroller = new Scroller(getContext(), new DecelerateInterpolator());
        // 设置抗锯齿
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 设置文字内容居中绘制, 颜色, 大小
        paint.setTextAlign(Paint.Align.CENTER);
        normalTextColor = Color.argb(150, 210, 210, 210);
        normalTextSize = (int) ScreenUtils.dp2px(14);
        currentTextColor = Color.WHITE;
        currentTextSize = (int) ScreenUtils.dp2px(14);
        dividerY = (int) ScreenUtils.dp2px(21);
        setOnTouchListener(this);
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                // 计算开始拖拽的位置
                if (dragEndTime == 0) {
                    dragStartPosition = position;
                } else {
                    dragStartPosition = dragPosition;
                }
                Log.d(TAG, "onDown: " + dragPosition + " " + dragStartPosition);
                // 将上次移动的总距离归零
                dragDistanceY = 0;
                // 初始化专用画笔
                timeLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                timeLinePaint.setTextAlign(Paint.Align.RIGHT);
                timeLinePaint.setTextSize(ScreenUtils.dp2px(14));
                timeLinePaint.setColor(Color.WHITE);
                simpleDateFormat = new SimpleDateFormat("mm:ss.S");
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (lrcViewSingleTapUpListener != null) {
                    return lrcViewSingleTapUpListener.onSingleTapUp();
                }
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (!hasLrc) {
                    return false;
                }
                // 开启拖拽模式
                TYPE_DRAG = true;
                // 防止抖动
                if (Math.abs(distanceY) < 1) {
                    return true;
                }
                // 计算拖拽到的位置
                dragDistanceY += distanceY;
                dragPosition = (int) (dragDistanceY / (normalTextSize + dividerY)) + dragStartPosition;

                // 超出歌词范围后, 滑动无效
                if (dragPosition < 0 || dragPosition > lrc.getLineCount() - 1) {
                    dragDistanceY -= distanceY;
                    return true;
                }
                // repaint
                invalidate();
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.d(TAG, "onFling: ");
                return false;
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // measure
        viewY = getMeasuredHeight();
        viewX = getMeasuredWidth();
        centerX = viewX / 2;
        centerY = viewY / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setTextSize(currentTextSize);
        // 如果无歌词或需要提示, 则绘制提示语
        if (!hasLrc || needTint) {
            canvas.drawText(tint, 0, tint.length(), centerX, centerY, paint);
            return;
        }
        paint.setColor(currentTextColor);
        // 当前歌词行对象
        LrcRow lrcRow;
        // 当前歌词目标绘制坐标Y
        int targetY;
        // 当前歌词行中心Y
        int currentLineCenterY;
        // 单行歌词内容
        String content;
        // 单行歌词长度
        int length;
        // 当前高亮歌词位置, 在拖拽模式中, 是中间, 在普通模式中是当前播放位置
        int index;
        // 绘制当前歌词行

        if (TYPE_DRAG) {
            lrcRow = lrc.getLrcRow(dragPosition);
            content = lrcRow.getContent();
            length = content.length();
            targetY = (int) (centerY + currentTextSize / 2 - dragDistanceY % (currentTextSize + dividerY));
            index = dragPosition;
            // 绘制当前时间
            String time = simpleDateFormat.format(new Date(lrcRow.getTime()));
            float aline = ScreenUtils.dp2px(5);
            canvas.drawText(time, 0, time.length(), viewX - aline, centerY - aline, timeLinePaint);
            canvas.drawLine(0, centerY, viewX, centerY, paint);
        } else {
            lrcRow = lrc.getLrcRow(position);
            content = lrcRow.getContent();
            length = content.length();
            targetY = centerY + currentTextSize / 2;
            index = position;
        }
        currentLineCenterY = targetY - currentTextSize / 2;
        canvas.drawText(content, 0, length, centerX, targetY, paint);
        // 其它文字画笔颜色&大小
        paint.setColor(normalTextColor);
        paint.setTextSize(normalTextSize);
        for (int i = 0; i < lrc.getLineCount(); i++) {
            lrcRow = lrc.getLrcRow(i);
            content = lrcRow.getContent();
            length = content.length();
            if (i != index) {
                // 当前时间后的歌词
                int between = Math.abs(index - i);
                if (i > index) {
                    targetY = currentLineCenterY + currentTextSize / 2 + dividerY * between + between * normalTextSize;
                    // 超出View屏幕范围不绘制
                    if (targetY - 2 * dividerY - normalTextSize > viewY) {
                        continue;
                    }
                } else {
                    targetY = currentLineCenterY - currentTextSize / 2 - dividerY * between - (between - 1) * normalTextSize;
                    // 不在View范围内不绘制
                    if (targetY + dividerY + normalTextSize < 0) {
                        continue;
                    }
                }
                canvas.drawText(content, 0, length, centerX, targetY, paint);
            }
        }
        super.onDraw(canvas);
    }

    /**
     * 设置Lrc数据对象
     *
     * @param lrc Object
     */
    public void setLrc(Lrc lrc) {
        this.lrc = lrc;
        if (lrc == null || lrc.getLineCount() == 0) {
            hasLrc = false;
        } else {
            needTint = false;
            hasLrc = true;
        }
        // 初始化
        position = 0;
        dragPosition = 0;
        dragEndTime = 0;
        timeLinePaint = null;
        simpleDateFormat = null;
    }

    /**
     * 外部调用, 传入当前歌曲播放进度, 然后判断是否需要重绘
     *
     * @param time 当前时间
     */
    public void refreshLrc(int time) {
        if (TYPE_DRAG) {
            // 仍在拖拽
            if (dragEndTime == 0) {
                return;
            }
            // ActionUp触发, 拖拽事件结束
            if (System.currentTimeMillis() - dragEndTime > 3000) {
                TYPE_DRAG = false;
                // 初始化
                dragEndTime = 0;
            } else {
                // 停止拖拽, 但正处于延时状态
                return;
            }
        }
        // 如果当前时间显示的正好是当前行, 则返回, 避免重绘
        if (time > showTime && time < nextTime) {
            return;
        }
        if (hasLrc) {
            LrcRow lrcRow;
            int count = lrc.getLineCount();
            for (int i = 0; i < count; i++) {
                lrcRow = lrc.getLrcRow(i);
                showTime = lrcRow.getTime();
                // 最后一行
                if (i + 1 > count - 1) {
                    nextTime = Integer.MAX_VALUE;
                    if (TextUtils.isEmpty(lrcRow.getContent())) {
                        // 防止拖拽事件结束时, 歌词不能归位
                        position = i - 1;
                        smoothScrollTo(normalTextSize + dividerY);
                        return;
                    }
                } else {
                    nextTime = lrc.getLrcRow(i + 1).getTime();
                }
                // 如果找到则调用平滑滚动进行重绘并滚动
                if (time > showTime && time < nextTime) {
                    position = i;
                    smoothScrollTo(normalTextSize + dividerY);
                    return;
                }
            }
        }
    }

    /**
     * 平滑滚动
     */
    private void smoothScrollTo(int distanceY) {
        scroller.startScroll(0, 0, 0, distanceY, 1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        // 如果滚动结束才继续
        if (scroller.computeScrollOffset()) {
            // 计算滚动距离
            int x = scroller.getCurrX();
            int y = scroller.getCurrY();
            scrollTo(x, y);
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            // 记录拖拽停止时间
            dragEndTime = System.currentTimeMillis();
        }
        return true;
    }

    public void setLrcViewSingleTapUpListener(LrcViewSingleTapUpListener lrcViewSingleTapUpListener) {
        this.lrcViewSingleTapUpListener = lrcViewSingleTapUpListener;
    }

    public void drawTint(String tint) {
        this.tint = tint;
        needTint = true;
        postInvalidate();
    }
}
