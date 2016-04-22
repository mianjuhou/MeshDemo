package com.fs.mesh.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.fs.mesh.DoublePointerBean;
import com.fs.mesh.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangdean on 2016/4/22.
 */
public class CurveView extends View {

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

    public CurveView(Context context) {
        this(context, null);
    }

    public CurveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CurveView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        mPath1.moveTo(0, 0);
        mPath1.cubicTo(20, 600, 100, 200, 200, 1000);
        mPath2.moveTo(500, 0);
        mPath2.cubicTo(480, 600, 400, 200, 300, 1000);

        mPathMeasure1.setPath(mPath1, false);
        mPathMeasure2.setPath(mPath2, false);

        float path1Length = mPathMeasure1.getLength();
        mPath1Seg1 = path1Length / 50;
        mCurPos1 = new float[2];

        float path2Length = mPathMeasure2.getLength();
        mPath1Seg2 = path2Length / 50;
        mCurPos2 = new float[2];

        pointers=new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            mPathMeasure1.getPosTan(mPath1Seg1 *i, mCurPos1,null);
            mPathMeasure2.getPosTan(mPath1Seg2*i,mCurPos2,null);
            pointers.add(new DoublePointerBean(mCurPos1[0],mCurPos1[1],mCurPos2[0],mCurPos1[1]));
        }
        DoublePointerBean endPointer = pointers.get(pointers.size()-1);
        for (int i = 0; i < 30; i++) {
            pointers.add(new DoublePointerBean(endPointer.x0,endPointer.y0,endPointer.x1,endPointer.y1));
        }

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cat);
        vertNum=(meshWidth+1)*(meshHeight+1)*2;
        verts=new float[vertNum];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(500,1000);
    }
    boolean once=true;
    @Override
    protected void onDraw(Canvas canvas) {
        buildVerts(mStartIndex);
        canvas.drawBitmapMesh(mBitmap,meshWidth,meshHeight,verts,0,null,0,null);
//        canvas.translate(0,-startIndex*20);
//        Bitmap curBitmap = Bitmap.createBitmap(500, 1000, Bitmap.Config.ARGB_8888);
//        canvas.drawBitmap(curBitmap, 0, 0, null);
//        canvas.drawPath(mPath1, mPaint);
//        canvas.drawPath(mPath2, mPaint);
//        mPaint.setColor(Color.BLUE);
//        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setStrokeWidth(2);
//        for (int i = 0; i < 50; i++) {
//            mPathMeasure1.getPosTan(mPath1Seg1 *i, mCurPos1,null);
//            canvas.drawCircle(mCurPos1[0],mCurPos1[1],5,mPaint);
//
//            mPathMeasure2.getPosTan(mPath1Seg2*i,mCurPos2,null);
//            canvas.drawCircle(mCurPos2[0],mCurPos2[1],5,mPaint);
//        }
        if(once&&animation){
            once=false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 50; i++) {
                        SystemClock.sleep(5);
                        mStartIndex++;
                        postInvalidate();
                    }
                }
            }).start();
        }
    }
    boolean animation=false;

    public void startAnimation(){
        animation=true;
        invalidate();
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
