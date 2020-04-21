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
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;
import android.os.Bundle;

import com.example.utils.Essay;
import com.example.utils.EssayDao;
import com.example.utils.LimitScrollEditText;
import com.example.utils.RememberUser;
import com.example.utils.User;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
public class WriteEssayActivity extends AppCompatActivity {
	private final int HANDLER_MSG_TELL_RECV = 0x124;
    private ImageView flower;

    private Bitmap bimg;
    private File mPhotoFile;
    private String mPhotoPath;
    private static final int REQUEST_SYSTEM_PIC = 1;
    private static final int CAMERA_RESULT = 2;
    private Button write_submit;
    private LimitScrollEditText WriteTitle;
    private LimitScrollEditText WriteContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_essay);
		//图片
        flower = (ImageView)findViewById(R.id.writeimg);
        final Bitmap bitmap=((BitmapDrawable)flower.getDrawable()).getBitmap();
		//手机图库按钮动作
        flower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(WriteEssayActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(WriteEssayActivity.this, new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    //打开系统相册
                    openAlbum();
                }
            }
        });
        WriteTitle = (LimitScrollEditText)findViewById(R.id.WriteTitle);
        WriteTitle.setMaxLength(20);
        WriteTitle.setHint("最多输入20字");
		WriteContent = (LimitScrollEditText)findViewById(R.id.WriteContent);
        WriteContent.setMaxLength(500);
        WriteContent.setHint("最多输入500字");

        write_submit=(Button)findViewById(R.id.write_submit);
        write_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=WriteTitle.getTextString();
                String content=WriteContent.getTextString();
                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                String phone=sharedPreferences.getString("phone","");
                Essay essay;
                if(bimg!=null) {
                    byte[] bytes=img(bimg);
                    essay = new Essay(title, content, phone, 0, 0, new Date(), 1, bytes);
                }
                else
                    essay = new Essay(title, content, phone, 0, 0, new Date(), 0, null);
                EssayDao essaydao=new EssayDao();
                Boolean f=essaydao.Insert(essay);
                if(f==true) {
                    Toast.makeText(WriteEssayActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(WriteEssayActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(WriteEssayActivity.this,"提交失败",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(WriteEssayActivity.this, "You denied the permission", Toast.LENGTH_SHORT).show();
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
            flower.setImageBitmap(bitmap);
        }
    }
    @TargetApi(19)
    private void handleImageOnKitkat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(WriteEssayActivity.this, uri)) {
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
            flower.setImageBitmap(bitmap);
        } else {
            Toast.makeText(WriteEssayActivity.this, "failed to get image", Toast.LENGTH_SHORT).show();
        }

    }
    //把图片转换为字节
    private byte[]img(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
