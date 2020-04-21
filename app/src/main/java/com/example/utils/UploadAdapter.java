package com.example.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.flowerclassify.R;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UploadAdapter extends BaseAdapter implements View.OnClickListener {
    private List<UploadImg> mContentList;
    private LayoutInflater mInflater;
    private Callback mCallback;
    public UploadAdapter(Context context,  List<UploadImg> objects,Callback callback) {
        mContentList = objects;
        mInflater = LayoutInflater.from(context);
        mCallback = callback;
    }
    public interface Callback {
         public void click(View v);
     }
    @Override
    public int getCount() {
        return mContentList.size();
    }

    @Override
     public Object getItem(int position) {
        return mContentList.get(position);
    }

    @Override
     public long getItemId(int position) {
             return position;
    }
     @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.upload_item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView
                    .findViewById(R.id.upload_imageView);
            holder.textView = (EditText) convertView
                    .findViewById(R.id.upload_name);
            holder.button = (Button) convertView.findViewById(R.id.upload_but);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(mContentList.get(position).getName());
        Bitmap bitmap= BitmapFactory.decodeByteArray(mContentList.get(position).getImg(),0,
                mContentList.get(position).getImg().length);
        holder.imageView.setImageBitmap(bitmap);
        holder.button.setOnClickListener(this);
        holder.button.setTag(position);
        return convertView;
    }

    public class ViewHolder {
		public ImageView imageView;
        public EditText textView;
        public Button button;
    }

    //响应按钮点击事件,调用子定义接口，并传入View
    @Override
    public void onClick(View v) {
        mCallback.click(v);
    }
}