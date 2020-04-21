package com.example.flowerclassify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utils.Essay;
import com.example.utils.EssayAdapter;
import com.example.utils.EssayDao;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MyEssayActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView myessay__list_view;
    private FloatingActionButton myessay_fab_1;
    private FloatingActionButton myessay_fab_2;
    private List<Essay> myessays= new ArrayList<Essay>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_essay);

        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        EssayDao essaydao = new EssayDao();
        myessays = essaydao.QueryByAuthor(phone);
        System.out.println(phone+"size:"+myessays.size());
        myessay__list_view = (ListView) findViewById(R.id.myessay__list_view);
        myessay_fab_1 = (FloatingActionButton) findViewById(R.id.myessay_fab_1);
        myessay_fab_2 = (FloatingActionButton) findViewById(R.id.myessay_fab_2);
        EssayAdapter adapter = new EssayAdapter(MyEssayActivity.this, R.layout.essay_item, myessays);
        myessay__list_view.setAdapter(adapter);
        // 为ListView的列表项的单击事件绑定事件监听器
        myessay__list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyEssayActivity.this, MyEssayDetailActivity.class);
                intent.putExtra("myessayid", myessays.get(position).getId());
                startActivity(intent);

            }
        });
        myessay_fab_1.setOnClickListener(this);
        myessay_fab_2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myessay_fab_1:
                myessay__list_view.smoothScrollToPosition(0);
                break;
            case R.id.myessay_fab_2:
                Intent intent = new Intent(MyEssayActivity.this, WriteEssayActivity.class);
                startActivity(intent);
                break;
        }
    }
}
