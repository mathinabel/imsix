package com.quyuanjin.imsix.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kongzue.dialog.v2.SelectDialog;
import com.lqr.optionitemview.OptionItemView;
import com.quyuanjin.imsix.MainActivity;
import com.quyuanjin.imsix.R;
import com.quyuanjin.imsix.login.LoginAndRegisterAc;
import com.quyuanjin.imsix.splash.SplashAc;
import com.quyuanjin.imsix.utils.SharedPreferencesUtils;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

public class SettingAc  extends AppCompatActivity {
    private OptionItemView oivExit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initBackListener();
    }

    private void initBackListener() {
        CommonTitleBar commonTitleBar = findViewById(R.id.titlebar10);
        commonTitleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    onBackPressed();
                }
            }
        });
    }

    private void initView() {
        oivExit=findViewById(R.id.oivExit);
        oivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectDialog.show(SettingAc.this, "提示", "是否退出登录", "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferencesUtils.setParam(getApplicationContext(), "name","");
                        SharedPreferencesUtils.setParam(getApplicationContext(), "des", "");
                        SharedPreferencesUtils.setParam(getApplicationContext(), "sex", "");
                        SharedPreferencesUtils.setParam(getApplicationContext(), "portrait", "");
                        SharedPreferencesUtils.setParam(getApplicationContext(), "uuid", "");
                        SharedPreferencesUtils.setParam(getApplicationContext(), "userid", "");

                        startActivity(new Intent(SettingAc.this, MainActivity.class));

                        finish();

                    }
                }, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            }
        });
    }
}
