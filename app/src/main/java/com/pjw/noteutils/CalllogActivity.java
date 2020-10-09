package com.pjw.noteutils;

import androidx.annotation.Dimension;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.pjw.noteutils.bean.PhoneBean;
import com.pjw.noteutils.databinding.ActivityCalllogBinding;
import com.pjw.noteutils.ui.dashboard.DashboardFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CalllogActivity extends AppCompatActivity {

    private ActivityCalllogBinding ACallBin;
    private List<String> phoneList = new ArrayList<>();
    private long nowTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ACallBin = DataBindingUtil.setContentView(this, R.layout.activity_calllog);
        ACallBin.setClick(new ClickProxy());
        init();
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

            //随机生成电话号码
            String[] phoneHead = {"152","199","189","135","138","158","181","156","187","186","136","199"};
            String[] phoneArea = {"0281","0281","0281","0281","0281","0281","0281","0281","0281","0281","0281","0281","0281","0281","0281","0281"};

            for (int i = 0; i<Integer.parseInt(times); i++){
                String head = phoneHead[getRandom(12)];
                String area = phoneArea[getRandom(16)];
                String phone = head+area+getRandom(10)+getRandom(10)+getRandom(10)+getRandom(10);

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