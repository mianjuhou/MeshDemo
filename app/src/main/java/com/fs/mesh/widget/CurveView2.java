package com.fs.mesh.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.fs.mesh.DoublePointerBean;

import java.util.List;

/**
 * Created by fangdean on 2016/4/22.
 */
public class CurveView2 extends View {

    private Paint mPaint;
    private Path mPath1;
    private Path mPath2;
    private PathMeasure mPathMeasure1 = new PathMeasure();
    private PathMeasure mPathMeasure2 = new PathMeasure();
    private float[] mCurrentPosition1 = new float[2];
    private float[] mCurrentPosition2 = new float[2];
    private float[] mCurPos1;
    private float mPath1Seg1;
    private float mPath1Seg2;
    private float[] mCurPos2;
    private int mStartIndex=0;

    public CurveView2(Context context) {
        this(context, null);
    }

    public CurveView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CurveView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private List<DoublePointerBean> pointers;
    private Bitmap mBitmap;
    private int meshWidth=20;
    private int meshHeight=20;
    private int vertNum;
    private float[] verts;
    private void init() {
        mPath1 = new Path();
        mPath2 = new Path();
        mPaint = new Paint();

        mPaint.setStrokeWidth(1);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);

        vertNum=(meshWidth+1)*(meshHeight+1)*2;
        verts=new float[vertNum];
    }

    public void setBitmap(Bitmap bitmap,float left,float top,float right,float bottom,float endLeft,float endRight){
        mBitmap=bitmap;
        if(mBitmap!=null){
            int measuredHeight = getMeasuredHeight();
            float width1=endLeft-left;
            float width2=endRight-right;
            mPath1.moveTo(left, top);
            mPath1.lineTo(left,bottom);
            mPath1.cubicTo(20, 600, 100, 200, endLeft, measuredHeight);
            mPath2.moveTo(right, top);
            mPath2.lineTo(right,bottom);
            mPath2.cubicTo(right+width2*0.91f, measuredHeight*0.36f, right+width2*0.55f, measuredHeight*0.68f, endRight, measuredHeight);
//            mPath2.quadTo(right,600,endRight,measuredHeight);

//            mPathMeasure1.setPath(mPath1, false);
//            mPathMeasure2.setPath(mPath2, false);
//
//            float path1Length = mPathMeasure1.getLength();
//            mPath1Seg1 = path1Length / 50;
//            mCurPos1 = new float[2];
//
//            float path2Length = mPathMeasure2.getLength();
//            mPath1Seg2 = path2Length / 50;
//            mCurPos2 = new float[2];
//
//            pointers=new ArrayList<>();
//            for (int i = 0; i < 50; i++) {
//                mPathMeasure1.getPosTan(mPath1Seg1 *i, mCurPos1,null);
//                mPathMeasure2.getPosTan(mPath1Seg2*i,mCurPos2,null);
//                pointers.add(new DoublePointerBean(mCurPos1[0],mCurPos1[1],mCurPos2[0],mCurPos1[1]));
//            }
//            DoublePointerBean endPointer = pointers.get(pointers.size()-1);
//            for (int i = 0; i < 30; i++) {
//                pointers.add(new DoublePointerBean(endPointer.x0,endPointer.y0,endPointer.x1,endPointer.y1));
//            }

            invalidate();
        }
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        setMeasuredDimension(500,1000);
//    }
    boolean once=true;
    @Override
    protected void onDraw(Canvas canvas) {
        if(mBitmap!=null){
//            buildVerts(mStartIndex);
//            canvas.drawBitmapMesh(mBitmap,meshWidth,meshHeight,verts,0,null,0,null);
//            if(once&&animation){
//                once=false;
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        for (int i = 0; i < 50; i++) {
//                            SystemClock.sleep(5);
//                            mStartIndex++;
//                            postInvalidate();
//                        }
//                        animFinish=true;
//                    }
//                }).start();
//            }
//            if(animFinish){
//                if(listener!=null){
//                    listener.animFinshed();
//                }
//            }
            canvas.drawPath(mPath1,mPaint);
            canvas.drawPath(mPath2,mPaint);
        }
    }
    boolean animation=false;
    boolean animFinish=false;
    public void startAnimation(){
        animation=true;
        invalidate();
    }
    public interface AnimFinishListener{
        void animFinshed();
    }
    private AnimFinishListener listener;
    public void setAnimFinishListener(AnimFinishListener listener){
        this.listener=listener;
    }

    private void buildVerts(int startIndex) {
        int index=0;
        float minWidth=100;
        float dWidth=0;
        for (int i = 0; i <= meshHeight; i++) {
            DoublePointerBean pointer = pointers.get(startIndex+i);
            float widthLength=(pointer.x1-pointer.x0)/meshWidth;
            float heightLength=20;
            for (int j = 0; j <= meshWidth; j++) {
                verts[index*2+0]=widthLength*j+pointer.x0;
                verts[index*2+1]=heightLength*i+startIndex*20;
                index++;
            }
        }
    }



    class MyRunnable implements Runnable{
        Bitmap mBitmap;

        public MyRunnable(Bitmap bitmap) {
            mBitmap = bitmap;
        }
        @Override
        public void run() {
            for (int i = 0; i < 50; i++) {
                int x = 20 * i;
                int pos1=-1;
                int pos2=-1;
                for (int j = 0; j < 500; j++) {
                    int pixel = mBitmap.getPixel(j, x);
                    if(Color.TRANSPARENT!=pixel){
                        if(pos1==-1){
                            pos1=j;
                        }else{
                            pos2=j;
                            break;
                        }
                    }
                }
                Log.i("Tag","("+pos1+","+x+")--("+pos2+","+x+")");
            }
        }
    }
}
