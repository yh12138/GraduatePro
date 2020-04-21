package com.example.flowerclassify;

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
import com.example.utils.Comment;
import com.example.utils.CommentDao;
import com.example.utils.Essay;
import com.example.utils.EssayDao;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyEssayDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView myessayd_title;
    private TextView myessayd_content;
    private TextView myessayd_anthor;
    private TextView myessayd_date;
    private TextView myessayd_commentCount;
    private TextView myessayd_collectCount;
    private LinearLayout myessayd_l_detail;
    private ListView myessayd_comment_list;
    private FloatingActionButton myessayd_fab_1;
    private ImageView myessayd_comment;
    private ImageView myessayd_chat;
    private LinearLayout myessayd_enroll;
    private TextView myessayd_hide_down;
    private EditText myessayd_comment_content;
    private Button myessayd_comment_send;
    private RelativeLayout myessayd_rl_comment;
    private RelativeLayout myessayd_fab;
    private Essay essay;
    private List<Comment> data;
    private AdapterComment adapterComment;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_essay_detail);
        //应用前要隐藏原有的Title
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        initView();
    }

    private void initView() {
        myessayd_title = (TextView) findViewById(R.id.myessayd_title);
        myessayd_content = (TextView) findViewById(R.id.myessayd_content);
        myessayd_anthor = (TextView) findViewById(R.id.myessayd_anthor);
        myessayd_date = (TextView) findViewById(R.id.myessayd_date);
        myessayd_commentCount = (TextView) findViewById(R.id.myessayd_commentCount);
        myessayd_collectCount = (TextView) findViewById(R.id.myessayd_collectCount);
        myessayd_l_detail = (LinearLayout) findViewById(R.id.myessayd_l_detail);
        myessayd_comment_list = (ListView) findViewById(R.id.myessayd_comment_list);
        myessayd_fab_1 = (FloatingActionButton) findViewById(R.id.myessayd_fab_1);
        myessayd_comment = (ImageView) findViewById(R.id.myessayd_comment);
        myessayd_chat = (ImageView) findViewById(R.id.myessayd_chat);
        myessayd_enroll = (LinearLayout) findViewById(R.id.myessayd_enroll);
        myessayd_hide_down = (TextView) findViewById(R.id.myessayd_hide_down);
        myessayd_comment_content = (EditText) findViewById(R.id.myessayd_comment_content);
        myessayd_comment_send = (Button) findViewById(R.id.myessayd_comment_send);
        myessayd_rl_comment = (RelativeLayout) findViewById(R.id.myessayd_rl_comment);
        myessayd_fab = (RelativeLayout) findViewById(R.id.myessayd_fab);



        CommentDao commentDao=new CommentDao();
        Intent intent=getIntent();
        int essayid=intent.getIntExtra("myessayid",-1);
        System.out.println("id:"+essayid);
        EssayDao essaydao=new EssayDao();
        essay=essaydao.QueryById(essayid);
        data = commentDao.QueryAll(essayid);
        // 初始化适配器
        adapterComment = new AdapterComment(MyEssayDetailActivity.this, data);
        myessayd_comment_list.setAdapter(adapterComment);

        myessayd_title.setText(essay.getTitle());
        myessayd_content.setText("\u3000\u3000"+essay.getContent());
        myessayd_anthor.setText(essay.getAuthor());
        myessayd_date.setText(ConverToString(essay.getDate()));
        myessayd_commentCount.setText("评论("+Integer.toString(essay.getCommentCount())+")");
        myessayd_collectCount.setText("收藏("+Integer.toString(essay.getCollectCount())+")");

        myessayd_comment.setOnClickListener(this);
        myessayd_hide_down.setOnClickListener(this);
        myessayd_fab_1.setOnClickListener(this);
        myessayd_comment_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myessayd_comment:
                // 弹出输入法
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                // 显示评论框
                myessayd_enroll.setVisibility(View.GONE);
                myessayd_rl_comment.setVisibility(View.VISIBLE);
                break;
            case R.id.myessayd_hide_down:
                // 隐藏评论框
                myessayd_enroll.setVisibility(View.VISIBLE);
                myessayd_rl_comment.setVisibility(View.GONE);
                // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
                InputMethodManager im = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(myessayd_comment_content.getWindowToken(), 0);
                break;
            case R.id.myessayd_comment_send:
                sharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE);
                String phone=sharedPreferences.getString("phone","");
                if(phone!="")
                    sendComment();
                else
                    Toast.makeText(MyEssayDetailActivity.this,"请先登录",Toast.LENGTH_LONG).show();

                break;
            case R.id.myessayd_fab_1:
                myessayd_comment_list.smoothScrollToPosition(0);
                break;
        }
    }

    /**
     * 发送评论
     */
    public void sendComment(){
        if(myessayd_comment_content.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "评论不能为空！", Toast.LENGTH_SHORT).show();
        }else{
            // 生成评论数据
            Comment comment = new Comment();
            sharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE);
            String phone=sharedPreferences.getString("phone","");

            Intent intent=getIntent();
            int essayid=intent.getIntExtra("myessayid",-1);
            System.out.println("phone:"+phone);
            comment.setAuthor(phone);
            comment.setContent(myessayd_comment_content.getText().toString());
            comment.setDate(new Date());
            comment.setEssayid(essayid);
            CommentDao commentDao=new CommentDao();
            commentDao.Insert(comment);
            essay.setCommentCount(essay.getCommentCount()+1);
            adapterComment.addComment(comment);
            // 发送完，清空输入框
            myessayd_comment_content.setText("");


            Toast.makeText(getApplicationContext(), "评论成功！", Toast.LENGTH_SHORT).show();
        }
    }
    //把日期转为字符串  
    public static String ConverToString(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return df.format(date);
    }
}
