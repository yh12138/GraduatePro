package com.example.utils;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.flowerclassify.R;
/*第二步创建TitleLayout类 */
public class TitleLayout extends LinearLayout {
    public TitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //布局填充器--从 传入的上下文参数--进行填充
        LayoutInflater.from(context).inflate(R.layout.title,this);

        Button titleBack=(Button)findViewById(R.id.button_backward);
        titleBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)getContext()).finish();
            }
        });

    }
}