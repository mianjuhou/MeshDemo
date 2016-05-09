package com.fs.mv;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangdean on 2016/5/6.
 */
public class MeshView extends View{

    private int mMeasuredWidth;
    private int mMeasuredHeight;
    private PathMeasure mPathMeasure;
    private int mMeshHeight;
    private int mMeshWidth;
    private int mSegTimes;
    private float mEndWidth;

    public MeshView(Context context) {
        this(context,null);
    }

    public MeshView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MeshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPath=new Path();
        mPaint=new Paint();
        mPathMeasure=new PathMeasure();
    }
    private Bitmap mBitmap;
    private int mLeft,mTop,mRight,mBottom;
    private double mPercent;
    private Path mPath;
    private Paint mPaint;
    private boolean mFzx;
    public void initView(Bitmap bitmap,int left,int top,int right,int bottom,double percent,float endWidth,boolean fzx){
        mBitmap=bitmap;
        mLeft=left;
        mTop=top;
        mRight=right;
        mBottom=bottom;
        mPercent=percent;
        mEndWidth=endWidth;
        mFzx=fzx;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasuredWidth = getMeasuredWidth();
        mMeasuredHeight = getMeasuredHeight();

        mMeshHeight =10;
        mMeshWidth = 20;
        float segHeight=(mBottom-mTop)/mMeshHeight;

        float startX=(mLeft+mRight)/2;
        float startY=mBottom;
        float endX=(float) (mMeasuredWidth*mPercent);
        float endY=mMeasuredHeight;

        mPath.reset();
        mPath.moveTo((mLeft+mRight)/2,mTop);
        mPath.lineTo(startX,startY);
        mPath.cubicTo(
                (float) (startX+(endX-startX)*0.12),//
                (float) (startY+(endY-startY)*0.50),//
                (float) (startX+(endX-startX)*0.88),//
                (float) (startY+(endY-startY)*0.50),//
                endX,//
                endY);

        mPathMeasure.setPath(mPath,false);
        mSegTimes = (int) Math.ceil(mMeasuredHeight*1.0/segHeight);

        float segLength=mPathMeasure.getLength()/ mSegTimes;
        float segWidth=(mRight-mLeft- mEndWidth)/ (mSegTimes-mMeshHeight)/2;
        pointers=new ArrayList<DoublePointerBean>();
        float[] pos=new float[2];
        int index=0;
        float dw=0;
        for (int i = 0; i < mSegTimes+mMeshHeight+1; i++) {
            mPathMeasure.getPosTan(segLength*i,pos,null);
            if(pos[1]<=mBottom){
                pointers.add(new DoublePointerBean(mLeft,pos[1],mRight,pos[1]));
            }else if(pos[1]<=mMeasuredHeight){
                dw=(mRight-mLeft)/2-segWidth*index;
                pointers.add(new DoublePointerBean(//
                        (pos[0]-dw),//
                        pos[1],//
                        (pos[0]+dw),//
                        pos[1]));
                index++;
            }else{
                pointers.add(new DoublePointerBean(//
                        (pos[0]-dw),//
                        pos[1],//
                        (pos[0]+dw),//
                        pos[1]));
            }
        }

        int vertNum=(mMeshWidth+1)*(mMeshHeight+1)*2;
        verts=new float[vertNum];
    }
    private List<DoublePointerBean> pointers;
    private float[] verts;
    @Override
    protected void onDraw(Canvas canvas) {
        if(mBitmap!=null){
            buildVerts(mStartIndex);
            canvas.drawBitmapMesh(mBitmap,mMeshWidth,mMeshHeight,verts,0,null,0,null);

            if(mFzx){
                mPaint.setColor(Color.RED);
                mPaint.setStrokeWidth(2);
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawPath(mPath,mPaint);

                mPaint.setStrokeWidth(1);
                mPaint.setStyle(Paint.Style.FILL);
                for (int i = 0; i < pointers.size(); i++) {
                    DoublePointerBean pointer = pointers.get(i);
                    canvas.drawCircle(pointer.x0,pointer.y0,2,mPaint);
                    canvas.drawCircle(pointer.x1,pointer.y1,2,mPaint);
                }
            }

            if(once){
                once=false;
                ValueAnimator valueAnimator=ValueAnimator.ofInt(1,mSegTimes);
                valueAnimator.setDuration(500);
                valueAnimator.setInterpolator(new AccelerateInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    private int mCurIndex;
                    private int mOldCurIndex=-1;
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mCurIndex = (int)animation.getAnimatedValue();
                        if(mCurIndex!=mOldCurIndex){
                            mOldCurIndex=mCurIndex;
                            mStartIndex=mCurIndex;
                            postInvalidate();
                        }
                    }
                });
                valueAnimator.start();
            }
        }
    }
    private boolean once=true;
    private int mStartIndex=0;
    private void buildVerts(int startIndex) {
        int index=0;
        for (int i = 0; i <= mMeshHeight; i++) {
            DoublePointerBean pointer = pointers.get(startIndex+i);
            float widthLength=(pointer.x1-pointer.x0)/mMeshWidth;
            float heightLength=(mBottom-mTop)/mMeshHeight;
            for (int j = 0; j <= mMeshWidth; j++) {
                verts[index*2+0]=widthLength*j+pointer.x0;
                verts[index*2+1]=pointer.y0;
                index++;
            }
        }
    }

    public static class PathAnimatioin extends Animation {
        private int curIndex;
        private int oldCurIndex=-1;
        private int mFromIndex;
        private int mEndIndex;
        public PathAnimatioin(int fromIndex,int endIndex,IAnimationUpdateListener listener){
            mFromIndex=fromIndex;
            mEndIndex=endIndex;
            mListener=listener;
        }
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            curIndex=(int)(mFromIndex+(mEndIndex-mFromIndex)*interpolatedTime);
            if(curIndex!=oldCurIndex){
                if(mListener!=null){
                    mListener.onAnimUpdate(curIndex);
                }
                oldCurIndex=curIndex;
            }
        }
        private IAnimationUpdateListener mListener;
        public interface IAnimationUpdateListener{
            void onAnimUpdate(int index);
        }
    }
}
