package com.fs.mesh;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.fs.mesh.widget.CurveView;

public class MainActivity extends AppCompatActivity {

    private CurveView mCv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCv = (CurveView) findViewById(R.id.cv);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCv.startAnimation();
            }
        });
    }
}
