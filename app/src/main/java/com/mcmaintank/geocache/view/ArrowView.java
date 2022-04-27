package com.mcmaintank.geocache.view;

import static android.content.ContentValues.TAG;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;


public class ArrowView extends View {

    private Paint mArrowPaint;

    private Paint mArrowAnimPaint;

    private Context mContext;

    private Paint mCententPaint;

    private int x, y;

    private int mRadius = 200;

    private RectF mRectf;

    private final int mHeartPaintWidth = 10;

    private float curPos;


    public ArrowView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

//        this.mContext = context;
//        init();
    }

    public ArrowView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
        this.mContext = context;
        init();
    }

    public ArrowView(Context context)
    {
        this(context, null);
        this.mContext = context;
        init();
    }

    private DrawFilter mDrawFilter;

    private void init(){
        mArrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArrowPaint.setStrokeWidth(mHeartPaintWidth);
        mArrowPaint.setStyle(Paint.Style.STROKE);
        mArrowAnimPaint = new Paint(mArrowPaint);
        mArrowAnimPaint.setColor(Color.WHITE);
        mCententPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mCententPaint.setColor(Color.BLUE);

        mCententPaint.setStrokeWidth(6);
        Log.i(TAG,"Init successful");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        x = w / 2;
        y = h / 2;
        mRadius = w / 2 - mHeartPaintWidth * 3; //因为制定了Paint的宽度，因此计算半径需要减去这个
        mRectf = new RectF(x - mRadius, y - mRadius, x + mRadius, y + mRadius);
    }
    public float rotate = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPoint(x,y,mCententPaint);
        canvas.setDrawFilter(mDrawFilter);
        canvas.rotate(rotate,x,y);
        canvas.drawLine(x,mRectf.top + 30, x  , mRectf.top - 30,mCententPaint);
        curPos = 0;
        mCententPaint.setTextSize(50);
        Log.i(TAG,"Draw successful");
    }

    @Override
    public void setRotation(float rotation){
        @SuppressLint("ObjectAnimatorBinding")
        ObjectAnimator animator = ObjectAnimator.ofFloat(mArrowPaint,"rotation",curPos,rotation);
        curPos = rotation;
        animator.setDuration(100);
        animator.start();
    }


}
