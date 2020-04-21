package com.example.flowerclassify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utils.User;
import com.example.utils.UserDao;

import org.litepal.crud.DataSupport;

public class ModifyPwdActivity extends AppCompatActivity {
    private EditText modify_pwd1;
    private EditText modify_newpwd1;
    private TextView modify_phone;
    private Button modify_modify;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        Intent intent=getIntent();
        String phone=intent.getStringExtra("phone");
        modify_pwd1=(EditText)findViewById(R.id.modify_pwd1);
        modify_newpwd1=(EditText)findViewById(R.id.modify_newpwd1);
        modify_phone=(TextView)findViewById(R.id.modify_phone);
        modify_phone.setText(phone);
        UserDao userdao=new UserDao();
        user=userdao.Query(phone);
        modify_pwd1.setText(user.getPassword());
        modify_modify=(Button)findViewById(R.id.modify_modify);
        modify_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ModifyPwdActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                String newpwd=modify_newpwd1.getText().toString();
                UserDao userdao=new UserDao();
                userdao.UpdatePwd(user,newpwd);
                Toast.makeText(ModifyPwdActivity.this, "修改成功", Toast.LENGTH_LONG).show();
            }
        });
    }
}
