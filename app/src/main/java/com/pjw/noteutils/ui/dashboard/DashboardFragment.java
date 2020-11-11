package com.pjw.noteutils.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.pjw.noteutils.R;
import com.pjw.noteutils.base.BaseFragment;
import com.pjw.noteutils.data.NotePreference;
import com.pjw.noteutils.data.ShareConst;
import com.pjw.noteutils.databinding.FragmentDashboardBinding;


public class DashboardFragment extends BaseFragment {

    private FragmentDashboardBinding fDashBin;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        fDashBin =  DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard,container,false);
        fDashBin.setClick(new ClickProxy());
        return fDashBin.getRoot();
    }


    public class ClickProxy {
        public void goCallLog() {
            NotePreference notePreference = new NotePreference(getContext());
            int verify = notePreference.readInt(ShareConst.IsVerifyCode);
            if (verify == 2){
                nav().navigate(R.id.action_DashboardFragment_to_CalllogActivity);
            }else {
                Toast.makeText(getActivity(), "请先验证授权码!" , Toast.LENGTH_SHORT).show();
            }
        }
    }

}