package com.ntil.habiture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import utils.BitmapHelper;

/**
 * Created by GawinHsu on 5/6/15.
 */
public class PokeView extends View {
    private final boolean DEBUG = true;
    private Paint mPaint = new Paint();
    private Bitmap bitmapBackground = null;
    private Bitmap bitmapDrawing = null;
    private Bitmap bitmapTool = null;
    private float mX = 0, mY = 0;

    public PokeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        trace("onDraw");
        drawSampleTool(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        bitmapBackground = BitmapHelper.loadScaledResource(getResources(), R.drawable.sample_human, right - left, bottom - top);
        bitmapDrawing = Bitmap.createBitmap(bitmapBackground);
        bitmapTool = BitmapFactory.decodeResource(getResources(), R.drawable.sample_tool).copy(Bitmap.Config.ARGB_8888, true);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        mX = event.getX();
        mY = event.getY();
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            trace("touchDown");
            invalidate();
            return true;
        }

        return super.onTouchEvent(event);
    }

    private void drawSampleTool(Canvas canvas) {
        Canvas drawCanvas = new Canvas(bitmapDrawing);
        drawCanvas.drawBitmap(bitmapTool, mX, mY, mPaint);
        canvas.drawBitmap(bitmapDrawing, 0, 0, mPaint);
    }

    private void trace(String message) {
        if(DEBUG)
            Log.d("NotifyView", message);
    }

}
