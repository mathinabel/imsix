package com.quyuanjin.imsix.contract.contractdetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.mbms.MbmsErrors;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.quyuanjin.imsix.Constant;
import com.quyuanjin.imsix.R;
import com.quyuanjin.imsix.chatsession.ChatAc;
import com.quyuanjin.imsix.login.User;
import com.quyuanjin.imsix.utils.ToastUtils;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class ContractDetailAc extends AppCompatActivity {
    private Button btnCheat;
    private String receverid;
    private TextView tvName;
    private TextView tvAccount5;
    private SimpleDraweeView ivHeader;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receverid= getIntent().getStringExtra("receverid");
       // ToastUtils.show(ContractDetailAc.this,"收到的uid是："+receverid);

        setContentView(R.layout.activity_user_info);
        initView();
        initDataFromNet();
        initBackListener();
    }
    private void initBackListener() {
        CommonTitleBar commonTitleBar=findViewById(R.id.titlebar5);
        commonTitleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    onBackPressed();
                }
            }
        });
    }

    private void initListener(User user) {
        btnCheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ContractDetailAc.this, ChatAc.class);
                intent.putExtra("receverid",receverid);
                intent.putExtra("por",user.getPortrait());
                intent.putExtra("name",user.getName());
                startActivity(intent);



                finish();
            }
        });
    }

    private void initView() {
        ivHeader = findViewById(R.id.ivHeader);
        tvName = findViewById(R.id.tvName6);
        tvAccount5 = findViewById(R.id.tvAccount6);
        btnCheat=findViewById(R.id.btnCheat);
    }

    private void initDataFromNet() {


        OkHttpUtils.post()
                .url(Constant.URL + "PullUserDetail")
                .addParams("userid",receverid)
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
                        if (user!=null){
                            if (!"".equals(user.getId())) {
                                tvAccount5.setText(user.getId());
                                tvName.setText(user.getName());
                                ivHeader.setImageURI(Uri.parse(user.getPortrait()));
                                initListener(user);
                            }

                        }


                        //     }

                        //    }).start();

                    }


                });
    }
}
