package com.quyuanjin.imsix.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.kongzue.dialog.v2.TipDialog;
import com.quyuanjin.imsix.Constant;
import com.quyuanjin.imsix.MainActivity;
import com.quyuanjin.imsix.R;
import com.quyuanjin.imsix.utils.SDialog;
import com.quyuanjin.imsix.utils.SharedPreferencesUtils;
import com.quyuanjin.imsix.utils.TimerHelper;
import com.quyuanjin.imsix.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;

public class LoginAndRegisterAc extends AppCompatActivity implements View.OnClickListener {
    private ImageView backgroundImage;
    private EditText phoneEditText;
    private EditText pwd;
    private Button login;
    private Button register;
    private TextView morning;
    private TextView littleTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login_or_register);
        initView();
        initListener();
        initBackGroundChange();
    }

    private void initBackGroundChange() {
        if (TimerHelper.TimeType() == Constant.time_late) {
            backgroundImage.setBackgroundResource(R.drawable.good_night_img);
            morning.setText(R.string.night);
            littleTitle.setText("晚上了");
        }
    }

    private void initListener() {
        backgroundImage.setOnClickListener(this);
        phoneEditText.setOnClickListener(this);
        pwd.setOnClickListener(this);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        littleTitle.setOnClickListener(this);
    }

    private void initView() {
        backgroundImage = findViewById(R.id.imageView);
        phoneEditText = findViewById(R.id.phoneEditText);
        pwd = findViewById(R.id.pwd);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        morning = findViewById(R.id.morning);
        littleTitle = findViewById(R.id.littleTitle);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView:
                break;
            case R.id.phoneEditText:
                break;


            case R.id.login:

                String userid = phoneEditText.getText().toString();
                String pwd2 = pwd.getText().toString();

                if (userid.equals("") || "".equals(pwd2)) {
                    ToastUtils.show(getApplicationContext(), "请正确填写内容");
                } else {
                    OkHttpUtils.post()
                            .url(Constant.URL + "login")
                            .addParams("userid", userid)
                            .addParams("pwd", pwd2)
                            .build().execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            SDialog.cancelSweetDialog(LoginAndRegisterAc.this,
                                    "哪里出问题了", "点击返回");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Gson gson = new Gson();
                            User user = gson.fromJson(response, User.class);
                            if (!"".equals(user.getId())){
                                SharedPreferencesUtils.setParam(getApplicationContext(), "name", user.getName());
                                SharedPreferencesUtils.setParam(getApplicationContext(), "des", user.getDescription());
                                SharedPreferencesUtils.setParam(getApplicationContext(), "sex", user.getSex());
                                SharedPreferencesUtils.setParam(getApplicationContext(), "portrait", user.getPortrait());
                                SharedPreferencesUtils.setParam(getApplicationContext(), "uuid", user.getToken());
                                SharedPreferencesUtils.setParam(getApplicationContext(), "userid", user.getId());
                                Intent intent = new Intent(LoginAndRegisterAc.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                TipDialog.show(LoginAndRegisterAc.this, "用户名或密码错误", TipDialog.SHOW_TIME_LONG, TipDialog.TYPE_ERROR);
                            }



                        }
                    });
                }
                break;
            case R.id.register:
                OkHttpUtils.post()
                        .url(Constant.URL + "register")
                        //      .params( )//
                        //     .headers()//
                        .build()//
                        .execute(
                                new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {

                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        Gson gson = new Gson();
                                        User user = gson.fromJson(response, User.class);
                                        SharedPreferencesUtils.setParam(getApplicationContext(), "name", user.getName());
                                        SharedPreferencesUtils.setParam(getApplicationContext(), "des", user.getDescription());
                                        SharedPreferencesUtils.setParam(getApplicationContext(), "sex", user.getSex());
                                        SharedPreferencesUtils.setParam(getApplicationContext(), "portrait", user.getPortrait());
                                        SharedPreferencesUtils.setParam(getApplicationContext(), "uuid", user.getToken());
                                        SharedPreferencesUtils.setParam(getApplicationContext(), "userid", user.getId());
                                        Intent intent = new Intent(LoginAndRegisterAc.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }


                                    @Override
                                    public void inProgress(float progress, long total, int id) {
                                        super.inProgress(progress, total, id);


                                    }
                                });
                SDialog.showSweetDialog(LoginAndRegisterAc.this);

                break;
        }
    }

}
