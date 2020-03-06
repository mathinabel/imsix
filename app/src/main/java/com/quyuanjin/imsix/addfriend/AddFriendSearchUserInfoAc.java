package com.quyuanjin.imsix.addfriend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quyuanjin.imsix.Constant;
import com.quyuanjin.imsix.MainActivity;
import com.quyuanjin.imsix.R;
import com.quyuanjin.imsix.app.App;
import com.quyuanjin.imsix.contract.EventBusContract;
import com.quyuanjin.imsix.contract.PojoContract;
import com.quyuanjin.imsix.login.LoginAndRegisterAc;
import com.quyuanjin.imsix.login.User;
import com.quyuanjin.imsix.utils.SDialog;
import com.quyuanjin.imsix.utils.SharedPreferencesUtils;
import com.quyuanjin.imsix.utils.TimerHelper;
import com.quyuanjin.imsix.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import netty.NettyLongChannel;
import netty.ProtoConstant;
import okhttp3.Call;

public class AddFriendSearchUserInfoAc extends AppCompatActivity {
    private Button btnAddFriend;
    private String serachthing;
    private TextView tvName;
    private TextView tvAccount5;
    private SimpleDraweeView ivHeader;
    String name;
    String des;
    String sex;
    String por;
    String userid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_search_user_info);


        name = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "name", "");
        des = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "des", "");
        sex = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "sex", "");
        por = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "portrait", "");
        userid = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "userid", "");

        serachthing = getIntent().getStringExtra("search");
        ToastUtils.show(this, serachthing);
        initView();
        initDataFromNet();
    }

    private void initListener(User user, String name) {
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //将自身信息打包发送
                PojoPullUnReadAddFriendMsgFromNet pojoPullUnReadAddFriendMsgFromNet = new PojoPullUnReadAddFriendMsgFromNet(null, AddFriendSearchUserInfoAc.this.name,name , sex, userid, user.getId(), "", "", "", por, Constant.addfriend_send_but_wait_to_answer, "");
                Gson gson = new Gson();
                NettyLongChannel.sendMsg(ProtoConstant.ADD_FRIEND, gson.toJson(pojoPullUnReadAddFriendMsgFromNet));

                //将信息存储，状态为等待对方答复
                //  App.getDaoSession().getPojoPullUnReadAddFriendMsgFromNetDao().insert(pojoPullUnReadAddFriendMsgFromNet);
                new SweetAlertDialog(AddFriendSearchUserInfoAc.this)
                        .setTitleText("请求已发送").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    }
                }).show();

            }
        });
    }

    private void initView() {
        ivHeader = findViewById(R.id.ivHeader1);
        btnAddFriend = findViewById(R.id.btnAddFriend);
        tvName = findViewById(R.id.tvName5);
        tvAccount5 = findViewById(R.id.tvAccount5);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void initDataFromNet() {


        OkHttpUtils.post()
                .url(Constant.URL + "PullUserDetail")
                .addParams("userid", serachthing)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //放入多线程，运行完eventbus通知本界面
                        //   new Thread(new Runnable() {
                        //     @Override
                        //     public void run() {
                        //解析传过来的json进行本地存储

                        Gson gson = new Gson();
                        User user = gson.fromJson(response, User.class);
                        if (!"".equals(user.getId())) {
                            tvAccount5.setText(user.getId());
                            tvName.setText(user.getName());
                            ivHeader.setImageURI(Uri.parse(user.getPortrait()));
                            initListener(user,user.getName());
                        }


                        //     }

                        //    }).start();

                    }


                });
    }

}
