package com.king.mytennis.view_v_7_0.view;

import com.king.mytennis.view_v_7_0.view.DragLayout.Status;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class DragCustomRelativeLayout extends RelativeLayout {
    private DragLayout dl;

    public DragCustomRelativeLayout(Context context) {
        super(context);
    }

    public DragCustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragCustomRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setDragLayout(DragLayout dl) {
        this.dl = dl;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (dl.getStatus() != Status.Close) {
            return true;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (dl.getStatus() != Status.Close) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                dl.close();
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

}
