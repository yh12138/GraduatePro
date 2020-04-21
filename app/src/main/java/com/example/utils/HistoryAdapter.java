package com.example.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.flowerclassify.R;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryAdapter extends ArrayAdapter{
    private final int resourceId;
    public HistoryAdapter(Context context, int textViewResourceId, List<History> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryViewHolder holder;
        History history = (History) getItem(position); // 获取当前项的Collect实例
        View view=LayoutInflater.from(getContext()).inflate(resourceId, null);
        holder = new HistoryViewHolder();
        //获取该布局内的视图
        holder.himg = (ImageView) view.findViewById(R.id.himg);
        holder.hname1 = (TextView) view.findViewById(R.id.hname1);
        holder.hname2 = (TextView) view.findViewById(R.id.hname2);
        holder.hname3 = (TextView) view.findViewById(R.id.hname3);
        Bitmap bitmap= BitmapFactory.decodeByteArray(history.getFlower(),0,history.getFlower().length);
        String [] names=StrToArray(history.getName(),";");
        String [] scores=StrToArray(history.getScore(),";");
        //为视图设置内容
        holder.himg.setImageBitmap(bitmap);
        holder.hname1.setText(names[0]+"("+scores[0]+")");
        holder.hname2.setText(names[1]+"("+scores[1]+")");
        holder.hname3.setText(names[2]+"("+scores[2]+")");


        return view;
    }
    //把日期转为字符串  
    public static String ConverToString(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return df.format(date);
    }
    //把图片转换为字节
    private byte[]img(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    //String转为String数组
    private String[] StrToArray(String str,String s) {
        String[] strs=str.split(s);
        System.out.println(strs.length+"strs："+str.length());
        return strs;
    }
    class HistoryViewHolder {
        ImageView himg;
        TextView hname1;
        TextView hname2;
        TextView hname3;
    }
}
