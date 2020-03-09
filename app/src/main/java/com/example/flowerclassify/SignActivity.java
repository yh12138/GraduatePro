package com.example.flowerclassify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utils.RememberUser;
import com.example.utils.RememberUserDao;
import com.example.utils.User;
import com.example.utils.UserDao;

import org.litepal.crud.DataSupport;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SignActivity extends TitleActivity {
    private Button sign_login1;
    private Button sign_login;
    private EditText sign_account;
    private EditText sign_password;
    private CheckBox sign_remember;

    private RadioButton sign_user;
    private RadioButton sign_manager;
    private String role;
    private TextView sign_register;
    private TextView sign_forget;
    private Boolean remember;
    private String phone;
    private String pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        remember=false;
        sign_account=(EditText)findViewById(R.id.sign_account);
        sign_password=(EditText)findViewById(R.id.sign_password);
        sign_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                phone=sign_account.getText().toString();
                if(v.getId()==R.id.sign_password){
                    if(phone.length()>0&&hasFocus==true){
                        List<RememberUser> users= DataSupport.findAll(RememberUser.class);
                        RememberUser result=new RememberUser();
                        for(RememberUser user:users){
                            if(user.getPhone().equals(phone)) {
                                result = user;
                                break;
                            }
                        }
                        if(result!=null){
                            sign_password.setText(result.getPassword());
                        }
                    }
                }
            }
        });
        sign_user = (RadioButton)findViewById(R.id.sign_user);
        sign_manager = (RadioButton)findViewById(R.id.sign_manager);
        sign_login1=(Button)findViewById(R.id.sign_login1);
        sign_login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               DataSupport.deleteAll(User.class);
                DataSupport.deleteAll(RememberUser.class);
            }
        });

        sign_login=(Button)findViewById(R.id.sign_login);
        sign_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone=sign_account.getText().toString();
                pwd=sign_password.getText().toString();
                if(sign_user.isChecked()){
                    role="y";
                }
                if(sign_manager.isChecked()){
                    role="m";
                }
                UserDao userdao=new UserDao();
                RememberUserDao rememberuserdao=new RememberUserDao();
				User result=userdao.Query(phone);

				if(result.getName()!= null) {
                    if (result.getPassword().equals(pwd)&&result.getRole().equals(role)) {
                        Toast.makeText(SignActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                        //记录登录状态
                        //步骤1：创建一个SharedPreferences对象
                        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                        //步骤2： 实例化SharedPreferences.Editor对象
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        //步骤3：将获取过来的值放入文件
                        //获取当前时间
                        Date date = new Date(System.currentTimeMillis());
                        String datestr = ConverToString(date);

                        editor.putString("phone", phone);
                        editor.putString("pwd", pwd);
                        editor.putString("date", datestr);
                        //步骤4：提交
                        editor.commit();
                        //记住密码
                        if(remember==true) {
                            RememberUser rememberUser=rememberuserdao.Query(phone);
                            if(rememberUser.getPhone()==null) {
                                RememberUser user = new RememberUser(phone,pwd);
                                rememberuserdao.Insert(user);
                            }
                        }
                        else{
                            RememberUser re=rememberuserdao.Query(phone);

                            if(re.getPhone()!=null){
                                DataSupport.deleteAll(RememberUser.class,"phone=?",phone);
                            }
                        }
                        Intent intent = new Intent(SignActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignActivity.this, "密码或账号或角色错误", Toast.LENGTH_LONG).show();
                    }
                }
				else {
                    Toast.makeText(SignActivity.this, "没有该账号", Toast.LENGTH_LONG).show();
                }
            }
        });
        sign_remember=(CheckBox)findViewById(R.id.sign_remember);
        sign_remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    Toast.makeText(SignActivity.this,"已选择",Toast.LENGTH_LONG).show();
                    remember=true;

                }else{
                    Toast.makeText(SignActivity.this,"未选择",Toast.LENGTH_LONG).show();
                    remember=false;
                }
            }
        });
        sign_register=(TextView)findViewById(R.id.sign_register);
        sign_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        sign_forget=(TextView)findViewById(R.id.sign_forget);
        sign_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String modifyphone=sign_account.getText().toString();
                UserDao userdao=new UserDao();
                User user=userdao.Query(modifyphone);
                if(user.getPhone()!=null) {
                    Intent intent = new Intent(SignActivity.this, ModifyPwdActivity.class);
                    intent.putExtra("phone", modifyphone);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(SignActivity.this,"没有该账号",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    public int getLayoutId() {
        return (R.layout.activity_sign);
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

}
