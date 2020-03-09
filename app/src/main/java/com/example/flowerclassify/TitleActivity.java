package com.example.flowerclassify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public abstract  class TitleActivity extends  AppCompatActivity implements View.OnClickListener {

    /**
     * 标题栏
     */
    private TextView mTitle;
    /**
     * < 返回
     */
    private Button mBackward;
    /**
     * 提交
     */
    private Button mForward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();

    }

    public abstract int getLayoutId();
    //初始化组件
    private void initView() {
        mTitle = (TextView) findViewById(R.id.text_title);
        mBackward = (Button) findViewById(R.id.button_backward);
        mBackward.setOnClickListener(this);
    }


    //返回按钮和提交按钮的点击判断监听事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.button_backward:
				finish();
                break;
        }
    }
}
