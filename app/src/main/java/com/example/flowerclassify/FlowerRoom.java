package com.example.flowerclassify;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.utils.EssayAdapter;
import com.example.utils.EssayDao;
import com.example.utils.Flower;
import com.example.utils.LeftBaseAdapter;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import jxl.Sheet;
import jxl.Workbook;

public class FlowerRoom extends Fragment {

    List<Map<String,Object>> mainMapList=new ArrayList<>();
    List<List<Map<String,Object>>> gridMapList_List=new ArrayList<>();
    List<Map<String,Object>> nowGridMapList=new ArrayList<>();
    LeftBaseAdapter leftBaseAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.flowerroom, container, false);
        return view;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        initData();

        ListView  classify_left_listview= (ListView)getActivity().findViewById(R.id.classify_left);
        leftBaseAdapter=new LeftBaseAdapter(getActivity(),mainMapList);
        classify_left_listview.setAdapter(leftBaseAdapter);
        initAdapter(0);
        classify_left_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                leftBaseAdapter.setNowSelectedIndex(position);

                initAdapter(position);
            }
        });

    }
    /**
     * 获取 excel 表格中的数据,不能在主线程中调用
     *
     * @param xlsName excel 表格的名称
     * @param index   第几张表格中的数据
     */
    private List<Flower> getXlsData(String xlsName, int index) {
        List<Flower> fowerList=new ArrayList<Flower>();
        AssetManager assetManager = getActivity().getAssets();

        try {
            Workbook workbook = Workbook.getWorkbook(assetManager.open(xlsName));
            Sheet sheet = workbook.getSheet(index);

            int sheetNum = workbook.getNumberOfSheets();
            int sheetRows = sheet.getRows();
            int sheetColumns = sheet.getColumns();

            Log.d("room", "the num of sheets is " + sheetNum);
            Log.d("room", "the name of sheet is  " + sheet.getName());
            Log.d("room", "total rows is 行=" + sheetRows);
            Log.d("room", "total cols is 列=" + sheetColumns);

            for (int i = 1; i < sheetRows; i++) {
                Flower fower = new Flower();
                fower.setName(sheet.getCell(0, i).getContents());
                fower.setAlias(sheet.getCell(1, i).getContents());
                fower.setBranch(sheet.getCell(2, i).getContents());
                fower.setFanmily(sheet.getCell(3, i).getContents());
                fower.setScientific_name(sheet.getCell(4, i).getContents());
                fower.setEnglish_name(sheet.getCell(5, i).getContents());
                fower.setProvenance(sheet.getCell(6, i).getContents());
                fower.setReproduction(sheet.getCell(7, i).getContents());
                fower.setStage(sheet.getCell(8, i).getContents());
                fower.setSunshine(sheet.getCell(9, i).getContents());
                fower.setTemperature(sheet.getCell(10, i).getContents());
                fower.setSoil(sheet.getCell(11, i).getContents());
                fower.setMoisture(sheet.getCell(12, i).getContents());
                fower.setCharacter(sheet.getCell(13, i).getContents());
                fower.setOrnamental(sheet.getCell(14, i).getContents());
                fower.setImg(sheet.getCell(15, i).getContents());
                fowerList.add(fower);
            }

            workbook.close();

        } catch (Exception e) {
            Log.e("room", "read error=" + e, e);
        }

        return fowerList;
    }

    private void initAdapter(int position) {
        GridView classify_grid = (GridView)getActivity().findViewById(R.id.classify_grid);
        nowGridMapList= gridMapList_List.get(position);
        ImageAdapter simpleAdapter =
                new ImageAdapter(getActivity(), nowGridMapList);
        classify_grid.setAdapter(simpleAdapter);
        classify_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"点击了"+nowGridMapList.get(position).get("girdName"),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(),FlowerDetailActivity.class);
                intent.putExtra("flowername",nowGridMapList.get(position).get("girdName").toString());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        List<Flower> flower=getXlsData("flower.xls",0);
        List<String> flowername=new ArrayList<String>();
        List<String> flowername1=new ArrayList<String>();
        for(int i=0;i<flower.size();i++){
            flowername.add(flower.get(i).getBranch());
            flowername1.add(flower.get(i).getBranch());
            //System.out.println(flower.get(i).getBranch());
        }
		List<String> name=removeDuplicate(flowername);//科
        List<Integer> num=new ArrayList<Integer>();//数量
		for (int i=0;i<name.size();i++) {
            //System.out.println(name.get(i) + ": " + Collections.frequency(flowername1,name.get(i)));
            num.add(Collections.frequency(flowername1,name.get(i)));
        }
        int k=0;
        for (int i=0;i<name.size();i++){
            Map<String,Object> mianMap=new HashMap<>();
            mianMap.put("flowerName",name.get(i));
            mainMapList.add(mianMap);

            List<Map<String,Object>> gridMapList=new ArrayList<>();
            for (int j=0;j<num.get(i);j++){

                Map<String,Object>  gridMap=new HashMap<>();
                gridMap.put("girdName", flower.get(k).getName());
                gridMap.put("girdIcon",flower.get(k).getImg());


                gridMapList.add(gridMap);
                k++;
            }
            gridMapList_List.add(gridMapList);
        }
    }
	public static List<String> removeDuplicate(List<String> list)
	{
		    Set set = new LinkedHashSet<String>();
			set.addAll(list);
			list.clear();
			list.addAll(set);
			return list;
	} 
	 private class ImageAdapter extends BaseAdapter {
 
        private Context mContext;
        List<Map<String, Object>> mainMapList;
        public ImageAdapter(Context context,List<Map<String, Object>> mainMapList) {
            this.mContext = context;
            this.mainMapList = mainMapList;
        }
 
 
        @Override
        public int getCount() {
            return mainMapList.size();
        }
 
        @Override
        public Object getItem(int position) {
            return mainMapList.get(position);
        }
 
        @Override
        public long getItemId(int position) {
            return position;
        }
 
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
 
 
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.room_right_girdview_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.itemImg = (ImageView)  convertView.findViewById(R.id.right_tv_list_item_icon);
                viewHolder.t = (TextView) convertView.findViewById(R.id.right_tv_list_item);
                viewHolder.t .setText(mainMapList.get(position).get("girdName").toString());
				//Log.d("test", "string: "+mainMapList.get(position).get("girdIcon").toString());
                Bitmap bitmap=base64ToBitmap(mainMapList.get(position).get("girdIcon").toString());
                // Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chat);
                viewHolder.itemImg.setImageBitmap(bitmap);
                convertView.setTag(viewHolder);
 
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

 
 
            return convertView;
        }
		/*** base64转为bitmap*/
		public Bitmap base64ToBitmap(String base64Data) {
			Bitmap bitmap=null;
			try {
				//Log.d("test", "stringToBitmap: "+base64Data);
				byte[] bytes = Base64.decode(base64Data, Base64.NO_WRAP);
				bitmap= BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bitmap;
		}

        class ViewHolder {
            ImageView itemImg;
            TextView t;
        }
    }
}
