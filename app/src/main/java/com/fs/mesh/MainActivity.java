package com.fs.mesh;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.fs.mesh.widget.CurveView;

public class MainActivity extends AppCompatActivity {

    private CurveView mCv;
    private ImageView mIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIv = (ImageView) findViewById(R.id.iv);
        mCv = (CurveView) findViewById(R.id.cv);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCv.startAnimation();
            }
        });
        mCv.setAnimFinishListener(new CurveView.AnimFinishListener() {
            @Override
            public void animFinshed() {
                mIv.setImageResource(R.drawable.ljt_have);
            }
        });


    }
}
