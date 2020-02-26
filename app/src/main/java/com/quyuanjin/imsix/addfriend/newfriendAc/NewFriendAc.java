package com.quyuanjin.imsix.addfriend.newfriendAc;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quyuanjin.imsix.Constant;
import com.quyuanjin.imsix.R;
import com.quyuanjin.imsix.addfriend.PojoPullUnReadAddFriendMsgFromNet;
import com.quyuanjin.imsix.app.App;
import com.quyuanjin.imsix.recentmsg.EventBusPojoRecement;
import com.quyuanjin.imsix.recentmsg.PojoRecementMsg;
import com.quyuanjin.imsix.utils.SharedPreferencesUtils;
import com.quyuanjin.imsix.utils.ToastUtils;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class NewFriendAc extends AppCompatActivity {
    private NewFriendAdapter newFriendAdapter;
    private ArrayList<PojoPullUnReadAddFriendMsgFromNet> entityList = new ArrayList<>();
    private SwipeRecyclerView rvNewFriend;
    private String userid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userid = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "userid", "");
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_new_friend);
        initBackListener();
        initDatamNet();
        initRecyclerView();
    }

    private void initBackListener() {
        CommonTitleBar commonTitleBar=findViewById(R.id.titlebar6);
        commonTitleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    onBackPressed();
                }
            }
        });
    }

    private void initRecyclerView() {
        rvNewFriend = findViewById(R.id.rvNewFriend);
        newFriendAdapter = new NewFriendAdapter(entityList, this);
        rvNewFriend.setLayoutManager(new LinearLayoutManager(this));
        rvNewFriend.setAdapter(newFriendAdapter);
        newFriendAdapter.notifyDataSetChanged();
    }

    private void initDataFromLocal() {

    }

    private void initDatamNet() {
        // App.getDaoSession().getPojoRecementMsgDao().deleteAll();

        OkHttpUtils.post()
                .url(Constant.URL + "PullUnReadAddFriendReq")
                .addParams("userid", userid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //放入多线程，运行完eventbus通知本界面
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //如果本地有，先删除本地
                                // App.getDaoSession().getPojoRecementMsgDao().deleteAll();

                                //解析传过来的json进行本地存储
                                Gson gson = new Gson();
                                List<PojoPullUnReadAddFriendMsgFromNet> list = gson.fromJson(response, new TypeToken<List<PojoPullUnReadAddFriendMsgFromNet>>() {
                                }.getType());

                                entityList.addAll(list);
                                EventBus.getDefault().post(new Wocao());

                            }

                        }).start();

                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)//
    public void Refresh(Wocao wocao) {
        newFriendAdapter.notifyDataSetChanged();

    }
}
