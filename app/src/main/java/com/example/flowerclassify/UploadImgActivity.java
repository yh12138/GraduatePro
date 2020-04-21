package com.example.flowerclassify;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utils.UploadAdapter;
import com.example.utils.UploadImg;
import com.example.utils.UploadImgDao;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class UploadImgActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        UploadAdapter.Callback, View.OnClickListener {
    private List<UploadImg> contentList;
    private ListView mListView;
    private FloatingActionButton upload_fab_1;
    private FloatingActionsMenu upload_fab_menu;
    private final int HANDLER_MSG_TELL_RECV = 0x124;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_img);

        init();
    }

    private void init() {
        mListView = (ListView) findViewById(R.id.upload_list_view);
        UploadImgDao uploaddao = new UploadImgDao();
        contentList = uploaddao.QueryAll();
        //
        mListView.setAdapter(new UploadAdapter(this, contentList, this));
        mListView.setOnItemClickListener(this);
        upload_fab_1 = (FloatingActionButton) findViewById(R.id.upload_fab_1);
        upload_fab_menu = (FloatingActionsMenu) findViewById(R.id.upload_fab_menu);

        upload_fab_1.setOnClickListener(this);
    }


    /**
     * 响应ListView中item的点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
        Toast.makeText(this, "listview的item被点击了！，点击的位置是-->" + position,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * 接口方法，响应ListView按钮点击事件
     */
    @Override
    public void click(View v) {
//        Toast.makeText(
//                UploadImgActivity.this,
//                "listview的内部的按钮被点击了！，位置是-->" + (Integer) v.getTag() + ",内容是-->"
//                        + contentList.get((Integer) v.getTag()).getId(),
//                Toast.LENGTH_SHORT).show();
        Bitmap bitmap= BitmapFactory.decodeByteArray(contentList.get((Integer) v.getTag()).getImg(),0,
                contentList.get((Integer) v.getTag()).getImg().length);
        //网络和端口号（端口号与服务器一致但可以随便）
        String host = "192.168.1.106";
        String port = "666";

        String content = "2;"+contentList.get((Integer) v.getTag()).getName()+";"+bitmapToBase64(bitmap);
        System.out.println("content："+contentList.get((Integer) v.getTag()).getId()+content);
        System.out.println("content.length："+content.length());

        Toast.makeText(UploadImgActivity.this,host+","+port+","+content.length(),Toast.LENGTH_LONG).show();
        //启动网络线程处理数据
        startNetThread(host,Integer.parseInt(port),content);
    }
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            //接受到服务器信息时执行
            //设置弹框
            AlertDialog.Builder builder=new AlertDialog.Builder(UploadImgActivity.this);
            String result=(msg.obj).toString();
            if(result.equals("success")) {
                builder.setMessage("上传成功");
                //创建弹框并显示
                builder.create().show();
            }
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
                    byte[] bytes = new byte[1024];
                    int n = 0;
                    n=is.read(bytes);
                    String result=new String(bytes,0,n);
                    System.out.println("upload"+result);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_fab_1:
                mListView.smoothScrollToPosition(0);
                break;
        }
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
}
