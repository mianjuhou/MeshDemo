package com.fs.mesh;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.fs.mesh.widget.CurveView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.btn)
    Button mBtn;
    @Bind(R.id.cv)
    CurveView mCv;
    @Bind(R.id.iv)
    ImageView mIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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
