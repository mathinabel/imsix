package com.quyuanjin.imsix.recentmsg;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quyuanjin.imsix.Constant;
import com.quyuanjin.imsix.MainActivity;
import com.quyuanjin.imsix.app.App;
import com.quyuanjin.imsix.chatsession.ChatAc;
import com.quyuanjin.imsix.R;
import com.quyuanjin.imsix.chatsession.Msg;
import com.quyuanjin.imsix.login.LoginAndRegisterAc;
import com.quyuanjin.imsix.utils.SharedPreferencesUtils;
import com.quyuanjin.imsix.utils.ToastUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.impl.ScrollBoundaryDeciderAdapter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.OnItemLongClickListener;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import eventbus.ClienthandlerBus.TellReceMent;
import eventbus.ClienthandlerBus.TellRecementAddFirendMsgBack;
import json.User;
import okhttp3.Call;

public class RecementFragment extends Fragment {

    private View view;//定义view用来设置fragment的layout
    public SwipeRecyclerView mmSwipeRecyclerView;//定义RecyclerView
    //定义以entity实体类为对象的数据集合
    private ArrayList<PojoRecementMsg> entityList = new ArrayList<>();
    //自定义recyclerveiw的适配器
    private RecentMsgRecycleAdapter mRecentMsgRecycleAdapter;


    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int position) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_70);

            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            // 添加左侧的，如果不添加，则左侧不会出现菜单。
            {


                SwipeMenuItem closeItem = new SwipeMenuItem(getContext()).setBackground(R.drawable.selector_green)
                        .setImage(R.drawable.ic_action_close)
                        .setWidth(width)
                        .setHeight(height);
                swipeLeftMenu.addMenuItem(closeItem); // 添加菜单到左侧。
            }

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
         /*   {
                SwipeMenuItem deleteItem = new SwipeMenuItem(context).setBackground(R.drawable.selector_red)
                        .setImage(R.drawable.ic_action_delete)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。

                SwipeMenuItem addItem = new SwipeMenuItem(context).setBackground(R.drawable.selector_green)
                        .setText("添加")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(addItem); // 添加菜单到右侧。
            }*/
        }
    };
    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private OnItemMenuClickListener mMenuItemClickListener = new OnItemMenuClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {

                Toast.makeText(getContext(), "list第" + position + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT)
                        .show();
            } else if (direction == SwipeRecyclerView.LEFT_DIRECTION) {
                Toast.makeText(getContext(), "list第" + position + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };

    public RecementFragment() {
    }

    private String uuid;
    private String userid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recent_message, container, false);
        //先从本地获取，然后通知界面填充数据，再从网络加载
        uuid = (String) SharedPreferencesUtils.getParam(view.getContext().getApplicationContext(), "uuid", "");
        userid = (String) SharedPreferencesUtils.getParam(view.getContext().getApplicationContext(), "userid", "");
        //  App.getDaoSession().getPojoRecementMsgDao().deleteAll();

     //   initDataFromLocal();
        initRecyclerView();
        initDataFromNet();
        initRefresh();
        return view;
    }


    private void initRefresh() {

        final RefreshLayout refreshLayout = view.findViewById(R.id.refreshLayout2);
        refreshLayout.setEnableRefresh(false);//必须关闭
        refreshLayout.setEnableAutoLoadMore(true);//必须关闭
        refreshLayout.setEnableNestedScroll(false);//必须关闭
        refreshLayout.setEnableScrollContentWhenLoaded(true);//必须关闭
        refreshLayout.getLayout().setScaleY(-1);//必须设置

        refreshLayout.setScrollBoundaryDecider(new ScrollBoundaryDeciderAdapter() {
            @Override
            public boolean canLoadMore(View content) {
                return super.canRefresh(content);//必须替换
            }
        });

        //监听加载，而不是监听 刷新
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                       // initDataFromNet();
                        refreshLayout.finishLoadMore();
                    }
                }, 1000);
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initDataFromNet();
            }
        });
    }


    private void initRecyclerView() {


        //获取RecyclerView
        mmSwipeRecyclerView = view.findViewById(R.id.rvRecentMessage);
        //创建adapter
        mRecentMsgRecycleAdapter = new RecentMsgRecycleAdapter(getContext(), entityList);

        mmSwipeRecyclerView.setSwipeMenuCreator(swipeMenuCreator);

        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局
        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        mmSwipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


        mmSwipeRecyclerView.setOnItemMenuClickListener(mMenuItemClickListener);

        mmSwipeRecyclerView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int adapterPosition) {

            }
        });

        mmSwipeRecyclerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int adapterPosition) {
                Intent intent = new Intent(getContext(), ChatAc.class);
                intent.putExtra("receverid", entityList.get(adapterPosition).getYourUserId());
                getContext().startActivity(intent);
            }
        });


        //给RecyclerView设置adapter
        mmSwipeRecyclerView.setAdapter(mRecentMsgRecycleAdapter);


        mRecentMsgRecycleAdapter.notifyDataSetChanged();
    }


    private void initDataFromLocal() {

      /*  List<PojoRecementMsg> pojoRecementMsgList = App.getDaoSession().getPojoRecementMsgDao().queryBuilder().build().list();
        if (pojoRecementMsgList != null) {
            //这里因该按时间排序展示，现在不做
            entityList.addAll(pojoRecementMsgList);
        }*/


    }

    private void initDataFromNet() {

        OkHttpUtils.post()
                .url(Constant.URL + "chating")
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
                         //刷新时，数据同步
                               entityList.clear();

                               //解析传过来的json进行本地存储
                                Gson gson = new Gson();
                                List<PojoRecementMsg> list = gson.fromJson(response, new TypeToken<List<PojoRecementMsg>>() {
                                }.getType());
                            /*   int k=entityList.size();
                                for (int j=0;j<list.size();j++){
                                    if (k==0){
                                        entityList.addAll(list);
                                    }else {
                                        for (int i =0;i<k;i++){
                                            if (entityList.get(i).getName().equals(list.get(j).getName())){
                                                entityList.add(list.get(j));
                                            }
                                        }
                                    }
                                }*/
                               entityList.addAll(list);
                                //  App.getDaoSession().getPojoRecementMsgDao().insertInTx(list);
                               EventBus.getDefault().post(new EventBusPojoRecement());

                            }

                      }).start();

               /*


                        //本地存储

                        //TODO 这应该放到eventbus里面执行，且，是收到了本界面的通知
                        initDataFromLocal();//从本地数据库重新加载
                        // 通知更新
                        mRecentMsgRecycleAdapter.notifyDataSetChanged();
*/
                  }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        initDataFromNet();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)//
    public void RefreshDataLocal(EventBusPojoRecement eventBusPojoRecement) {
        //   initDataFromLocal();
        mRecentMsgRecycleAdapter.notifyDataSetChanged();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)//
    public void ReDataNet(TellRecementAddFirendMsgBack recementAddFirendMsgBack) {
        initDataFromNet();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)//
    public void ReAddFrNet(TellReceMent mainac) {
        initDataFromNet();
       //ToastUtils.show( getContext(),"收到一条来自"+mainac.getMsg().getSendID()+"的消息，消息为："+mainac.getMsg().getMsg());
     //   ToastUtils.show( getContext(),"收到一条新消息");

    }
}
