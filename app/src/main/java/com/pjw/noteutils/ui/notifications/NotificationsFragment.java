package com.pjw.noteutils.ui.notifications;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.pjw.noteutils.R;
import com.pjw.noteutils.base.BaseFragment;
import com.pjw.noteutils.bean.CodeBean;
import com.pjw.noteutils.data.NotePreference;
import com.pjw.noteutils.data.ShareConst;
import com.pjw.noteutils.databinding.FragmentDashboardBinding;
import com.pjw.noteutils.databinding.FragmentNotificationsBinding;
import com.pjw.noteutils.ui.dashboard.DashboardFragment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class NotificationsFragment extends BaseFragment {

    private FragmentNotificationsBinding fNotiBin;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        fNotiBin =  DataBindingUtil.inflate(inflater, R.layout.fragment_notifications,container,false);
        fNotiBin.setClick(new NotificationsFragment.ClickProxy());

        init();

        return fNotiBin.getRoot();
    }

    private void init(){
        setEditTextHintWithSize(fNotiBin.edtCode,getString(R.string.noti_code),12);
    }

    public class ClickProxy {
        public void vertifyCode() {
            if (TextUtils.isEmpty(fNotiBin.edtCode.getText().toString())){
                Toast.makeText(getActivity(), "请输入授权码!", Toast.LENGTH_SHORT).show();
                return;
            }

//            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");// HH:mm:ss
//            //获取当前时间
//            Date date = new Date(System.currentTimeMillis());
//            if (fNotiBin.edtCode.getText().toString().equals(simpleDateFormat.format(date))){
//
//            }else {
//                Toast.makeText(getActivity(), "授权码错误!", Toast.LENGTH_SHORT).show();
//            }

            BmobQuery<CodeBean> bmobQuery = new BmobQuery<CodeBean>();
            bmobQuery.getObject(fNotiBin.edtCode.getText().toString(), new QueryListener<CodeBean>() {
                @Override
                public void done(CodeBean object, BmobException e) {
                    if(e==null){
                        //成功的操作
                        NotePreference notePreference = new NotePreference(getContext());
                        notePreference.writeInt(ShareConst.IsVerifyCode,2);

                        CodeBean p2 = new CodeBean();
                        p2.setObjectId(fNotiBin.edtCode.getText().toString());
                        p2.delete(new UpdateListener() {

                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Toast.makeText(getActivity(), "验证成功!" , Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getActivity(), "验证成功!但发生未知错误" , Toast.LENGTH_SHORT).show();
                                }
                            }

                        });

                    }else{
                        Toast.makeText(getActivity(), "验证失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
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

    @NonNull
    public String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}