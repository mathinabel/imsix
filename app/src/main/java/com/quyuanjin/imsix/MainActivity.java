package com.quyuanjin.imsix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.quyuanjin.imsix.addfriend.AddFriendAc;
import com.quyuanjin.imsix.addfriend.AddFriendSearchUserInfoAc;
import com.quyuanjin.imsix.app.App;
import com.quyuanjin.imsix.chatsession.ChatAc;
import com.quyuanjin.imsix.chatsession.Msg;
import com.quyuanjin.imsix.contract.ContractFragment;
import com.quyuanjin.imsix.contract.PojoContract;
import com.quyuanjin.imsix.fragment.MenuListFragment;
import com.quyuanjin.imsix.login.LoginAndRegisterAc;
import com.quyuanjin.imsix.recentmsg.RecementFragment;
import com.quyuanjin.imsix.utils.OkGoUpdateHttpUtil;
import com.quyuanjin.imsix.utils.SharedPreferencesUtils;
import com.quyuanjin.imsix.utils.ToastUtils;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;
import com.vector.update_app.utils.ColorUtil;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eventbus.ClienthandlerBus.TellContractAddFirendMsgBack;
import kr.co.namee.permissiongen.PermissionGen;
import okhttp3.Call;
import service.NettyService;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SCAN = 9;
    private CommonTabLayout mTabLayout_1;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private int[] mIconUnselectIds = {
            R.mipmap.tab_speech_unselect,
            R.mipmap.tab_contact_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.tab_speech_select,
            R.mipmap.tab_contact_select};
    private String[] mTitles = {"消息", "联系人"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<Fragment> mFragments2 = new ArrayList<>();
    ViewPager mViewPager;
    MyPagerAdapter myPagerAdapter;
    CommonTitleBar commonTitleBar;
    FlowingDrawer mDrawer;
    TopRightMenu mTopRightMenu;
    private String uuid;
    private static String userid;
    private SimpleDraweeView simpleDraweeView;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        name = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "name", "");
        uuid = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "uuid", "");
        userid = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "userid", "");
           if (("".equals(uuid)) || ("".equals(userid))) {
            Intent intent = new Intent(MainActivity.this, LoginAndRegisterAc.class);
            startActivity(intent);

            this.finish();
        }

//建立长连接
        Intent intent = new Intent(this, NettyService.class);
        intent.putExtra("command", "longConnect");
        startService(intent);
        setContentView(R.layout.activity_main);

        mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    Log.i("MainActivity", "Drawer STATE_CLOSED");
                }
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                Log.i("MainActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
            }
        });
        mTabLayout_1 = this.findViewById(R.id.tl_1);
        mViewPager = this.findViewById(R.id.vp_2);
        commonTitleBar = findViewById(R.id.commonTitleBar);

        View leftCustomLayout = commonTitleBar.getLeftCustomView();
        View rightCustomLayout = commonTitleBar.getRightCustomView();
        simpleDraweeView = leftCustomLayout.findViewById(R.id.my_image_view);

        simpleDraweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.toggleMenu();

            }
        });
        ImageView simpleDraweeView2 = rightCustomLayout.findViewById(R.id.selected_search);
        simpleDraweeView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTopRightMenu = new TopRightMenu(MainActivity.this);

//添加菜单项
                List<MenuItem> menuItems = new ArrayList<>();
                menuItems.add(new MenuItem(R.mipmap.ic_around_blue, "加好友"));
               menuItems.add(new MenuItem(R.mipmap.ic_scan_blue, "扫一扫"));

                mTopRightMenu
                        .setHeight(480)     //默认高度480
                        .setWidth(320)      //默认宽度wrap_content
                        .showIcon(true)     //显示菜单图标，默认为true
                        .dimBackground(true)        //背景变暗，默认为true
                        .needAnimationStyle(true)   //显示动画，默认为true
                        .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                        .addMenuList(menuItems)
                        .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                            @Override
                            public void onMenuItemClick(int position) {
                                if (position == 0) {
                                    Intent intent = new Intent(MainActivity.this, AddFriendAc.class);
                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                }else if (position==1){

                                    PermissionGen.with(MainActivity.this)
                                            .addRequestCode(100)
                                            .permissions(
                                                    Manifest.permission.CAMERA)
                                            .request();


                                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                                    /*ZxingConfig是配置类
                                     *可以设置是否显示底部布局，闪光灯，相册，
                                     * 是否播放提示音  震动
                                     * 设置扫描框颜色等
                                     * 也可以不传这个参数
                                     * */
                                    ZxingConfig config = new ZxingConfig();
                                    config.setPlayBeep(true);//是否播放扫描声音 默认为true
                                    config.setShake(true);//是否震动  默认为true
                                    config.setDecodeBarCode(true);//是否扫描条形码 默认为true
                                    config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
                                    config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
                                    config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
                                    config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
                                    intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                                }


                            }
                        })
                        .showAsDropDown(simpleDraweeView2, -225, 100);    //带偏移量
//      		.showAsDropDown(moreBtn)
            }
        });

        ImageView simpleDraweeView3 = rightCustomLayout.findViewById(R.id.selected_search2);
        simpleDraweeView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddFriendAc.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

     Uri uri = Uri.parse("");
        simpleDraweeView.setImageURI(uri);

        int version = android.os.Build.VERSION.SDK_INT;
       /* if (version < 21) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = 80;

            commonTitleBar.setLayoutParams(layoutParams);
        }*/


        mFragments.add(new RecementFragment());
        mFragments.add(new ContractFragment());


        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        mTabLayout_1.setTabData(mTabEntities);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(myPagerAdapter);
      //  mTabLayout_1.showMsg(1, 5);
        mTabLayout_1.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mTabLayout_1.hideMsg(position);
                mViewPager.setCurrentItem(position);
                switch (position) {
                    case 0:
                        commonTitleBar.getCenterTextView().setText("消息");
                        break;
                    case 1:
                        commonTitleBar.getCenterTextView().setText("联系人");
                }
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {
                    //  mTabLayout_1.showMsg(0, 10);
//                    UnreadMsgUtils.show(mTabLayout_2.getMsgView(0), mRandom.nextInt(100) + 1);


                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout_1.setCurrentTab(position);
                mTabLayout_1.hideMsg(position);
                mViewPager.setCurrentItem(position);
                switch (position) {
                    case 0:
                        commonTitleBar.getCenterTextView().setText("消息");
                        break;
                    case 1:
                        commonTitleBar.getCenterTextView().setText("联系人");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupMenu();
        checkVersion();
        PermissionGen.with(MainActivity.this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.CAMERA)
                .request();
    }

    private void setupMenu() {
        FragmentManager fm = getSupportFragmentManager();
        MenuListFragment mMenuFragment = (MenuListFragment) fm.findFragmentById(R.id.id_container_menu);
        if (mMenuFragment == null) {
            mMenuFragment = new MenuListFragment();
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment).commit();
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        String s = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "CutPath", "");
        if (!("".equals(s))) {
            simpleDraweeView.setImageURI(Uri.parse(s));

        }
    }

    @Override
    public void onBackPressed() {
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.addCategory(Intent.CATEGORY_HOME);
        startActivity(launcherIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }



    private void checkVersion() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        String updateURL = "http://www.samuer.top/json";
        Map<String, String> params = new HashMap<String, String>();

        //  params.put("appKey", "ab55ce55Ac4bcP408cPb8c1Aaeac179c5f6f");
        params.put("appVersion", getLocalVersionName(this));
        //    params.put("key1", "value2");
        //   params.put("key2", "value3");

        new UpdateAppManager
                .Builder()
                //必须设置，当前Activity
                .setActivity(this)
                //必须设置，实现httpManager接口的对象
                .setHttpManager(new OkGoUpdateHttpUtil())
                //必须设置，更新地址
                .setUpdateUrl(updateURL)

                //以下设置，都是可选
                //设置请求方式，默认get
                .setPost(false)
                //添加自定义参数，默认version=1.0.0（app的versionName）；apkKey=唯一表示（在AndroidManifest.xml配置）
                .setParams(params)
                //设置点击升级后，消失对话框，默认点击升级后，对话框显示下载进度
                //.hideDialogOnDownloading()
                //设置头部，不设置显示默认的图片，设置图片后自动识别主色调，然后为按钮，进度条设置颜色
                .setTopPic(R.mipmap.top_8)
                //为按钮，进度条设置颜色，默认从顶部图片自动识别。
                .setThemeColor(ColorUtil.getRandomColor())
                //设置apk下砸路径，默认是在下载到sd卡下/Download/1.0.0/test.apk
                .setTargetPath(path)
                //设置appKey，默认从AndroidManifest.xml获取，如果，使用自定义参数，则此项无效
                //.setAppKey("ab55ce55Ac4bcP408cPb8c1Aaeac179c5f6f")
                //不显示通知栏进度条
                //  .dismissNotificationProgress()
                //是否忽略版本
                .showIgnoreVersion()

                .build()
                //检测是否有新版本
                .checkNewApp(new UpdateCallback() {
                    /**
                     * 解析json,自定义协议
                     *
                     * @param json 服务器返回的json
                     * @return UpdateAppBean
                     */
                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        UpdateAppBean updateAppBean = new UpdateAppBean();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            updateAppBean
                                    //（必须）是否更新Yes,No
                                    .setUpdate(jsonObject.optString("update"))
                                    //（必须）新版本号，
                                    .setNewVersion(jsonObject.optString("new_version"))
                                    //（必须）下载地址
                                    .setApkFileUrl(jsonObject.optString("apk_file_url"))
                                    //（必须）更新内容
                                    .setUpdateLog(jsonObject.optString("update_log"))
                                    //大小，不设置不显示大小，可以不设置
                                    .setTargetSize(jsonObject.optString("target_size"))
                                    //是否强制更新，可以不设置
                                    .setConstraint(false);
                            //设置md5，可以不设置
                            // .setNewMd5(jsonObject.optString("new_md51"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return updateAppBean;
                    }

                    /**
                     * 网络请求之前
                     */
                    @Override
                    public void onBefore() {
                        //     CProgressDialogUtils.showProgressDialog(MainActivity.this);
                    }

                    /**
                     * 网路请求之后
                     */
                    @Override
                    public void onAfter() {
                        //     CProgressDialogUtils.cancelProgressDialog(MainActivity.this);
                    }

                    /**
                     * 没有新版本
                     */
                    @Override
                    public void noNewApp(String e) {
                        //   Toast.makeText(MainActivity.this, "没有新版本", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                        super.hasNewApp(updateApp, updateAppManager);
                        //自定义对话框
                        showDiyDialog(updateApp, updateAppManager);
                    }

                });
    }

    private void showDiyDialog(UpdateAppBean updateApp, final UpdateAppManager updateAppManager) {

        String targetSize = updateApp.getTargetSize();
        String updateLog = updateApp.getUpdateLog();

        String msg = "";

        if (!TextUtils.isEmpty(targetSize)) {
            msg = "新版本大小：" + targetSize + "\n\n";
        }

        if (!TextUtils.isEmpty(updateLog)) {
            msg += updateLog;
        }

        new AlertDialog.Builder(this)
                .setTitle(String.format("是否升级到%s版本？", updateApp.getNewVersion()))
                .setMessage(msg)
                .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //不显示下载进度
                        updateAppManager.download();

                        dialog.dismiss();
                    }
                })
                .setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }
    public  String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
            Log.d("TAG", "当前版本名称：" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
             //   result.setText("扫描结果为：" + content);

                Intent intent = new Intent(MainActivity.this, AddFriendSearchUserInfoAc.class);
                intent.putExtra("search", content);
                startActivity(intent);
            }
        }
    }



}
