package com.fs.mv;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTv;
    private MeshView mMv;
    private View mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentView = View.inflate(this, R.layout.activity_main,null);
        setContentView(mContentView);
        getSupportActionBar().hide();

        findViewById(R.id.btn_setting).setOnClickListener(new View.OnClickListener() {

            private View mPopView;
            private PopupWindow mPop;

            @Override
            public void onClick(View v) {
                mPopView = View.inflate(MainActivity.this, R.layout.setting_dialog,null);
                CheckBox cb_fzx= (CheckBox) mPopView.findViewById(R.id.cb_fzx);
                boolean fzx=getSharedPreferences("config",MODE_PRIVATE).getBoolean("fzx",false);
                cb_fzx.setChecked(fzx);
                mPopView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText et_location= (EditText) mPopView.findViewById(R.id.et_location);
                        EditText et_width= (EditText) mPopView.findViewById(R.id.et_width);
                        CheckBox cb_fzx= (CheckBox) mPopView.findViewById(R.id.cb_fzx);
                        String location=et_location.getText().toString().trim();
                        String width=et_width.getText().toString().trim();
                        if(!TextUtils.isEmpty(location)){
                            getSharedPreferences("config",MODE_PRIVATE).edit()
                                    .putString("location",location)
                                    .commit();
                        }
                        if(!TextUtils.isEmpty(width)){
                            getSharedPreferences("config",MODE_PRIVATE).edit()
                                    .putString("width",width)
                                    .commit();
                        }
                        getSharedPreferences("config",MODE_PRIVATE).edit()
                                .putBoolean("fzx",cb_fzx.isChecked())
                                .commit();
                        mPop.dismiss();
                    }
                });
                mPopView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPop.dismiss();
                    }
                });
                mPop = new PopupWindow(mPopView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mPop.setFocusable(true);
                mPop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mPop.showAtLocation(mContentView, Gravity.CENTER,0,0);
            }
        });

        mTv = (TextView) findViewById(R.id.tv);
        mMv = (MeshView) findViewById(R.id.mv);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int left = mTv.getLeft();
                int right = mTv.getRight();
                int top = mTv.getTop();
                int bottom = mTv.getBottom();
                mTv.setDrawingCacheEnabled(true);
                Bitmap drawingCache = mTv.getDrawingCache();

                mTv.setVisibility(View.INVISIBLE);
                mMv.setVisibility(View.VISIBLE);

                String location=getSharedPreferences("config",MODE_PRIVATE).getString("location",null);
                String width=getSharedPreferences("config",MODE_PRIVATE).getString("width",null);
                boolean fzx=getSharedPreferences("config",MODE_PRIVATE).getBoolean("fzx",false);
                if(location==null){
                    location="0.8";
                }
                if(width==null){
                    width="20";
                }
                double dlocation=Double.parseDouble(location);
                float fwidth=Float.parseFloat(width);
                mMv.initView(drawingCache,left,top,right,bottom,dlocation,fwidth,fzx);
            }
        });
    }
}
