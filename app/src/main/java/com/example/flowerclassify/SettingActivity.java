package com.example.flowerclassify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Toast;
import com.example.utils.User;
import com.example.utils.UserDao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingActivity extends AppCompatActivity {
    private final int HANDLER_MSG_TELL_RECV = 0x124;
    private Button setting_modify;
    private ImageView setting_headimg;

    private Bitmap bimg;
    private File mPhotoFile;
    private String mPhotoPath;
    private Button library;
    private Button camera;
    private static final int REQUEST_SYSTEM_PIC = 1;
    private static final int CAMERA_RESULT = 2;

    private byte[] headshot;//头像
    private EditText setting_phone1;
    private EditText setting_name1;
    private EditText setting_pwd1;
    private String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //摄像头按钮
        camera = (Button)findViewById(R.id.setting_camera);
        //手机图库按钮
        library = (Button)findViewById(R.id.setting_library);
        //手机图库按钮动作
        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(SettingActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SettingActivity.this, new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    //打开系统相册
                    openAlbum();
                }
            }
        });
        //摄像头按钮动作
        camera.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");//开始拍照
                    mPhotoPath = getSDPath()+"/"+ getPhotoFileName();//设置图片文件路径，getSDPath()和getPhotoFileName()具体实现在下面
                    mPhotoFile = new File(mPhotoPath);
                    if (!mPhotoFile.exists()) {
                        mPhotoFile.createNewFile();//创建新文件
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,//Intent有了图片的信息
                            Uri.fromFile(mPhotoFile));
                    startActivityForResult(intent, CAMERA_RESULT);//跳转界面传回拍照所得数据
                } catch (Exception e) {
                }
            }
        });
        //图片
        setting_headimg = (ImageView)findViewById(R.id.setting_headimg);
        Bitmap bitmap=((BitmapDrawable)setting_headimg.getDrawable()).getBitmap();
        headshot=img(bitmap);

        setting_phone1=(EditText)findViewById(R.id.setting_phone1);
        setting_name1=(EditText)findViewById(R.id.setting_name1);
        setting_pwd1=(EditText)findViewById(R.id.setting_pwd1);
        UserDao userdao=new UserDao();
        Intent intent=getIntent();
        String phone=intent.getStringExtra("phone");
        User user=userdao.Query(phone);
        role=user.getRole();
        setting_phone1.setText(user.getPhone());
        setting_name1.setText(user.getName());
        setting_pwd1.setText(user.getPassword());
        setting_modify = (Button)findViewById(R.id.setting_modify);
        setting_modify.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDao userdao=new UserDao();
                User r=new User(headshot,setting_phone1.getText().toString(),setting_name1.getText().toString(),
                        setting_pwd1.getText().toString(), role);
                userdao.UpdateAll(r);
                Toast.makeText(SettingActivity.this, "修改成功", Toast.LENGTH_LONG).show();
                //记录登录状态
                //步骤1：创建一个SharedPreferences对象
                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                //步骤2： 实例化SharedPreferences.Editor对象
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //步骤3：将获取过来的值放入文件
                //获取当前时间
                Date date = new Date(System.currentTimeMillis());
                String datestr = ConverToString(date);

                editor.putString("phone", setting_phone1.getText().toString());
                editor.putString("pwd", setting_pwd1.getText().toString());
                editor.putString("date", datestr);
                //步骤4：提交
                editor.commit();
                finish();
            }
        });
    }
    //得到手机SD卡路径
    public String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if   (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();

    }

    //得到手机文件名字
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date)  +".jpg";
    }

    //打开系统相册
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_SYSTEM_PIC);//打开系统相册

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(SettingActivity.this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //相册照片
        if (requestCode == REQUEST_SYSTEM_PIC && resultCode == RESULT_OK && null != data) {
            if (Build.VERSION.SDK_INT >= 19) {
                handleImageOnKitkat(data);
            } else {
                handleImageBeforeKitkat(data);
            }
        }
        //相机照片
        if (requestCode == CAMERA_RESULT) {
            Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath, null);
            bimg=bitmap;
            headshot=img(bitmap);
            setting_headimg.setImageBitmap(bitmap);
        }
    }
    @TargetApi(19)
    private void handleImageOnKitkat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(SettingActivity.this, uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content:" +
                        "//downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是File类型的uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        //根据图片路径显示图片
        displayImage(imagePath);

    }

    private void handleImageBeforeKitkat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);

    }
    //得到图片路径
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    //显示图片
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            bimg=bitmap;
            headshot=img(bitmap);
            setting_headimg.setImageBitmap(bitmap);
        } else {
            Toast.makeText(SettingActivity.this, "failed to get image", Toast.LENGTH_SHORT).show();
        }

    }
    //把图片转换为字节
    private byte[]img(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
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
