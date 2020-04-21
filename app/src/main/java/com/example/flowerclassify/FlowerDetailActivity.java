package com.example.flowerclassify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ImageView;
import com.example.utils.Flower;

import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

public class FlowerDetailActivity extends AppCompatActivity {
    private TextView flower_detail_name;
    private TextView flower_detail_branch;
    private TextView flower_detail_sciencename;
    private TextView flower_detail_engname;
    private TextView flower_detail_provenance;
    private TextView flower_detail_reproduce;
    private TextView flower_detail_stage;
    private TextView flower_detail_sunshine;
    private TextView flower_detail_temperature;
    private TextView flower_detail_soil;
    private TextView flower_detail_moisture;
    private TextView flower_detail_character;
    private TextView flower_detail_ornamental;
    private ImageView flower_detail_image;
    ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_detail);
        List<Flower> flower=getXlsData("flower.xls",0);
        Intent intent=getIntent();
        String flowername=intent.getStringExtra("flowername");
        Flower myflower=new Flower();
        for(Flower f:flower){
            if(f.getName().equals(flowername)){
                myflower=f;
                break;
            }
        }
        flower_detail_name= (TextView)findViewById(R.id.flower_detail_name);
        flower_detail_name.setText(myflower.getName()+myflower.getAlias());
        flower_detail_branch= (TextView)findViewById(R.id.flower_detail_branch);
        flower_detail_branch.setText(myflower.getFanmily());
        flower_detail_sciencename= (TextView)findViewById(R.id.flower_detail_sciencename);
        flower_detail_sciencename.setText(myflower.getScientific_name());
        flower_detail_engname= (TextView)findViewById(R.id.flower_detail_engname);
        if(myflower.getEnglish_name().equals("None"))
            flower_detail_engname.setText("");
        flower_detail_engname.setText(myflower.getEnglish_name());
        flower_detail_provenance= (TextView)findViewById(R.id.flower_detail_provenance);
        flower_detail_provenance.setText(myflower.getProvenance());
        flower_detail_reproduce= (TextView)findViewById(R.id.flower_detail_reproduce);
        flower_detail_reproduce.setText(myflower.getReproduction());
        flower_detail_stage= (TextView)findViewById(R.id.flower_detail_stage);
        flower_detail_stage.setText(myflower.getStage());
        flower_detail_sunshine= (TextView)findViewById(R.id.flower_detail_sunshine);
        flower_detail_sunshine.setText(myflower.getSunshine());
        flower_detail_temperature= (TextView)findViewById(R.id.flower_detail_temperature);
        flower_detail_temperature.setText(myflower.getTemperature());
        flower_detail_soil= (TextView)findViewById(R.id.flower_detail_soil);
        flower_detail_soil.setText(myflower.getSoil());
        flower_detail_moisture= (TextView)findViewById(R.id.flower_detail_moisture);
        flower_detail_moisture.setText(myflower.getMoisture());

        flower_detail_character = (TextView)findViewById(R.id.flower_detail_character);
        flower_detail_character.setText("\u3000\u3000"+myflower.getCharacter());
        //flower_detail_character.getViewTreeObserver().addOnGlobalLayoutListener(new OnTvGlobalLayoutListener1());
        flower_detail_ornamental = (TextView)findViewById(R.id.flower_detail_ornamental);
        flower_detail_ornamental.setText("\u3000\u3000"+myflower.getOrnamental());
        //flower_detail_ornamental.getViewTreeObserver().addOnGlobalLayoutListener(new OnTvGlobalLayoutListener2());
        flower_detail_image=(ImageView)findViewById(R.id.flower_detail_image);
        Bitmap bitmap=base64ToBitmap(myflower.getImg());
        flower_detail_image.setImageBitmap(bitmap);

    }

    private class OnTvGlobalLayoutListener1 implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            flower_detail_character.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            final String newText = autoSplitText(flower_detail_character);
            if (!TextUtils.isEmpty(newText)) {
                flower_detail_character.setText(newText);
            }
        }
    }
    private class OnTvGlobalLayoutListener2 implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            flower_detail_ornamental.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            final String newText = autoSplitText(flower_detail_ornamental);
            if (!TextUtils.isEmpty(newText)) {
                flower_detail_ornamental.setText(newText);
            }
        }
    }
    private String autoSplitText(final TextView tv) {
        final String rawText = tv.getText().toString(); //原始文本
        final Paint tvPaint = tv.getPaint(); //paint，包含字体等信息
        final float tvWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight(); //控件可用宽度

        //将原始文本按行拆分
        String [] rawTextLines = rawText.replaceAll("\r", "").split("\n");
        StringBuilder sbNewText = new StringBuilder();
        for (String rawTextLine : rawTextLines) {
            if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                //如果整行宽度在控件可用宽度之内，就不处理了
                sbNewText.append(rawTextLine);
            } else {
                //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                float lineWidth = 0;
                for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                    char ch = rawTextLine.charAt(cnt);
                    lineWidth += tvPaint.measureText(String.valueOf(ch));
                    if (lineWidth <= tvWidth) {
                        sbNewText.append(ch);
                    } else {
                        sbNewText.append("\n");
                        lineWidth = 0;
                        --cnt;
                    }
                }
            }
            sbNewText.append("\n");
        }

        //把结尾多余的\n去掉
        if (!rawText.endsWith("\n")) {
            sbNewText.deleteCharAt(sbNewText.length() - 1);
        }

        return sbNewText.toString();
    }
    /**
     * 获取 excel 表格中的数据,不能在主线程中调用
     *
     * @param xlsName excel 表格的名称
     * @param index   第几张表格中的数据
     */
    private List<Flower> getXlsData(String xlsName, int index) {
        List<Flower> fowerList=new ArrayList<Flower>();
        AssetManager assetManager =getAssets();

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
}
