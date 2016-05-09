package com.fs.beziertool;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mEt1;
    private TextView mEt11;
    private TextView mEt2;
    private TextView mEt22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEt1 = (TextView) findViewById(R.id.et1);
        mEt11 = (TextView) findViewById(R.id.et11);
        mEt2 = (TextView) findViewById(R.id.et2);
        mEt22 = (TextView) findViewById(R.id.et22);

        CubicView cv= (CubicView) findViewById(R.id.cv);
        cv.setPointerChangeListener(new CubicView.PointerChange() {
            @Override
            public void change(float x1, float y1, float x2, float y2) {
                mEt1.setText("x1:"+x1);
                mEt11.setText("y1:"+y1);
                mEt2.setText("x2:"+x2);
                mEt22.setText("y2:"+y2);
            }
        });
    }
}
