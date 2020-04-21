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
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
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


import com.example.utils.History;
import com.example.utils.HistoryDao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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


    //横向GridView
    GridView gridView;

    private String result;
    public static String rootXMLPath = Environment.getExternalStorageDirectory().getPath() + "/testTXT";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recognize, container, false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        //识别按钮
        recognize=(Button)getActivity().findViewById(R.id.recognize);

        //recognize按钮动作
        recognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //网络和端口号（端口号与服务器一致但可以随便）
                String host = "192.168.1.106";
                String port = "666";

                String content = "1;"+flower_content;
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
        bimg=((BitmapDrawable)flower.getDrawable()).getBitmap();
        flower_content=bitmapToBase64(bimg);



        gridView= (GridView)getActivity().findViewById(R.id.gridview);


    }


    /**设置GirdView参数，绑定数据*/
    private void setGridView(Bitmap[] bitmaps,String []strs) {
        int size = bitmaps.length;

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int columnWidth = dm.widthPixels;//由一屏幕显示的项数决定

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size * columnWidth + size, LinearLayout.LayoutParams.FILL_PARENT);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(columnWidth/1); // 设置列表项宽
        gridView.setHorizontalSpacing(5); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size); // 设置列数量=列表集合数

        GridViewAdapter adapter = new GridViewAdapter(getActivity().getApplicationContext(), bitmaps,strs);
        gridView.setAdapter(adapter);
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            //接受到服务器信息时执行
            //设置弹框
//            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
//            builder.setMessage("1，python传来的数据："+(msg.obj).toString().length());
//            //创建弹框并显示
//            builder.create().show();

            String str=(msg.obj).toString();
            //Toast.makeText(getActivity(),str.length(),Toast.LENGTH_LONG).show();
            String strs[]=StrToArray(str,";");
            Bitmap[] arrBitmap=StrToBitmaps(strs[0],strs[3],strs[6]);
            String name=strs[1]+";"+strs[4]+";"+strs[7]+";";
            String score=strs[2]+";"+strs[5]+";"+strs[8]+";";
            byte[] flower=img(bimg);
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
            String phone=sharedPreferences.getString("phone","");
            History history=new History(flower,name,score,phone,new Date());
            HistoryDao historydao=new HistoryDao();
            historydao.Insert(history);
            setGridView(arrBitmap,strs);
        }
    };
    //服务器连接
    private void startNetThread(final String host,final int port,final String content){
        new Thread() {
            @Override
            public void run() {
                try {
                    //Toast.makeText(getActivity(),"startNetThread",Toast.LENGTH_LONG).show();
                    //创建客户端对象
                    Socket socket = new Socket(host, port);
                    //获取客户端对象的输出流
                    OutputStream outputStream=socket.getOutputStream();
                    //把内容以字节流的形式写入
                    outputStream.write(content.getBytes());
                    //刷新流管道
                    outputStream.flush();
                    System.out.println("打印客户端中内容："+socket);

                    //拿到客户端输入流
                    InputStream is = socket.getInputStream();
                    char[] bytes = new char[1024];
                    int n = -1;
                    result="";
                    //回应数据
//					byte[] bytes = new byte[1024];
//                    int n=0;//得到实际读取到的字节数 读到最后返回-1
//                    result="";
//                    int k=0;
//                    System.out.println("startNetThread:"+result+";");
//                    //循环读取
//                    while((n=is.read(bytes))!=-1)//把fis里的东西读到bytes数组里去
//                    {
//                        //把字节转成String 从0到N变成String
//                        String w=new String(bytes,0,n);
//                        k=k+n;
//                        System.out.println(n+"char:startNetT1："+w);
//                        //result=result+w;
//                        //System.out.println(n+":result："+result);
//                    }
					BufferedReader reader = new BufferedReader(new InputStreamReader(is,"GBK"));
					do  {
						if ((n=reader.read(bytes))!=-1){
							String w=new String(bytes,0,n);
							System.out.println(w);
							result=result+w;
						}
					}while (reader.ready());
					socket.shutdownInput();
                    System.out.println(":startNetThread："+result.length());
                    Message msg = handler.obtainMessage(HANDLER_MSG_TELL_RECV, result);//new String(bytes,0,n)
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
		Bitmap bitmap=null;
		try {
			Log.d("test", "stringToBitmap: "+base64Data);
			byte[] bytes = Base64.decode(base64Data, Base64.NO_WRAP);
			bitmap= BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
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
        System.out.println(strs.length+"strs："+str.length());
        return strs;
    }
    //base转为Bitmap数组
    private Bitmap[] StrToBitmaps(String str1,String str2,String str3) {
        System.out.println("str1："+str1);
        System.out.println("str2："+str2);
        System.out.println("str3"+str3);
        Bitmap bitmap1=base64ToBitmap(str1);
        Bitmap bitmap2=base64ToBitmap(str2);
        Bitmap bitmap3=base64ToBitmap(str3);
        Bitmap[] arrBitmap={bitmap1,bitmap2,bitmap3};
        System.out.println("StrToBitmaps："+arrBitmap.length);
		//Toast.makeText(getActivity(), "arrBitmap:"+arrBitmap.length, Toast.LENGTH_SHORT).show();
        return arrBitmap;
    }
    /**
     * 保存内容到TXT文件中
     *
     * @param fileName
     * @param content
     * @return
     */
    public static boolean writeToXML(String fileName, String content) {
        FileOutputStream fileOutputStream;
        BufferedWriter bufferedWriter;
        createDirectory(rootXMLPath);
        File file = new File(rootXMLPath + "/" + fileName + ".txt");
        try {
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            bufferedWriter.write(content);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    //把图片转换为字节
    private byte[]img(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    /**
     * 读取XML内容
     *
     * @param filePath
     * @return
     */
    public static String readFromXML(String filePath) {
        FileInputStream fileInputStream;
        BufferedReader bufferedReader;
        StringBuilder stringBuilder = new StringBuilder();
        File file = new File(filePath);
        if (file.exists()) {
            try {
                fileInputStream = new FileInputStream(file);
                bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 创建文件夹
     *
     * @param fileDirectory
     */
    public static void createDirectory(String fileDirectory) {
        File file = new File(fileDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
    /** GirdView 数据适配器 */
    public class GridViewAdapter extends BaseAdapter {
        Context context;
        Bitmap [] list;
        String [] results;
        public GridViewAdapter(Context _context, Bitmap [] _list,String [] results) {
            this.list = _list;
            this.context = _context;
            this.results=results;
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
            final TextView result_f=(TextView) convertView.findViewById(R.id.result_f);
            imageView.setImageBitmap(list[position]);//0 1 2
            result_f.setText(results[position*3+1]+"("+results[position*3+2]+")");//1 4 7
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