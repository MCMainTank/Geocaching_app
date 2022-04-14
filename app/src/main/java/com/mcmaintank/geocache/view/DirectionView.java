package com.mcmaintank.geocache.view;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class DirectionView extends View {

    private Paint mRingPaint;

    private Paint mCententPaint; //绘制中心实线的画布

    private Paint mColorPaint;

    /**
     * 圆环半径 根据view的宽度计算
     * */
    private int mRadius = 200;

    /**
     * 圆环的中心点 -- 画圆环和旋转画布时需要使用
     * */
    private int x, y;

    /**
     * 圆环动画使用 -- 与mRingPaint唯一不同得方在于颜色
     * */
    private Paint mRingAnimPaint;

    /**
     * 圆环大小 矩形
     * */
    private RectF mRectf;

    private Context mContext;

    /**
     * 圆环 宽度
     * */
    private final int mHeartPaintWidth = 10;

    /**
     * 圆环动画开始时 画弧的偏移量
     * */
    private int mAnimAngle = -1;

    private Double targetLatitude;

    private Double targetLongitude;

    public DirectionView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

//        this.mContext = context;
//        init();
    }

    public DirectionView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
        this.mContext = context;
        init();
    }

    public DirectionView(Context context)
    {
        this(context, null);
        this.mContext = context;
        init();
    }


    private  void init(){
        mRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRingPaint.setStrokeWidth(mHeartPaintWidth);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingAnimPaint = new Paint(mRingPaint);
        mRingAnimPaint.setColor(Color.WHITE);
        //初始化心跳曲线
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);

        mCententPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mCententPaint.setColor(Color.BLACK);

        mCententPaint.setStrokeWidth(3);
        Log.i(TAG,"Init successful");
    }

    private DrawFilter mDrawFilter;

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

//        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mColorPaint);

        canvas.setDrawFilter(mDrawFilter);//在canvas上抗锯齿
        //由于drawArc默认从x轴开始画，因此需要将画布旋转或者绘制角度旋转，2种方案
        //int level = canvas.save();
        //先绘制竖线
        canvas.drawLine(x,mRectf.top + 30, x  , mRectf.top - 60,mCententPaint);
        //绘制中心线
        canvas.drawLine(x,y-80,x,y + 80,mCententPaint);

        canvas.drawLine(x - 80,y,x + 80, y ,mCententPaint);
        canvas.rotate(rotate, x, y);// 旋转的时候一定要指明中心

        for (int i = 0; i < 360; i += 3) {
            canvas.drawArc(mRectf, i, 1, false, mRingPaint);
        }
        mCententPaint.setTextSize(50);
        mCententPaint.setColor(Color.RED);
        canvas.drawText("N",x,mRectf.top + mHeartPaintWidth  + 50,mCententPaint);
        mCententPaint.setColor(Color.BLACK);
        canvas.rotate(90, x, y);// 旋转的时候一定要指明中心
        canvas.drawText("E",x,mRectf.top + mHeartPaintWidth + 50,mCententPaint);
        canvas.rotate(90, x, y);// 旋转的时候一定要指明中心
        canvas.drawText("S",x,mRectf.top + mHeartPaintWidth + 50,mCententPaint);
        canvas.rotate(90, x, y);// 旋转的时候一定要指明中心
        canvas.drawText("W",x,mRectf.top + mHeartPaintWidth + 50,mCententPaint);
        canvas.rotate(90, x, y);// 旋转的时候一定要指明中心
        mCententPaint.setTextSize(30);
        for (int i = 0; i < 360; i += 3) {
            if(i == 0|| i == 30 || i ==60 || i ==90 || i ==120 || i == 150 || i == 180 || i == 210 || i == 240 || i == 270 || i == 300 || i == 330 || i== 0){
                canvas.drawText(""+i,x,mRectf.top - mHeartPaintWidth ,mCententPaint);
                canvas.rotate(30, x, y);// 旋转的时候一定要指明中心
            }
        }
        Log.i(TAG,"Draw successful");
    }



}
