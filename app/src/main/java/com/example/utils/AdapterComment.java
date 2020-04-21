package com.example.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.flowerclassify.R;
import java.util.List;

public class AdapterComment extends BaseAdapter {

    Context context;
    List<Comment> data;

    public AdapterComment(Context c, List<Comment> data){
        this.context = c;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        // 重用convertView
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_comment, null);
            holder.comment_image= (ImageView) convertView.findViewById(R.id.comment_image);
            holder.comment_anthor = (TextView) convertView.findViewById(R.id.comment_anthor);
            holder.comment_content = (TextView) convertView.findViewById(R.id.comment_content);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        // 适配数据
        String author=data.get(i).getAuthor();
        UserDao userdao=new UserDao();
        User user=userdao.Query(author);
        Bitmap bitmap= BitmapFactory.decodeByteArray(user.getHeadimg(),0,user.getHeadimg().length);
        holder.comment_image.setImageBitmap(bitmap);
        holder.comment_anthor.setText(author);
        holder.comment_content.setText(data.get(i).getContent());

        return convertView;
    }

    /**
     * 添加一条评论,刷新列表
     * @param comment
     */
    public void addComment(Comment comment){
        data.add(comment);
        notifyDataSetChanged();
    }

    /**
     * 静态类，便于GC回收
     */
    public static class ViewHolder{
        ImageView comment_image;
        TextView comment_anthor;
        TextView comment_content;
    }
}
