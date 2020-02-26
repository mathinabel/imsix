package com.quyuanjin.imsix.contract;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allenliu.sidebar.ISideBarSelectCallBack;
import com.allenliu.sidebar.SideBar;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quyuanjin.imsix.Constant;
import com.quyuanjin.imsix.R;
import com.quyuanjin.imsix.addfriend.newfriendAc.NewFriendAc;
import com.quyuanjin.imsix.app.App;
import com.quyuanjin.imsix.contract.contractdetail.ContractDetailAc;
import com.quyuanjin.imsix.contract.utils.PinYinSortUtils;
import com.quyuanjin.imsix.utils.SharedPreferencesUtils;
import com.quyuanjin.imsix.utils.ToastUtils;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.OnItemLongClickListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yanzhenjie.recyclerview.widget.DefaultItemDecoration;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eventbus.ClienthandlerBus.TellContr;
import eventbus.ClienthandlerBus.TellContractAddFirendMsgBack;
import eventbus.ClienthandlerBus.TellContractFragment;
import okhttp3.Call;


public class ContractFragment extends Fragment {
    private View view;
    private SimpleDraweeView simpleDraweeViewAddFriendImg;
    private SimpleDraweeView simpleDraweeViewAddGroupImg;
    private SwipeRecyclerView contractRecyclerView;
    private ArrayList<PojoContract> entityList = new ArrayList<>();
    private HashMap<String, Integer> locMap = new HashMap<>();

    private ContractAdapter contractAdapter;
    private SideBar bar;
    private AutoLinearLayout llNewFriend;
private TextView tvNewFriendUnread;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private String uuid;
    private String userid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        view = inflater.inflate(R.layout.fragment_contract, container, false);
        uuid = (String) SharedPreferencesUtils.getParam(view.getContext().getApplicationContext(), "uuid", "");
        userid = (String) SharedPreferencesUtils.getParam(view.getContext().getApplicationContext(), "userid", "");


        initContractHeader();
        initDataFromNet();
     //   initDataFromLocal();
        initRecyclerView();

        initAddFriendView();
        initSideBar();

        //initScrollView();
        return view;

    }

    private void initDataFromNet() {

        //如果本地有，先删除本地
     //  App.getDaoSession().getPojoContractDao().deleteAll();
        OkHttpUtils.post()
                .url(Constant.URL + "contract")
                .addParams("userid", userid)
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
                        ArrayList<PojoContract> list2 = gson.fromJson(response, new TypeToken<List<PojoContract>>() {
                        }.getType());
                     /*   for (PojoContract pojoContract : list2) {
                            pojoContract.setId(null);
                            App.getDaoSession().getPojoContractDao().insert(pojoContract);
                        }*/

                        EventBus.getDefault().post(new EventBusContract(list2));

                        //     }

                        //    }).start();

                    }


                });
    }

 /*   private void initScrollView() {
        scroll_view=view.findViewById(R.id.scroll_view);
        scroll_view.addOnViewStickyListener(new StickyNestedScrollView.OnViewStickyListener() {
            @Override
            public void onSticky(View view) {

            }

            @Override
            public void onUnSticky(View view) {

            }
        });

    }*/

    private void initAddFriendView() {
        llNewFriend = view.findViewById(R.id.llNewFriend);
        tvNewFriendUnread=view.findViewById(R.id.tvNewFriendUnread);
        llNewFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewFriendAc.class);
                startActivity(intent);
            }
        });
    }

    private void initSideBar() {
        bar = (SideBar) view.findViewById(R.id.bar);
        bar.setOnStrSelectCallBack(new ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                //    Toast.makeText(getContext(),String.valueOf(index),Toast.LENGTH_SHORT).show();
                //   Log.d("tag1", String.valueOf(index) + "" + selectStr);

                if (locMap.containsKey(selectStr)) {

                    if (locMap.get(selectStr) != null) {
                        int pos = locMap.get(selectStr);
                        smoothMoveToPosition(contractRecyclerView, pos);


                    }

                }

            }
        });
    }

    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;

    /**
     * 滑动到指定位置
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前，使用smoothScrollToPosition
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后，最后一个可见项之前
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                // smoothScrollToPosition 不会有效果，此时调用smoothScrollBy来滑动到指定位置
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }

    private void initDataFromLocal(ArrayList<PojoContract> list2) {

        //List<PojoContract> pojoContracts = App.getDaoSession().getPojoContractDao().loadAll();
        //entityList.addAll(pojoContracts);
      //  entityList.addAll(list2);

        PinYinSortUtils pinYinSortUtil = new PinYinSortUtils(list2);
        entityList.addAll(pinYinSortUtil.sortAndAdd());

        locMap = pinYinSortUtil.getPosList();


    }

    private void initRecyclerView() {
        contractRecyclerView = view.findViewById(R.id.contract_recycler);
        contractRecyclerView.setNestedScrollingEnabled(false);
        contractAdapter = new ContractAdapter(entityList, getContext());
        contractRecyclerView.addItemDecoration(new DefaultItemDecoration(ContextCompat.getColor(getContext(), R.color.main_bg1)));
        contractRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mShouldScroll && RecyclerView.SCROLL_STATE_IDLE == newState) {
                    mShouldScroll = false;
                    smoothMoveToPosition(contractRecyclerView, mToPosition);
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //  contractRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        contractRecyclerView.setLayoutManager(layoutManager);
        contractRecyclerView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int adapterPosition) {
            }
        });

        contractRecyclerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int adapterPosition) {
           //     ToastUtils.show(getContext(), "dianjile" + adapterPosition+entityList.get(adapterPosition).getTag());
               if (!(Constant.TAG_STICKY.equals(entityList.get(adapterPosition).getTag()))) {

                    Intent intent = new Intent(getContext(), ContractDetailAc.class);
                    intent.putExtra("receverid", entityList.get(adapterPosition).getContractUserId());
                    getContext().startActivity(intent);

              }
            }
        });

        contractRecyclerView.setAdapter(contractAdapter);
        contractAdapter.notifyDataSetChanged();
    }

    private void initContractHeader() {
        simpleDraweeViewAddFriendImg = view.findViewById(R.id.ivNewFriend);
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(R.mipmap.ic_new_friend))
                .build();
        simpleDraweeViewAddFriendImg.setImageURI(uri);

        simpleDraweeViewAddGroupImg = view.findViewById(R.id.ivNewGroup);
        Uri uri2 = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(R.mipmap.ic_group_cheat))
                .build();
        simpleDraweeViewAddGroupImg.setImageURI(uri2);
    }

    public ContractFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)//
    public void RefreshContractData(EventBusContract eventBusContract) {

        initDataFromLocal(eventBusContract.getList2());

        contractAdapter.notifyDataSetChanged();


    }

    @Subscribe(threadMode = ThreadMode.MAIN)//
    public void RefreshAddData(TellContractFragment tellContractFragment) {
        ToastUtils.show(getContext(), "有新好友");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)//
    public void RefreshAddDataNet(TellContractAddFirendMsgBack addFirendMsgBack) {
        //从网络上获取
        initDataFromNet();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)//
    public void ReAddFrNet(TellContr contr) {
        //这里应该是显示红点，表示未读加好友消息
        tvNewFriendUnread.setVisibility(View.VISIBLE);
        tvNewFriendUnread.setText("1");
    }
}
