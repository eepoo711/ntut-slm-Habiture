package com.ntil.habiture;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by GawinHsu on 5/4/15.
 */
public class PushView extends View{
    private Canvas mCanvas;
    private Paint mPaint;
    private Path mPath = new Path();
    private final boolean DEBUG = true;
    private float mX, mY;

    private void trace(String message) {
        if(DEBUG)
            Log.d("HomeMiddleFragment", message);
    }

    public PushView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDown(event);
                mCanvas.drawCircle(event.getX(), event.getY(), 5, mPaint);
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                touchMove(event);
                mCanvas.drawCircle(event.getX(), event.getY(), 5, mPaint);
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                touchUp(event);
                mCanvas.drawCircle(event.getX(), event.getY(), 5, mPaint);
                invalidate();
                return true;
        }
        return false;
    }

    private void touchUp(MotionEvent event) {
        trace("touchUp");
        mCanvas.drawCircle(event.getX(), event.getY(), 5, mPaint);
    }

    private void touchMove(MotionEvent event) {
        trace("touchMove");
        mCanvas.drawCircle(event.getX(), event.getY(), 5, mPaint);
        // mCanvas.drawPoint(event.getX(), event.getY(), mPaint);
    }

    private void touchDown(MotionEvent event) {
        trace("touchDown, x = " + event.getX() + ", y=" + event.getY());
        mCanvas.drawCircle(event.getX(), event.getY(), 5, mPaint);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        trace("onDraw");
        mCanvas = canvas;
        mPaint = new Paint();

        setWillNotDraw(false);
        // 底色
        //mCanvas.drawColor(Color.GRAY);
        //invalidate();

        //canvas.drawCircle(160, 160, 150, paint);
    }
}
