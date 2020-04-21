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

public class CollectAdapter extends ArrayAdapter{
    private final int resourceId;
    public static final int TYPE = 0;
    public static final int TYPE1 = 1;
    public CollectAdapter(Context context, int textViewResourceId, List<Collect> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EssayViewHolder holder;
        EssayViewHolder1 holder1;
        Collect collect = (Collect) getItem(position); // 获取当前项的Collect实例
        EssayDao essaydao=new EssayDao();
        Essay essay=essaydao.QueryById(collect.getEssayid());
        View view=LayoutInflater.from(getContext()).inflate(R.layout.essay_item, null);
        switch (essay.getModel()) {
            case TYPE:
                view = LayoutInflater.from(getContext()).inflate(R.layout.essay_item, null);//实例化一个对象
                holder = new EssayViewHolder();
                //获取该布局内的视图
                holder.essay_title = (TextView) view.findViewById(R.id.essay_title);
                holder.essay_anthor = (TextView) view.findViewById(R.id.essay_anthor);
                holder.essay_commentCount = (TextView) view.findViewById(R.id.essay_commentCount);
                holder.essay_date = (TextView) view.findViewById(R.id.essay_date);

                //为视图设置内容
                holder.essay_title.setText(essay.getTitle());
                holder.essay_anthor.setText(essay.getAuthor());
                holder.essay_commentCount.setText("评论("+Integer.toString(essay.getCommentCount())+")");
                holder.essay_date.setText(ConverToString(essay.getDate()));

                break;
            case TYPE1:
                view = LayoutInflater.from(getContext()).inflate(R.layout.essay_item1, null);//实例化一个对象
                holder1 = new EssayViewHolder1();
                //获取该布局内的视图
                holder1.essay_title1 = (TextView) view.findViewById(R.id.essay_title1);
                holder1.essay_anthor1 = (TextView) view.findViewById(R.id.essay_anthor1);
                holder1.essay_commentCount1 = (TextView) view.findViewById(R.id.essay_commentCount1);
                holder1.essay_date1 = (TextView) view.findViewById(R.id.essay_date1);
                holder1.image = (ImageView) view.findViewById(R.id.essay_image);
                //为视图设置内
                holder1.essay_title1.setText(essay.getTitle());
                holder1.essay_anthor1.setText(essay.getAuthor());
                holder1.essay_commentCount1.setText("评论("+Integer.toString(essay.getCommentCount())+")");
                holder1.essay_date1.setText(ConverToString(essay.getDate()));
                Bitmap bitmap= BitmapFactory.decodeByteArray(essay.getImage(),0,essay.getImage().length);
                holder1.image.setImageBitmap(bitmap);

                break;
        }


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
    class EssayViewHolder {
        TextView essay_title;
        TextView essay_anthor;
        TextView essay_commentCount ;
        TextView essay_date;
    }

    class EssayViewHolder1 {
        TextView essay_title1;
        TextView essay_anthor1;
        TextView essay_commentCount1;
        TextView essay_date1;
        ImageView image;
    }
}
