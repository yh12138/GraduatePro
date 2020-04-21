package com.example.flowerclassify;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.utils.AdapterComment;
import com.example.utils.Collect;
import com.example.utils.CollectDao;
import com.example.utils.Comment;
import com.example.utils.CommentDao;
import com.example.utils.Essay;
import com.example.utils.EssayDao;
import com.example.utils.User;
import com.example.utils.UserDao;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EssayDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView essay_detail_title;
    private TextView essay_detail_content;
    private TextView essay_detail_anthor;
    private TextView essay_detail_date;
    private TextView essay_detail_commentCount;
    private TextView essay_detail_collectCount;
    private LinearLayout l_essay_detail;

    private ListView comment_list;
    private ImageView comment;
    private ImageView chat;
    private LinearLayout rl_enroll;
    private TextView hide_down;
    private EditText comment_content;
    private Button comment_send;
    private RelativeLayout rl_comment;

    private AdapterComment adapterComment;
    private List<Comment> data;
    private SharedPreferences sharedPreferences;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private Essay essay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_essay_detail);
        //应用前要隐藏原有的Title
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        initView();
    }

    private void initView() {
        // 初始化评论列表
        comment_list = (ListView) findViewById(R.id.comment_list);

        CommentDao commentDao=new CommentDao();
        Intent intent=getIntent();
        int essayid=intent.getIntExtra("essayid",-1);
        System.out.println("id:"+essayid);
        EssayDao essaydao=new EssayDao();
        essay=essaydao.QueryById(essayid);
        data = commentDao.QueryAll(essayid);


        // 初始化适配器
        adapterComment = new AdapterComment(EssayDetailActivity.this, data);
        comment_list.setAdapter(adapterComment);
        comment = (ImageView) findViewById(R.id.comment);
        hide_down = (TextView) findViewById(R.id.hide_down);
        comment_content = (EditText) findViewById(R.id.comment_content);
        comment_send = (Button) findViewById(R.id.comment_send);

        chat = (ImageView) findViewById(R.id.chat);
        rl_enroll = (LinearLayout) findViewById(R.id.rl_enroll);
        rl_comment = (RelativeLayout) findViewById(R.id.rl_comment);

        essay_detail_title = (TextView) findViewById(R.id.essay_detail_title);
        essay_detail_content = (TextView) findViewById(R.id.essay_detail_content);
        essay_detail_anthor = (TextView) findViewById(R.id.essay_detail_anthor);
        essay_detail_date = (TextView) findViewById(R.id.essay_detail_date);
        essay_detail_commentCount = (TextView) findViewById(R.id.essay_detail_commentCount);
        essay_detail_collectCount = (TextView) findViewById(R.id.essay_detail_collectCount);

        essay_detail_title.setText(essay.getTitle());
        essay_detail_content.setText("\u3000\u3000"+essay.getContent());
        essay_detail_anthor.setText(essay.getAuthor());
        essay_detail_date.setText(ConverToString(essay.getDate()));
        essay_detail_commentCount.setText("评论("+Integer.toString(essay.getCommentCount())+")");
        essay_detail_collectCount.setText("收藏("+Integer.toString(essay.getCollectCount())+")");

        comment.setOnClickListener(this);
        hide_down.setOnClickListener(this);
        comment_send.setOnClickListener(this);

        fab1=(FloatingActionButton)findViewById(R.id.dfab_1);
        fab2=(FloatingActionButton)findViewById(R.id.dfab_2);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener( this );


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment:
                // 弹出输入法
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                // 显示评论框
                rl_enroll.setVisibility(View.GONE);
                rl_comment.setVisibility(View.VISIBLE);
                break;
            case R.id.hide_down:
                // 隐藏评论框
                rl_enroll.setVisibility(View.VISIBLE);
                rl_comment.setVisibility(View.GONE);
                // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
                InputMethodManager im = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(comment_content.getWindowToken(), 0);
                break;
            case R.id.comment_send:
                sharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE);
                String phone=sharedPreferences.getString("phone","");
                if(phone!="")
                    sendComment();
                else
                    Toast.makeText(EssayDetailActivity.this,"请先登录",Toast.LENGTH_LONG).show();

                break;
            case R.id.dfab_1:
                comment_list.smoothScrollToPosition(0);
                break;
            case R.id.dfab_2:

                sharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE);
                String phone1=sharedPreferences.getString("phone","");
                if(phone1.equals(""))
                    Toast.makeText(EssayDetailActivity.this,"请先登录",Toast.LENGTH_LONG).show();
                else {
                    essay.setCollectCount(essay.getCollectCount()+1);
                    CollectDao collectdao=new CollectDao();
                    Collect collect = new Collect(phone1,new Date(),essay.getId());
                    boolean f=collectdao.Insert(collect);
                    if(f==true)
                        Toast.makeText(EssayDetailActivity.this,"收藏成功",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(EssayDetailActivity.this,"收藏失败",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 发送评论
     */
    public void sendComment(){
        if(comment_content.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "评论不能为空！", Toast.LENGTH_SHORT).show();
        }else{
            // 生成评论数据
            Comment comment = new Comment();
            sharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE);
            String phone=sharedPreferences.getString("phone","");

            Intent intent=getIntent();
            int essayid=intent.getIntExtra("essayid",-1);
            System.out.println("phone:"+phone);
            comment.setAuthor(phone);
            comment.setContent(comment_content.getText().toString());
            comment.setDate(new Date());
            comment.setEssayid(essayid);
            CommentDao commentDao=new CommentDao();
            commentDao.Insert(comment);
            essay.setCommentCount(essay.getCommentCount()+1);
            adapterComment.addComment(comment);
            // 发送完，清空输入框
            comment_content.setText("");


            Toast.makeText(getApplicationContext(), "评论成功！", Toast.LENGTH_SHORT).show();
        }
    }
    //把日期转为字符串  
    public static String ConverToString(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return df.format(date);
    }
}
