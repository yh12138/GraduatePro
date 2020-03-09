package com.example.flowerclassify;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Recognize extends Fragment {
    private final int HANDLER_MSG_TELL_RECV = 0x124;
    private Button recognize;
    private Button my;
    private ImageView flower;
    private String flower_content;

    private Bitmap bimg;
    private File mPhotoFile;
    private String mPhotoPath;
    private Button library;
    private Button camera;
    private static final int REQUEST_SYSTEM_PIC = 1;
    private static final int CAMERA_RESULT = 2;
    //显示结果
    private ImageView flower1;
    private ImageView flower2;
    private ImageView flower3;
    private TextView result1;
    private TextView result2;
    private TextView result3;
    private TextView percent1;
    private TextView percent2;
    private TextView percent3;
    TextView[] arrTextr={result1,result2,result3};
    TextView[] arrTextp={percent1,percent2,percent3};
    ImageView[] arrImage={flower1,flower2,flower3};

    //横向GridView
    GridView gridView;
    private int[] imgs = {R.drawable.flower, R.drawable.flower,
            R.drawable.flower,R.drawable.flower,R.drawable.flower};



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recognize, container, false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //结果显示初始化
        int [] widget1={R.id.flower1,R.id.flower2,R.id.flower3};
        int [] widget2={R.id.result1,R.id.result2,R.id.result3,R.id.percent1,R.id.percent2,R.id.percent3};
        for (int i=0;i<3;i++){
            arrImage[i]=(ImageView)getActivity().findViewById(widget1[i]);
        }
        for (int i=0;i<3;i++){
            arrTextr[i]=(TextView)getActivity().findViewById(widget2[i]);
            arrTextp[i]=(TextView)getActivity().findViewById(widget2[i+3]);
        }

        //识别按钮
        recognize=(Button)getActivity().findViewById(R.id.recognize);

        //recognize按钮动作
        recognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //网络和端口号（端口号与服务器一致但可以随便）
                String host = "192.168.1.105";
                String port = "666";

                String content = flower_content;
                System.out.println("content："+content);
                System.out.println("content.length："+content.length());

                Toast.makeText(getActivity(),host+","+port+","+content.length(),Toast.LENGTH_LONG).show();
                //启动网络线程处理数据
                startNetThread(host,Integer.parseInt(port),content);
            }
        });
        //摄像头按钮
        camera = (Button)getActivity().findViewById(R.id.camera);
        //手机图库按钮
        library = (Button)getActivity().findViewById(R.id.btu_select);
        //手机图库按钮动作
        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new
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
        flower = (ImageView)getActivity().findViewById(R.id.flower);
        Bitmap bitmap=((BitmapDrawable)flower.getDrawable()).getBitmap();
        flower_content=bitmapToBase64(bitmap);


        arrImage[0].setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrImage[0].setDrawingCacheEnabled(true);
                MyImageDialog myImageDialog = new MyImageDialog(getActivity(),R.style.dialogWindowAnim,
                        -10,500,arrImage[0].getDrawingCache());
                myImageDialog.show();
            }
        });
        arrImage[1].setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrImage[1].setDrawingCacheEnabled(true);
                MyImageDialog myImageDialog = new MyImageDialog(getActivity(),R.style.dialogWindowAnim,
                        -10,500,arrImage[1].getDrawingCache());
                myImageDialog.show();
            }
        });
        arrImage[2].setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrImage[2].setDrawingCacheEnabled(true);
                MyImageDialog myImageDialog = new MyImageDialog(getActivity(),R.style.dialogWindowAnim,
                        -10,500,arrImage[2].getDrawingCache());
                myImageDialog.show();
            }
        });

        gridView= (GridView)getActivity().findViewById(R.id.gridview);

        setGridView();
    }


    /**设置GirdView参数，绑定数据*/
    private void setGridView() {
        int size = imgs.length;

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int columnWidth = dm.widthPixels;//由一屏幕显示的项数决定

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size * columnWidth + size, LinearLayout.LayoutParams.FILL_PARENT);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(columnWidth/1); // 设置列表项宽
        gridView.setHorizontalSpacing(5); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size); // 设置列数量=列表集合数

        GridViewAdapter adapter = new GridViewAdapter(getActivity().getApplicationContext(), imgs);
        gridView.setAdapter(adapter);
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            //接受到服务器信息时执行
            //设置弹框
            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
            builder.setMessage("python传来的数据："+(msg.obj).toString().length());
            //创建弹框并显示
            builder.create().show();

            String str=(msg.obj).toString();
            //Toast.makeText(getActivity(),str.length(),Toast.LENGTH_LONG).show();
            String strs[]=StrToArray(str,";");

            Bitmap[] arrBitmap=StrToBitmaps(strs[0],strs[3],strs[6]);

            for (int i=0;i<3;i++){
                arrImage[i].setImageBitmap(arrBitmap[i]);
                arrTextr[i].setText(strs[1+i*3]);//1 4 7
                arrTextp[i].setText(strs[2+i*3]);//2 5 8
            }

            
        }
    };
    //服务器连接
    private void startNetThread(final String host,final int port,final String content){
        new Thread() {
            @Override
            public void run() {
                try {

                    //创建客户端对象
                    Socket socket = new Socket(host, port);
                    //获取客户端对象的输出流
                    OutputStream outputStream=socket.getOutputStream();
                    //把内容以字节流的形式写入
                    outputStream.write(content.getBytes());
                    //刷新流管道
                    outputStream.flush();
                    System.out.println("打印客户端中内容："+socket);
					
					Toast.makeText(getActivity(),"startNetThread",Toast.LENGTH_LONG).show();
                    //拿到客户端输入流
                    InputStream is = socket.getInputStream();
                    byte[] bytes = new byte[is.available()];
                    //回应数据
                    int n = is.read(bytes);
					//byte[] bytes = new byte[1024];
					//int n=0;//得到实际读取到的字节数 读到最后返回-1
					//String result="";
				    ////循环读取
				    //while((n=is.read(bytes))!=-1)//把fis里的东西读到bytes数组里去
				    //{
					//    //把字节转成String 从0到N变成String
					//   String w=new String(bytes,0,n);
					//	 result=result+w;
				    //}

                    System.out.println("bytes.length："+bytes.length);
                    System.out.println("n："+n);
					Toast.makeText(getActivity(),"startNetThread1",Toast.LENGTH_LONG).show();
                    Message msg = handler.obtainMessage(HANDLER_MSG_TELL_RECV, new String(bytes, 0, n));
                    msg.sendToTarget();
                    //关闭流
                    is.close();
                    //关闭客户端
                    socket.close();
                } catch (Exception e) {
                }
            }
            //启动线程
        }.start();

    }

    /*** bitmap转为base64**/
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /*** base64转为bitmap*/
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /*** 将图片转换成Base64编码的字符串*/
    public static String imageToBase64(String path){
        if(TextUtils.isEmpty(path)){
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try{
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data,Base64.NO_CLOSE);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null !=is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
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
                    Toast.makeText(getActivity(), "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //相册照片
        if (requestCode == REQUEST_SYSTEM_PIC && resultCode == getActivity().RESULT_OK && null != data) {
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
            flower_content=bitmapToBase64(bimg);
            flower.setImageBitmap(bitmap);
        }
    }
    @TargetApi(19)
    private void handleImageOnKitkat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
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
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
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
            flower_content=bitmapToBase64(bimg);
            flower.setImageBitmap(bitmap);
        } else {
            Toast.makeText(getActivity(), "failed to get image", Toast.LENGTH_SHORT).show();
        }

    }
    //String转为String数组
    private String[] StrToArray(String str,String s) {
        String[] strs=str.split(s);
        Toast.makeText(getActivity(), "strs:"+strs.length, Toast.LENGTH_SHORT).show();
        return strs;
    }
    //base转为Bitmap数组
    private Bitmap[] StrToBitmaps(String str1,String str2,String str3) {
        Bitmap bitmap1=base64ToBitmap(str1);
        Bitmap bitmap2=base64ToBitmap(str2);
        Bitmap bitmap3=base64ToBitmap(str3);
        Bitmap[] arrBitmap={bitmap1,bitmap2,bitmap3};
		Toast.makeText(getActivity(), "arrBitmap:"+arrBitmap.length, Toast.LENGTH_SHORT).show();
        return arrBitmap;
    }
    /** GirdView 数据适配器 */
    public class GridViewAdapter extends BaseAdapter {
        Context context;
        int [] list;

        public GridViewAdapter(Context _context, int [] _list) {
            this.list = _list;
            this.context = _context;
        }


        @Override
        public int getCount() {
            return list.length;
        }

        @Override
        public Object getItem(int position) {
            return list[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.result, null);
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            imageView.setImageResource(list[position]);
//            imageView.setOnClickListener( new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    imageView.setDrawingCacheEnabled(true);
//                    MyImageDialog myImageDialog = new MyImageDialog(getActivity(),R.style.dialogWindowAnim,
//                            100,500,imageView.getDrawingCache());
//                    myImageDialog.show();
//                }
//            });
            return convertView;
        }
    }

}