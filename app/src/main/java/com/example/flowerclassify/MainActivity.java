package com.example.flowerclassify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    //声明ViewPager
    private ViewPager mViewPager;
    //适配器
    private FragmentPagerAdapter mAdapter;
    //装载Fragment的集合
    private List<Fragment> mFragments;

    //四个Tab对应的布局
    private LinearLayout mTabRoom;
    private LinearLayout mTabBlog;
    private LinearLayout mTabRecognize;
    private LinearLayout mTabMy;

    //四个Tab对应的ImageButton
    private ImageButton mImgRoom;
    private ImageButton mImgBlog;
    private ImageButton mImgRecognize;
    private ImageButton mImgMy;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String datestr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initViews();//初始化控件
        initEvents();//初始化事件
        initDatas();//初始化数据
        try {
            //步骤1：创建一个SharedPreferences对象
            sharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE);
            //步骤2： 实例化SharedPreferences.Editor对象
            editor = sharedPreferences.edit();
            datestr=sharedPreferences.getString("date","2020-3-5");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date=sdf.parse(datestr);
            //获取当前时间
            Date nowdate = new Date(System.currentTimeMillis());
            String nowstr=ConverToString(date);
            Date nowdate1=sdf.parse(nowstr);
            int day=getGapCount(nowdate1,date);
            if(day>7) {
                editor.clear();
                editor.commit();
            }
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void initDatas() {
        mFragments = new ArrayList<>();
        //将四个Fragment加入集合中
        mFragments.add(new FlowerRoom());
        mFragments.add(new FlowerBlog());
        mFragments.add(new Recognize());
        mFragments.add(new MyFragment());

        //初始化适配器
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {//从集合中获取对应位置的Fragment
                return mFragments.get(position);
            }

            @Override
            public int getCount() {//获取集合中Fragment的总数
                return mFragments.size();
            }

        };
        //不要忘记设置ViewPager的适配器
        mViewPager.setAdapter(mAdapter);
        //设置ViewPager的切换监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            //页面滚动事件
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //页面选中事件
            @Override
            public void onPageSelected(int position) {
                //设置position对应的集合中的Fragment
                mViewPager.setCurrentItem(position);
                resetImgs();
                selectTab(position);
            }

            @Override
            //页面滚动状态改变事件
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initEvents() {
        //设置四个Tab的点击事件
        mTabRoom.setOnClickListener(this);
        mTabBlog.setOnClickListener(this);
        mTabRecognize.setOnClickListener(this);
        mTabMy.setOnClickListener(this);

    }

    //初始化控件
    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        mTabRoom = (LinearLayout) findViewById(R.id.id_tab_room);
        mTabBlog= (LinearLayout) findViewById(R.id.id_tab_blog);
        mTabRecognize = (LinearLayout) findViewById(R.id.id_tab_recognize);
        mTabMy = (LinearLayout) findViewById(R.id.id_tab_my);

        mImgRoom= (ImageButton) findViewById(R.id.id_tab_room_img);
        mImgBlog = (ImageButton) findViewById(R.id.id_tab_blog_img);
        mImgRecognize = (ImageButton) findViewById(R.id.id_tab_recognize_img);
        mImgMy = (ImageButton) findViewById(R.id.id_tab_my_img);

    }

    @Override
    public void onClick(View v) {
        //先将四个ImageButton置为灰色
        resetImgs();

        //根据点击的Tab切换不同的页面及设置对应的ImageButton为绿色
        switch (v.getId()) {
            case R.id.id_tab_room:
                selectTab(0);
				
                break;
            case R.id.id_tab_blog:
                selectTab(1);
                break;
            case R.id.id_tab_recognize:
				sharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE);
				String phone=sharedPreferences.getString("phone","");
				if(phone!="")
					selectTab(2);
				else
					Toast.makeText(MainActivity.this,"请先登录",Toast.LENGTH_LONG).show();
                break;
            case R.id.id_tab_my:
                selectTab(3);
				
                break;
        }
    }

    private void selectTab(int i) {
        //根据点击的Tab设置对应的ImageButton为绿色
        switch (i) {
            case 0:
                mImgRoom.setImageResource(R.mipmap.flowerroom_pressed);
                break;
            case 1:
                mImgBlog.setImageResource(R.mipmap.flowerblog_pressed);
                break;
            case 2:
                mImgRecognize.setImageResource(R.mipmap.recognize_pressed);
                break;
            case 3:
                mImgMy.setImageResource(R.mipmap.my_pressed);
                break;
        }
        //设置当前点击的Tab所对应的页面
        mViewPager.setCurrentItem(i);
    }

    //将四个ImageButton设置为灰色
    private void resetImgs() {
        mImgRoom.setImageResource(R.mipmap.flowerroom_normal);
        mImgBlog.setImageResource(R.mipmap.flowerblog_normal);
        mImgRecognize.setImageResource(R.mipmap.recognize_normal);
        mImgMy.setImageResource(R.mipmap.my_normal);
    }
    //把日期转为字符串  
    public static String ConverToString(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        return df.format(date);
    }
    //把字符串转为日期  
    public static Date  ConverToDate(String strDate) throws Exception{
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.parse(strDate);
    }
    /**
         * 获取两个日期之间的间隔天数
         * @return
         */
    public static int getGapCount(Date startDate, Date endDate) {
         Calendar fromCalendar = Calendar.getInstance();
         fromCalendar.setTime(startDate);fromCalendar.set(Calendar.HOUR_OF_DAY, 0);fromCalendar.set(Calendar.MINUTE, 0);
         fromCalendar.set(Calendar.SECOND, 0);
         fromCalendar.set(Calendar.MILLISECOND, 0);

         Calendar toCalendar = Calendar.getInstance();
         toCalendar.setTime(endDate);
         toCalendar.set(Calendar.HOUR_OF_DAY, 0);
         toCalendar.set(Calendar.MINUTE, 0);
         toCalendar.set(Calendar.SECOND, 0);
         toCalendar.set(Calendar.MILLISECOND, 0);

         return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
}

}