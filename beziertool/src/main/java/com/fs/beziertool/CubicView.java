package com.fs.beziertool;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by fangdean on 2016/5/6.
 */
public class CubicView extends View{

    private float mStartX;
    private float mStartY;
    private float mEndX;
    private float mEndY;
    private float mX1;
    private float mY1;
    private float mX2;
    private float mY2;
    private float mDx;
    private float mDy;

    public CubicView(Context context) {
        this(context,null);
    }

    public CubicView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CubicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint=new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
        mPaint.setAntiAlias(true);
        mPath=new Path();
    }
    private boolean order=true;
    private Paint mPaint;
    private Path mPath;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        mStartX = measuredWidth*1.0f/4;
        mStartY = measuredHeight*1.0f/4;

        mEndX = mStartX *3;
        mEndY = mStartY *3;

        if(order){

        }else{

        }

        mX1 = mStartX+(mEndX-mStartX)*0.1f;
        mY1 = mStartY+(mEndY-mStartY)*0.9f;

        mX2 = mStartX+(mEndX-mStartX)*0.9f;
        mY2 = mStartY+(mEndY-mStartY)*0.1f;

        mDx = mEndX-mStartX;
        mDy = mEndY-mStartY;

        mPointerChange.change(0.1f,0.9f,0.9f,0.1f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.RED);
        canvas.drawRect(mStartX,mStartY,mEndX,mEndY,mPaint);

        mPath.reset();
        mPath.moveTo(mStartX,mStartY);
        mPath.cubicTo(mX1,mY1,mX2,mY2,mEndX,mEndY);
        canvas.drawPath(mPath,mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLUE);
        canvas.drawCircle(mX1,mY1,20,mPaint);
        canvas.drawCircle(mX2,mY2,20,mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(mStartX,mStartY,mX1,mY1,mPaint);
        canvas.drawLine(mX1,mY1,mX2,mY2,mPaint);
        canvas.drawLine(mX2,mY2,mEndX,mEndY,mPaint);
    }
    private int capture=0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                if((Math.abs(x-mX1)<20)&&(Math.abs(y-mY1)<20)){
                    capture=1;
                }else if((Math.abs(x-mX2)<20)&&(Math.abs(y-mY2)<20)){
                    capture=2;
                }else{
                    capture=0;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float curX = event.getX();
                float curY = event.getY();
                if(capture==1){
                    mX1=curX;
                    mY1=curY;
                    invalidate();
                }else if(capture==2){
                    mX2=curX;
                    mY2=curY;
                    invalidate();
                }
                if(capture!=0){
                    if(mPointerChange!=null){
                        float percentX1=(mX1-mStartX)/mDx;
                        float percentY1=(mY1-mStartY)/mDy;
                        float percentX2=(mX2-mStartX)/mDx;
                        float percentY2=(mY2-mStartY)/mDy;
                        mPointerChange.change(percentX1,percentY1,percentX2,percentY2);
                    }
                }
                break;
        }
        return true;
    }
    public interface PointerChange{
        void change(float x1,float y1,float x2,float y2);
    }
    private PointerChange mPointerChange;
    public void setPointerChangeListener(PointerChange listener){
        mPointerChange=listener;
    }
}
