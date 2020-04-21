package com.example.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import com.example.flowerclassify.R;

public class LeftBaseAdapter extends BaseAdapter {
    Context context;
    List<Map<String, Object>> mainMapList;

    public int getNowSelectedIndex() {
        return nowSelectedIndex;
    }

    public void setNowSelectedIndex(int nowSelectedIndex) {
        this.nowSelectedIndex = nowSelectedIndex;
        this.notifyDataSetChanged();//及时通知显示
    }

    private int nowSelectedIndex = 0;

    public LeftBaseAdapter(Context context, List<Map<String, Object>> mainMapList) {
        this.context = context;
        this.mainMapList = mainMapList;
    }

    @Override
    public int getCount() {
        return mainMapList.size();
    }

    @Override
    public Object getItem(int position) {
        return mainMapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.room_left_listview_item, null);
        }
        TextView tv_list_item = (TextView) convertView.findViewById(R.id.left_tv_list_item);
        tv_list_item.setText(mainMapList.get(position).get("flowerName").toString());

        if (position == nowSelectedIndex) {
            tv_list_item.setBackgroundColor(0xFFFFFFFF);
        } else {
            tv_list_item.setBackgroundColor(0xFFEBEBEB);
        }

        return convertView;
    }
}