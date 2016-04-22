package com.fs.mesh.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.fs.mesh.R;

/**
 * Created by fangdean on 2016/4/22.
 */
public class MeshView extends View{

    private Bitmap mBitmap;

    public MeshView(Context context) {
        this(context,null);
    }

    public MeshView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MeshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cat);
        vertNum=(meshWidth+1)*(meshHeight+1)*2;
        verts=new float[vertNum];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mBitmap.getWidth(),mBitmap.getHeight());
    }

    private int meshWidth=20;
    private int meshHeight=20;
    private int vertNum;
    private float[] verts;
    @Override
    protected void onDraw(Canvas canvas) {
        buildVerts();
        canvas.drawBitmapMesh(mBitmap,meshWidth,meshHeight,verts,0,null,0,null);
    }

    private void buildVerts() {
        int index=0;
        float minWidth=100;
        float dWidth=0;
        for (int i = 0; i <= meshHeight; i++) {
            //三角形
//            float widthLength=mBitmap.getWidth()*1.0f*(meshHeight-i)/(meshHeight*meshWidth);
            //梯形
            float widthLength=(mBitmap.getWidth()-i*(mBitmap.getWidth()-minWidth)*1.0f/meshHeight)/meshHeight;
            float heightLength=mBitmap.getHeight()/meshHeight;
            if(i<meshHeight/2){
                dWidth=i*20;
            }else{
                dWidth=((meshWidth/2-i)+meshWidth/2)*20;
            }
            for (int j = 0; j <= meshWidth; j++) {
                verts[index*2+0]=widthLength*j+dWidth;
                verts[index*2+1]=heightLength*i;
                index++;
            }
        }
    }
}
