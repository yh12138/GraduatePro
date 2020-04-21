package com.example.flowerclassify;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.utils.Essay;
import com.example.utils.EssayAdapter;
import com.example.utils.EssayDao;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FlowerBlog extends Fragment {
    private List<Essay> essayList = new ArrayList<Essay>();
    private ListView listView;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.flowerblog, container, false);
        return view;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 初始化数据
        EssayDao essaydao=new EssayDao();
        essayList=essaydao.QueryAll();
        EssayAdapter adapter = new EssayAdapter(getActivity(), R.layout.essay_item, essayList);
        listView = (ListView)getActivity().findViewById(R.id.blog_list_view);
        listView.setAdapter(adapter);
        // 为ListView的列表项的单击事件绑定事件监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EssayDetailActivity.class);
                intent.putExtra("essayid", essayList.get(position).getId());
                startActivity(intent);

            }
        });
        //附着在ListView，跟随ListView滚动滑入滑出
        FloatingActionsMenu fab_menu = (FloatingActionsMenu)getActivity().findViewById(R.id.fab_menu);

        fab1=(FloatingActionButton)getActivity().findViewById(R.id.fab_1);
        fab2=(FloatingActionButton)getActivity().findViewById(R.id.fab_2);
        fab1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.smoothScrollToPosition(0);
            }
        } );
        fab2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				Intent intent = new Intent(getActivity(), WriteEssayActivity.class);
                SharedPreferences sharedPreferences= getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
				String phone=sharedPreferences.getString("phone","");
				if(phone.equals(""))
					Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_LONG).show();
                else
                    startActivity(intent);

            }
        } );

    }

    //把图片转换为字节
    private byte[]img(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

}