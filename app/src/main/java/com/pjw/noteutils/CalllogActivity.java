package com.pjw.noteutils;

import androidx.annotation.Dimension;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.pjw.noteutils.bean.PhoneBean;
import com.pjw.noteutils.bean.JsonBean;
import com.pjw.noteutils.databinding.ActivityCalllogBinding;
import com.pjw.noteutils.utils.GetJsonDataUtil;

import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class CalllogActivity extends AppCompatActivity {

    private ActivityCalllogBinding ACallBin;
    private List<String> phoneList = new ArrayList<>();
    private long nowTime;

    private String myProvince;//选择的省
    private String myCity;//选择的市

    CityPickerView mPicker=new CityPickerView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ACallBin = DataBindingUtil.setContentView(this, R.layout.activity_calllog);
        ACallBin.setClick(new ClickProxy());

        //预先加载仿iOS滚轮实现的全部数据
        mPicker.init(this);

        //readExcel("phone.xlsx");
    }

    private void init(){
        setEditTextHintWithSize(ACallBin.edtTimes,getString(R.string.home_times_tips),12);
        setEditTextHintWithSize(ACallBin.edtNum,getString(R.string.home_number_tips),12);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "未获取对应权限", Toast.LENGTH_SHORT).show();
            ACallBin.btn1.setEnabled(false);
            ACallBin.btn2.setEnabled(false);
        }
    }

    public class ClickProxy {
        public void createNums() {
            nowTime = System.currentTimeMillis();
            //获取输入的电话
            String phoneNums = ACallBin.edtNum.getText().toString();
            if (TextUtils.isEmpty(phoneNums)){
                Toast.makeText(CalllogActivity.this, "请输入电话号码!", Toast.LENGTH_SHORT).show();
                return;
            }
            //解析所有电话号码
            String[] temp = null;
            temp = phoneNums.split("\\*");
            for (String s : temp) {
                if (s.length() != 11) {
                    Toast.makeText(CalllogActivity.this, "部分电话号码长度有误!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            for (String s : temp) {
                PhoneBean phoneBean = new PhoneBean();
                phoneBean.setNumber(s);
                phoneBean.setDate(nowTime);

                int callTime = getRandom(180)+10;
                phoneBean.setDuration(String.valueOf(callTime));
                nowTime = nowTime + callTime * 1000 + 40000;

                phoneBean.setType("2");
                phoneBean.setTypenew("0");

                createCallLog(phoneBean);
            }
        }

        public void createRandom(){
            nowTime = System.currentTimeMillis();
            //获取需要生成的个数
            String times =  ACallBin.edtTimes.getText().toString();
            if (TextUtils.isEmpty(times)){
                Toast.makeText(CalllogActivity.this, "请输入需要生成通话记录的条数!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(myProvince) || TextUtils.isEmpty(myCity)){
                Toast.makeText(CalllogActivity.this, "请选择城市!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (myProvince.equals("海南")){
                Toast.makeText(CalllogActivity.this, "当前暂无该省份数据，请联系管理员!", Toast.LENGTH_SHORT).show();
                return;
            }

            readExcel(myProvince+"省.xlsx");
            //获取地址
//            switch (myProvince){
//                case "上海省":
//
//                    break;
//                case "云南省":
//
//                    break;
//                case "内蒙古省":
//
//                    break;
//                case "北京省":
//
//                    break;
//                case "吉林省":
//
//                    break;
//                case "四川省":
//
//                    break;
//                case "天津省":
//
//                    break;
//                case "宁夏省":
//
//                    break;
//                case "安徽省":
//
//                    break;
//                case "山东省":
//
//                    break;
//                case "山西省":
//
//                    break;
//                case "广东省":
//
//                    break;
//                case "广西省":
//
//                    break;
//                case "新疆省":
//
//                    break;
//                case "江苏省":
//
//                    break;
//                case "江西省":
//
//                    break;
//                case "河北省":
//
//                    break;
//                case "浙江省":
//
//                    break;
//                case "湖北省":
//
//                    break;
//                case "湖南省":
//
//                    break;
//                case "甘肃省":
//
//                    break;
//                case "福建省":
//
//                    break;
//                case "西藏省":
//
//                    break;
//                case "贵州省":
//
//                    break;
//                case "辽宁省":
//
//                    break;
//                case "重庆省":
//
//                    break;
//                case "陕西省":
//
//                    break;
//                case "青海省":
//
//                    break;
//                case "黑龙江省":
//
//                    break;
//                default:
//                    Toast.makeText(CalllogActivity.this, "当前城市电话区号尚未收录，请联系管理员!", Toast.LENGTH_SHORT).show();
//            }

        }

        public void selectCity(){
            //添加默认的配置，不需要自己定义，当然也可以自定义相关熟悉，详细属性请看demo
            CityConfig cityConfig = new CityConfig.Builder().build();
            mPicker.setConfig(cityConfig);

            //监听选择点击事件及返回结果
            mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                    myProvince = province.getName();
                    myCity = city.getName();
                    ACallBin.edtAddress.setText(province.getName()+"-"+city.getName());
                }

                @Override
                public void onCancel() {

                }
            });

            //显示
            mPicker.showCityPicker( );
        }

    }






    private void readExcel(String fileName) {
        List<String> phoneList = new ArrayList<>();

        //将符合条件的电话号码读取到phoneList中
        try {
            InputStream inputStream = getAssets().open(fileName);
            XSSFWorkbook workbook;
            if (fileName.endsWith(".xls")) {
                workbook = new XSSFWorkbook(inputStream);
            } else if (fileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            } else {
                return;
            }
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            for (int r = 0; r < rowsCount; r++) {
                Row row = sheet.getRow(r);
                CellValue v0 = formulaEvaluator.evaluate(row.getCell(0));
                CellValue v1 = formulaEvaluator.evaluate(row.getCell(1));
                if (v0.getStringValue().equals(myCity)){
                    phoneList.add(v1.getStringValue());
                }

                Log.d("Excel", "readExcel: " + v0.getStringValue() + "-" + v1.getStringValue());
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //从中随机选取几条来生成电话号码
        for (int i = 0; i<Integer.parseInt(ACallBin.edtNum.getText().toString()); i++){
                int index = getRandom(phoneList.size());
                String phone = phoneList.get(index)+getRandom(10)+getRandom(10)+getRandom(10)+getRandom(10);

                PhoneBean phoneBean = new PhoneBean();
                phoneBean.setNumber(phone);
                phoneBean.setDate(nowTime);

                int callTime = getRandom(180)+10;
                phoneBean.setDuration(String.valueOf(callTime));
                nowTime = nowTime + callTime * 1000 + 60000;

                phoneBean.setType("2");
                phoneBean.setTypenew("0");

                createCallLog(phoneBean);
        }

    }



    /**
     * 生成一条通话记录
     * @param phoneBean
     */
    private void createCallLog(PhoneBean phoneBean){
        ContentValues values = new ContentValues();
        values.put(CallLog.Calls.NUMBER, phoneBean.getNumber());
        values.put(CallLog.Calls.DATE, phoneBean.getDate());
        values.put(CallLog.Calls.DURATION, phoneBean.getDuration());
        values.put(CallLog.Calls.TYPE, phoneBean.getType());
        values.put(CallLog.Calls.NEW, phoneBean.getTypenew());
        getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
    }

    /**
     * 生成随机数
     * @param count
     * @return
     */
    private int getRandom(int count){
        Random r = new Random();
        return r.nextInt(count );
    }

    /**
     * 设置edittext hint字体大小
     * @param editText
     * @param hintText
     * @param size
     */
    private void setEditTextHintWithSize(EditText editText, String hintText, @Dimension int size) {
        if (!TextUtils.isEmpty(hintText)) {
            SpannableString ss = new SpannableString(hintText);
            //设置字体大小 true表示单位是sp
            AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size, true);
            ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            editText.setHint(new SpannedString(ss));
        }
    }





}