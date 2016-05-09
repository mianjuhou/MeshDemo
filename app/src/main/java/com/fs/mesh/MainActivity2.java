package com.fs.mesh;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.fs.mesh.widget.CurveView2;

public class MainActivity2 extends AppCompatActivity {

    private CurveView2 mCv;
    private ImageView mIv;
    private Button mBtn_move;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mIv = (ImageView) findViewById(R.id.iv);
        mCv = (CurveView2) findViewById(R.id.cv);
        mBtn_move = (Button) findViewById(R.id.btn_move);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mCv.setVisibility(View.VISIBLE);
                mBtn_move.setDrawingCacheEnabled(true);
                mBtn_move.buildDrawingCache();
                Bitmap drawingCache = mBtn_move.getDrawingCache();
                float left=mBtn_move.getLeft();
                mCv.setBitmap(drawingCache,//
                        mBtn_move.getLeft(),//
                        mBtn_move.getTop(),//
                        mBtn_move.getRight(),//
                        mBtn_move.getBottom(),//
                        mIv.getLeft(),//
                        mIv.getRight());

            }
        });
        mCv.setAnimFinishListener(new CurveView2.AnimFinishListener() {
            @Override
            public void animFinshed() {
                mIv.setImageResource(R.drawable.ljt_have);
            }
        });


    }
}
