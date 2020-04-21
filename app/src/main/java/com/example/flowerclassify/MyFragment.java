package com.example.flowerclassify;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Layout;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.utils.DisplayUtil;
import com.example.utils.UploadImg;
import com.example.utils.User;


import org.litepal.crud.DataSupport;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class MyFragment extends Fragment {


	
    private ImageView blurImageView;
    private ImageView avatarImageView;
	private ImageView menu;
    private LinearLayout about;
    private LinearLayout setting;
    private LinearLayout classify_history;
    private LinearLayout collect;
    private LinearLayout put;
    private LinearLayout myessay;
    private LinearLayout check;
    private WifiManager my_wifiManager;
    private WifiInfo wifiInfo;
    private DhcpInfo dhcpInfo;
    private Button login;
    private Button register;
	private TextView user_name;
	private TextView user_val;


    private PopupWindow mPopWindow;
	private PopWinShare popWinShare;
    private Context context;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my, container, false);
        
        return view;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        blurImageView=(ImageView)getActivity().findViewById(R.id.h_back);
        avatarImageView=(ImageView)getActivity().findViewById(R.id.h_head);
		user_name=(TextView)getActivity().findViewById(R.id.user_name);
		user_val=(TextView)getActivity().findViewById(R.id.user_val);
		sharedPreferences= getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
		String phone=sharedPreferences.getString("phone","");
		if (phone!=""){
			List<User> users= DataSupport.findAll(User.class);
			User result=new User();
			for(User user:users){
				if(user.getPhone().equals(phone)) {
					result = user;
					break;
				}
			}
			user_name.setText(result.getName());
			user_val.setText(result.getPhone());
			byte[]images=result.getHeadimg();
			//Bitmap bitmap=BitmapFactory.decodeByteArray(images,0,images.length);
			
			Glide.with(getActivity()).load(images)
					.bitmapTransform(new BlurTransformation(getActivity(), 25), new CenterCrop(getActivity()))
					.into(blurImageView);

			Glide.with(getActivity()).load(images)
					.bitmapTransform(new CropCircleTransformation(getActivity()))
					.into(avatarImageView);
		}	
		else{
			Glide.with(getActivity()).load(R.drawable.logo)
					.bitmapTransform(new BlurTransformation(getActivity(), 25), new CenterCrop(getActivity()))
					.into(blurImageView);

			Glide.with(getActivity()).load(R.drawable.logo)
					.bitmapTransform(new CropCircleTransformation(getActivity()))
					.into(avatarImageView);
		}			
        Context myContext = getActivity().getApplicationContext();
        my_wifiManager = (WifiManager)myContext.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!my_wifiManager.isWifiEnabled()) {
            my_wifiManager.setWifiEnabled(true);
        }
        dhcpInfo = my_wifiManager.getDhcpInfo();
        wifiInfo = my_wifiManager.getConnectionInfo();

        about=(LinearLayout)getActivity().findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Context myContext = getActivity().getApplicationContext();
//                String host=getWifiIp()+getWiFiName();
                Toast.makeText(getActivity(),intToIp(wifiInfo.getIpAddress())+","+intToIp(dhcpInfo.gateway),Toast.LENGTH_LONG).show();

            }
        });
        setting=(LinearLayout)getActivity().findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences= getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                String phone=sharedPreferences.getString("phone","");
                if(phone.equals("")){
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getActivity(),SignActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent=new Intent(getActivity(),SettingActivity.class);
                    intent.putExtra("phone",phone);
                    startActivity(intent);
                }

            }
        });
        classify_history=(LinearLayout)getActivity().findViewById(R.id.classify_history);
        classify_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences= getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                String phone=sharedPreferences.getString("phone","");
                if(phone.equals("")){
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getActivity(),SignActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getActivity(), classify_historyActivity.class);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                }
            }
        });
        collect=(LinearLayout)getActivity().findViewById(R.id.collect);
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences= getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                String phone=sharedPreferences.getString("phone","");
                if(phone.equals("")){
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getActivity(),SignActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent=new Intent(getActivity(),CollectActivity.class);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                }
            }
        });
        put=(LinearLayout)getActivity().findViewById(R.id.put);
        put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences= getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                String phone=sharedPreferences.getString("phone","");
                if(phone.equals("")){
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getActivity(),SignActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getActivity(), PutActivity.class);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                }
            }
        });
        myessay=(LinearLayout)getActivity().findViewById(R.id.myessay);
        //myessay.setVisibility(View.GONE);
        myessay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences= getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                String phone=sharedPreferences.getString("phone","");
                if(phone.equals("")){
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getActivity(),SignActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getActivity(), MyEssayActivity.class);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                }
            }
        });
        check=(LinearLayout)getActivity().findViewById(R.id.check);
        sharedPreferences= getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String myphone=sharedPreferences.getString("phone","");
        if(myphone!="") {
            List<User> users = DataSupport.findAll(User.class);
            User result = new User();
            for (User user : users) {
                if (user.getPhone().equals(phone)) {
                    result = user;
                    break;
                }
            }
            if(result.getRole().equals("m"))
                check.setVisibility(View.VISIBLE);
        }
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences= getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                String phone=sharedPreferences.getString("phone","");
                if(phone.equals("")){
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getActivity(),SignActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getActivity(), UploadImgActivity.class);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                }
            }
        });
        login=(Button)getActivity().findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),SignActivity.class);
                startActivity(intent);
            }
        });
        register=(Button)getActivity().findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),RegisterActivity.class);
                startActivity(intent);
            }
        });
        //菜单
		menu=(ImageView)getActivity().findViewById(R.id.mymenu);
		menu.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				//showPopupMenu(View v);
                //showPopupWindow();
				if (popWinShare == null) {
					//自定义的单击事件
					OnClickLintener paramOnClickListener = new OnClickLintener();

					popWinShare = new PopWinShare(getActivity(), paramOnClickListener, DisplayUtil.dip2px(context, 80), DisplayUtil.dip2px(context, 80));
					//监听窗口的焦点事件，点击窗口外面则取消显示
					popWinShare.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
						
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							if (!hasFocus) {
								popWinShare.dismiss();
							}
						}
					});
				}
				//设置默认获取焦点
				popWinShare.setFocusable(true);
				//以某个控件的x和y的偏移量位置开始显示窗口
				popWinShare.showAsDropDown(menu, 20, 0);
				//如果窗口存在，则更新
				popWinShare.update();
            }
        });
    }
	private void showPopupMenu(View v) {
        //创建弹出式菜单对象（最低版本11）
        PopupMenu popup = new PopupMenu(getActivity(), v);//第二个参数是绑定的那个view
        //获取菜单填充器
        MenuInflater inflater = popup.getMenuInflater();
        //填充菜单
        inflater.inflate(R.menu.my, popup.getMenu());
        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.exit:
                        Toast.makeText(getActivity(), "退出", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.change:
                        Toast.makeText(getActivity(), "设置", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        //显示(这一行代码不要忘记了)
        popup.show();
	}
    private void showPopupWindow() {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.popuplayout, null);
        mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        RotateAnimation rotate = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF,
                0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(3000);
        menu.startAnimation(rotate);
//        TextView tv1 = (TextView)contentView.findViewById(R.id.pop_change);
//        TextView tv2 = (TextView)contentView.findViewById(R.id.pop_exit);
//        tv1.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "clicked pop_change", Toast.LENGTH_SHORT).show();
//                mPopWindow.dismiss();
//            }
//        });
//        tv2.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "clicked pop_exit", Toast.LENGTH_SHORT).show();
//                mPopWindow.dismiss();
//            }
//        });
        // 解决popupWindow显示后不消失问题
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOutsideTouchable(true);
        //相对位置 以mMenuTv为坐标,
        mPopWindow.showAsDropDown(menu,100,0);
    }
    @Override
    public void onResume() {
        super.onResume();
        StringBuilder sb = new StringBuilder();
        sb.append("网络信息：");
        sb.append("\nipAddress：" + intToIp(dhcpInfo.ipAddress));
        sb.append("\nnetmask：" + intToIp(dhcpInfo.netmask));
        sb.append("\ngateway：" + intToIp(dhcpInfo.gateway));
        sb.append("\nserverAddress：" + intToIp(dhcpInfo.serverAddress));
        sb.append("\ndns1：" + intToIp(dhcpInfo.dns1));
        sb.append("\ndns2：" + intToIp(dhcpInfo.dns2));
        sb.append("\n");
//        System.out.println(intToIp(dhcpInfo.ipAddress));
//        System.out.println(intToIp(dhcpInfo.netmask));
//        System.out.println(intToIp(dhcpInfo.gateway));
//        System.out.println(intToIp(dhcpInfo.serverAddress));
//        System.out.println(intToIp(dhcpInfo.dns1));
//        System.out.println(intToIp(dhcpInfo.dns2));
//        System.out.println(dhcpInfo.leaseDuration);

        sb.append("Wifi信息：");
        sb.append("\nIpAddress：" + intToIp(wifiInfo.getIpAddress()));
        sb.append("\nMacAddress：" + wifiInfo.getMacAddress());
    }



    private String intToIp(int paramInt) {
        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "."
                + (0xFF & paramInt >> 24);
    }

    /*获取 WIFI 的名称*/
    public String getWiFiName() {
        WifiManager wm = (WifiManager)getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wm != null) {
            WifiInfo winfo = wm.getConnectionInfo();
            if (winfo != null) {
                String s = winfo.getSSID();
                if (s.length() > 2 && s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') {
                    return s.substring(1, s.length() - 1);
                }
            }
        }
        return "Wifi 未获取到";
    }

    /*获取 WiFi 的 IP 地址 */
    public  String getWifiIp() {
        Context myContext = getActivity().getApplicationContext();
        if (myContext == null) {
            throw new NullPointerException("上下文 context is null");
        }
        WifiManager wifiMgr = (WifiManager) myContext.getSystemService(Context.WIFI_SERVICE);
        if (isWifiEnabled()) {
            int ipAsInt = wifiMgr.getConnectionInfo().getIpAddress();
            String ip = Formatter.formatIpAddress(ipAsInt);
            if (ipAsInt == 0) {
                return "未能获取到IP地址";
            } else {
                return ip;
            }
        } else {
            return "WiFi 未连接";
        }
    }

    /*判断当前 WIFI 是否连接 */
    public  boolean isWifiEnabled() {
        Context myContext = getActivity().getApplicationContext();
        if (myContext == null) {
            throw new NullPointerException("上下文 context is null");
        }
        WifiManager wifiMgr = (WifiManager) myContext.getSystemService(Context.WIFI_SERVICE);
        if (wifiMgr.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            ConnectivityManager connManager = (ConnectivityManager) myContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo wifiInfo = connManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            return wifiInfo.isConnected();
        } else {
            return false;
        }
    }

    /* @类名称: OnClickLintener
		@描述: 工具栏popwindow点击事件*/
	class OnClickLintener implements View.OnClickListener {
	  
			@Override  
			public void onClick(View v) {  
				switch (v.getId()) {  
				case R.id.layout_change:
                    Intent intent=new Intent(getActivity(),SignActivity.class);
                    startActivity(intent);
					break;  
				case R.id.layout_exit:
                    //步骤1：创建一个SharedPreferences对象
                    sharedPreferences=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                    //步骤2： 实例化SharedPreferences.Editor对象
                    editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    Intent intent1=new Intent(getActivity(),MainActivity.class);
                    startActivity(intent1);
					break;  
						
				default:  
					break;  
				}  
				  
			}  
			  
	}  
}
