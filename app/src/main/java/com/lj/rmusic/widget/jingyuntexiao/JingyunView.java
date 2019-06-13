package com.lj.rmusic.widget.jingyuntexiao;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class JingyunView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    private boolean mIsDrawing;

    private Paint mPaint;

    private int [] mCircleRadius;

    private int maxRadius;

    private int iconRaidus;


    private void initView(Context context, AttributeSet attrs) {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setFocusable(true);
        setKeepScreenOn(true);
        setFocusableInTouchMode(true);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);

        mCircleRadius = new int[3];

        iconRaidus = 330;
    }
    public JingyunView(Context context) {
        this(context, null);
    }

    public JingyunView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JingyunView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //设置为透明
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mIsDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }

    @Override
    public void run() {
        while (mIsDrawing) {
            drawSomething();
        }
    }

    private void drawSomething() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();


            mPaint.setStrokeWidth(5f);

            for (int i = 0; i < mCircleRadius.length; i++) {
                mCanvas.drawCircle(getWidth() / 2, getHeight() / 2, mCircleRadius[i] , mPaint);
            }
        } catch (Exception e) {

        } finally {
            if (mCanvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        maxRadius = Math.min(getWidth() / 2, getHeight() / 2);
        for (int i = 0; i < mCircleRadius.length; i++) {
            mCircleRadius[i] = (maxRadius - iconRaidus) / 3 * i + iconRaidus;
        }
    }
}
