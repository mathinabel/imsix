
package com.quyuanjin.imsix.fragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lqr.optionitemview.OptionItemView;
import com.quyuanjin.imsix.R;
import com.quyuanjin.imsix.myinfoac.MyInfoAc;
import com.quyuanjin.imsix.setting.SettingAc;
import com.quyuanjin.imsix.utils.SharedPreferencesUtils;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by mxn on 2016/12/13.
 * MenuListFragment
 */

public class MenuListFragment extends Fragment {

    private ImageView ivMenuUserProfilePhoto;
    private View view;
    private AutoLinearLayout llMyInfo;
    private OptionItemView oivSetting;
    private SimpleDraweeView my_image_view;
    private TextView tvName;
    private TextView tvAccount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container, false);
        initView();
        initListener();

        // initeventBus();


        return view;
    }

    private void initeventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void initListener() {
        llMyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyInfoAc.class);
                startActivity(intent);
            }
        });
        oivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getContext(), SettingAc.class);
                startActivity(intent2);

            }
        });

    }

    private void initView() {
        llMyInfo = view.findViewById(R.id.llMyInfo);
        oivSetting = view.findViewById(R.id.oivSetting);

        my_image_view = view.findViewById(R.id.my_image_view);
        tvName = view.findViewById(R.id.tvName);
        tvAccount = view.findViewById(R.id.tvAccount);
        tvName.setText((String) SharedPreferencesUtils.getParam(getContext().getApplicationContext(), "name", ""));
        tvAccount.setText((String) SharedPreferencesUtils.getParam(getContext().getApplicationContext(), "userid", ""));
    }

    @Override
    public void onStart() {
        super.onStart();
        String s = (String) SharedPreferencesUtils.getParam(getContext().getApplicationContext(), "CutPath", "");
        if (!("".equals(s))) {
            my_image_view.setImageURI(Uri.parse(s));

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    /*    if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }*/
    }
}
