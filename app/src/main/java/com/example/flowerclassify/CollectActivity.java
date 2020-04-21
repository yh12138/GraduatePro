package com.example.flowerclassify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utils.Collect;
import com.example.utils.CollectAdapter;
import com.example.utils.CollectDao;
import com.example.utils.Essay;
import com.example.utils.EssayAdapter;
import com.example.utils.EssayDao;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CollectActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView collect_list_view;
    private FloatingActionButton collect_fab_1;
    private FloatingActionButton collect_fab_2;
    private List<Collect> collects= new ArrayList<Collect>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        initView();
    }

    private void initView() {
        collect_list_view = (ListView) findViewById(R.id.collect_list_view);
        collect_fab_1 = (FloatingActionButton) findViewById(R.id.collect_fab_1);
        collect_fab_2 = (FloatingActionButton) findViewById(R.id.collect_fab_2);

        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        collects = new ArrayList<>();
        CollectDao collectdao = new CollectDao();
        collects = collectdao.QueryAll(phone);
        CollectAdapter adapter = new CollectAdapter(CollectActivity.this, R.layout.essay_item, collects);
        collect_list_view.setAdapter(adapter);
        collect_fab_1.setOnClickListener(this);
        collect_fab_2.setOnClickListener(this);
        // 为ListView的列表项的单击事件绑定事件监听器
        collect_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CollectActivity.this, EssayDetailActivity.class);
                EssayDao essaydao=new EssayDao();
                Essay essay=essaydao.QueryById(collects.get(position).getEssayid());
                intent.putExtra("essayid", essay.getId());
                startActivity(intent);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collect_fab_1:
                collect_list_view.smoothScrollToPosition(0);
                break;
            case R.id.collect_fab_2:
                Intent intent = new Intent(CollectActivity.this, WriteEssayActivity.class);
                startActivity(intent);
                break;
        }
    }
}
