package com.example.flowerclassify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utils.Collect;
import com.example.utils.CollectAdapter;
import com.example.utils.CollectDao;
import com.example.utils.History;
import com.example.utils.HistoryAdapter;
import com.example.utils.HistoryDao;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class classify_historyActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView history_list_view;
    private FloatingActionButton history_fab_1;
    private List<History> historys= new ArrayList<History>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_history);
        initView();
    }

    private void initView() {
        history_list_view = (ListView) findViewById(R.id.history_list_view);
        history_fab_1 = (FloatingActionButton) findViewById(R.id.history_fab_1);
        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        historys = new ArrayList<>();
        HistoryDao historydao = new HistoryDao();
        historys = historydao.QueryByAuthor(phone);
        HistoryAdapter adapter = new HistoryAdapter(classify_historyActivity.this, R.layout.history_item, historys);
        history_list_view.setAdapter(adapter);
        history_fab_1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.history_fab_1:
                history_list_view.smoothScrollToPosition(0);
                break;
        }
    }
}
