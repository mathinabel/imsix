package com.quyuanjin.imsix.myinfoac;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.listener.InputDialogOkButtonClickListener;
import com.kongzue.dialog.util.InputInfo;
import com.kongzue.dialog.v2.InputDialog;
import com.kongzue.dialog.v2.Notification;
import com.kongzue.dialog.v2.TipDialog;
import com.kongzue.dialog.v2.WaitDialog;
import com.lqr.optionitemview.OptionItemView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.style.PictureCropParameterStyle;
import com.quyuanjin.imsix.Constant;
import com.quyuanjin.imsix.R;
import com.quyuanjin.imsix.contract.EventBusContract;
import com.quyuanjin.imsix.contract.PojoContract;
import com.quyuanjin.imsix.dialog.MyDialog;
import com.quyuanjin.imsix.login.LoginAndRegisterAc;
import com.quyuanjin.imsix.login.User;
import com.quyuanjin.imsix.utils.SDialog;
import com.quyuanjin.imsix.utils.SharedPreferencesUtils;
import com.quyuanjin.imsix.utils.ToastUtils;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Request;


public class MyInfoAc extends AppCompatActivity {
    private AutoLinearLayout llHeader;
    private SimpleDraweeView portraitImageView;
    private OptionItemView oivName;
    private OptionItemView oivAccount;
    private OptionItemView oivQRCodeCard;
    private OptionItemView changePassword;
    String userid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        userid = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "userid", "");

        initView();
        initListener();
        initBackListener();
    }

    private void initBackListener() {
        CommonTitleBar commonTitleBar = findViewById(R.id.titlebar9);
        commonTitleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    onBackPressed();
                }
            }
        });
    }

    private void initListener() {
        llHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PictureSelector.create(MyInfoAc.this)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(1)
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                        .circleDimmedLayer(true)// 是否圆形裁剪
                        .hideBottomControls(false)
                        .showCropFrame(false)
                        .selectionMode(PictureConfig.SINGLE)
                        .theme(R.style.picture_white_style)
                        .enableCrop(true)
                        .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                        .forResult(new OnResultCallbackListener() {
                            @Override
                            public void onResult(List<LocalMedia> result) {
                                // 例如 LocalMedia 里面返回五种path
                                // 1.media.getPath(); 原图path，但在Android Q版本上返回的是content:// Uri类型
                                // 2.media.getCutPath();裁剪后path，需判断media.isCut();切勿直接使用
                                // 3.media.getCompressPath();压缩后path，需判断media.isCompressed();切勿直接使用
                                // 4.media.getOriginalPath()); media.isOriginal());为true时此字段才有值
                                // 5.media.getAndroidQToPath();Android Q版本特有返回的字段，但如果开启了压缩或裁剪还是取裁剪或压缩路
                                // 径；注意：.isAndroidQTransform(false);此字段将返回空
                                // 如果同时开启裁剪和压缩，则取压缩路径为准因为是先裁剪后压缩

                              /*  for (LocalMedia media : result) {
                                    Log.i(TAG, "是否压缩:" + media.isCompressed());
                                    Log.i(TAG, "压缩:" + media.getCompressPath());
                                    Log.i(TAG, "原图:" + media.getPath());
                                    Log.i(TAG, "是否裁剪:" + media.isCut());
                                    Log.i(TAG, "裁剪:" + media.getCutPath());
                                    Log.i(TAG, "是否开启原图:" + media.isOriginal());
                                    Log.i(TAG, "原图路径:" + media.getOriginalPath());
                                    Log.i(TAG, "Android Q 特有Path:" + media.getAndroidQToPath());
                                }*/
                                //        Toast.makeText(MyInfoAc.this, result.get(0).getCutPath(), Toast.LENGTH_LONG).show();
                                SharedPreferencesUtils.setParam(getApplicationContext(), "CutPath", "file://" + result.get(0).getCutPath());
                                portraitImageView.setImageURI(Uri.parse("file://" + result.get(0).getCutPath()));
                                //    EventBus.getDefault().post(new CutPath("file://" + result.get(0).getCutPath()));
                                updateImage(result.get(0).getCutPath());
                            }

                        });
            }
        });

        oivName.setRightText((String) SharedPreferencesUtils.getParam(getApplicationContext(), "name", ""));
        oivAccount.setRightText(userid);
    }

    private void initView() {
        llHeader = findViewById(R.id.llHeader);
        portraitImageView = findViewById(R.id.portraitImageView2);
        oivAccount = findViewById(R.id.oivAccount);
        oivName = findViewById(R.id.oivName);
        oivName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputDialog.show(MyInfoAc.this, "设置昵称", "设置一个好听的名字吧", new InputDialogOkButtonClickListener() {
                    @Override
                    public void onClick(Dialog dialog, String inputText) {
                        OkHttpUtils.post()
                                .url(Constant.URL + "setName")
                                .addParams("name", inputText)
                                .addParams("userid", userid)
                                .build().execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                TipDialog.show(MyInfoAc.this, "设置失败", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
                                dialog.dismiss();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                TipDialog.show(MyInfoAc.this, "设置成功", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
                                dialog.dismiss();
                                SharedPreferencesUtils.setParam(getApplicationContext(), "name", inputText);

                            }

                            @Override
                            public void onBefore(Request request, int id) {
                                super.onBefore(request, id);
                                WaitDialog.show(MyInfoAc.this, "等待中...");

                            }

                            @Override
                            public void onAfter(int id) {
                                super.onAfter(id);
                                WaitDialog.dismiss();
                            }
                        });
                    }
                });
            }
        });

        oivQRCodeCard = findViewById(R.id.oivQRCodeCard);
        oivQRCodeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogOrDis();
            }
        });
        changePassword = findViewById(R.id.change_password);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputDialog.show(MyInfoAc.this, "设置", "请设置密码：", new InputDialogOkButtonClickListener() {
                    @Override
                    public void onClick(Dialog dialog, String inputText) {
                        // if (!inputText.equals("kongzue")) {
                        //     TipDialog.show(MyInfoAc.this, "错误的用户名", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR);
                        //     Notification.show(MyInfoAc.this, 0, "小提示：用户名是：kongzue");
                        //   } else {


                        OkHttpUtils.post()
                                .url(Constant.URL + "setPwd")
                                .addParams("pwd", inputText)
                                .addParams("userid", userid)
                                .build().execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                TipDialog.show(MyInfoAc.this, "设置失败", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
                                dialog.dismiss();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Gson gson = new Gson();
                                User user = gson.fromJson(response, User.class);
                                if (!"".equals(user.getId())) {

                                    TipDialog.show(MyInfoAc.this, "设置成功", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
                                    dialog.dismiss();
                                    SharedPreferencesUtils.setParam(getApplicationContext(), "name", user.getName());

                                    oivName.setRightText(user.getName());

                                }else {
                                    TipDialog.show(MyInfoAc.this, "设置失败", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
                                    dialog.dismiss();
                                }


                            }

                            @Override
                            public void onBefore(Request request, int id) {
                                super.onBefore(request, id);
                                WaitDialog.show(MyInfoAc.this, "等待中...");

                            }

                            @Override
                            public void onAfter(int id) {
                                super.onAfter(id);
                                WaitDialog.dismiss();
                            }
                        });


                        //  }
                    }
                }).setInputInfo(new InputInfo()
                        .setMAX_LENGTH(18)                                          //设置最大长度11位
                );
            }
        });
    }

    private void showDialogOrDis() {
        MyDialog myAdvertisementView = new MyDialog(this);
        myAdvertisementView.showDialog();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String s = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "CutPath", "");
        if (!("".equals(s))) {
            portraitImageView.setImageURI(Uri.parse(s));
        }
    }

    private void updateImage(String cutPath) {

        //ToastUtils.show(MyInfoAc.this,cutPath);
        OkHttpUtils.post()
                .url(Constant.URL + "uploadPortrait")
                .addParams("userid", userid)
                .addFile("portrait", UUID.randomUUID() + ".png", new File(cutPath))//

                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //保存返回的头像地址
                        if (!"上传失败".equals(response)) {
                            SharedPreferencesUtils.setParam(getApplicationContext(), "CutPath", response);
                            String s = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "CutPath", "");
                            if (!("".equals(s))) {
                                portraitImageView.setImageURI(Uri.parse(s));
                            }
                            //    ToastUtils.show(MyInfoAc.this,"上传dao"+response);

                        } else {
                            ToastUtils.show(MyInfoAc.this, "上传失败");
                        }

                    }


                });
    }
}
