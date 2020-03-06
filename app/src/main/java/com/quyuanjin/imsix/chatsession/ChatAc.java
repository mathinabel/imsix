package com.quyuanjin.imsix.chatsession;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.quyuanjin.imsix.Constant;
import com.quyuanjin.imsix.R;
import com.quyuanjin.imsix.app.App;
import com.quyuanjin.imsix.myinfoac.GlideEngine;
import com.quyuanjin.imsix.recentmsg.PojoRecementMsg;
import com.quyuanjin.imsix.utils.ChenJingET;
import com.quyuanjin.imsix.utils.SharedPreferencesUtils;
import com.quyuanjin.imsix.utils.SoftKeyboardUtils;
import com.quyuanjin.imsix.utils.ToastUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.impl.ScrollBoundaryDeciderAdapter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.ios.IosEmojiProvider;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import eventbus.ClienthandlerBus.TellChatAc;
import eventbus.ClienthandlerBus.TellRecementAddFirendMsgBack;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import netty.NettyLongChannel;
import netty.ProtoConstant;
import okhttp3.Call;
import okhttp3.Request;
import weixin_recorder.MediaManager;
import weixin_recorder.Recorder;
import weixin_recorder.view.AudioRecorderButton;

public class ChatAc extends AppCompatActivity implements View.OnClickListener {
    EditText mEditText;
    Button sendBtn;
    RecyclerView recyclerView;
    ArrayList<Msg> mList = new ArrayList<>();
    ImageView imageView;

    LinearLayout view;

    //加号点出来的
    private ImageView ivAlbum;
    private ImageView ivShot;
    private ImageView ivLocation;
    private ImageView ivRedPack;
    private ImageView ivMore;
    private LinearLayout bolck_titlebar;
    private EmojiPopup emojiPopup;
    private ImageView ivAudio;
    int bheight;
    int afterHeight;
    ChatAdapter adapter;
    private ImageView ivAudiosend;
    private ImageView ivAudiorece;
    private AudioRecorderButton id_recorder_button;
    private CommonTitleBar titlebar;
    private String receverid;
    private String userid;
    private String por;
    private String receverName;
    private String s;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userid = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "userid", "");
        s = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "CutPath", "");

        por = getIntent().getStringExtra("por");
        receverName = getIntent().getStringExtra("name");

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        receverid = getIntent().getStringExtra("receverid");
        ToastUtils.show(ChatAc.this, "收到的uid是：" + receverid);
        EmojiManager.install(new IosEmojiProvider());
        setContentView(R.layout.activity_session);
        ChenJingET.assistActivity(this);
        mEditText = findViewById(R.id.etContent);
        sendBtn = findViewById(R.id.btnSend);
        recyclerView = findViewById(R.id.rvMsg);
        imageView = findViewById(R.id.ivEmo);
        view = findViewById(R.id.llRoot);
        ivAudio = findViewById(R.id.ivAudio);
        titlebar = findViewById(R.id.titlebar);
        titlebar.setListener((v, action, extra) -> {
            if (action == CommonTitleBar.ACTION_LEFT_TEXT) {
                onBackPressed();
            }
        });

        id_recorder_button = findViewById(R.id.id_recorder_button);
     /*   view.post(new Runnable() {
            @Override
            public void run() {

                bheight = view.getMeasuredHeight();
            }
        });*/
        //加号
        ivMore = findViewById(R.id.ivMore);
        //加号内容

        bolck_titlebar = findViewById(R.id.bolck_titlebar);
        ivAlbum = findViewById(R.id.ivAlbum);
        ivShot = findViewById(R.id.ivShot);
        ivLocation = findViewById(R.id.ivLocation);
        ivRedPack = findViewById(R.id.ivRedPack);

        bolck_titlebar.setOnClickListener(this);
        ivMore.setOnClickListener(this);
        ivAlbum.setOnClickListener(this);
        ivShot.setOnClickListener(this);
        ivLocation.setOnClickListener(this);
        ivRedPack.setOnClickListener(this);

        emojiPopup = EmojiPopup.Builder.fromRootView(view).build(mEditText);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

       // mList.add(new Msg(null, "", "", "你好", "", "", "", 0, 0, "", ""));
        initDataFromLocal();

        adapter = new ChatAdapter(this, mList);

        adapter.setOnItemLongClickListener(new ChatAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(final View view, final int position) {
              //  Toast.makeText(ChatAc.this, "这是条目" + mList.get(position), Toast.LENGTH_LONG).show();

                PopupMenu popupMenu = new PopupMenu(ChatAc.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_item, popupMenu.getMenu());

                //弹出式菜单的菜单项点击事件
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        mList.remove(position);
                        adapter.notifyItemRemoved(position);
                        return false;
                    }
                });
                popupMenu.show();

            }
        });

        adapter.setOnItemVoiceClickListener(new ChatAdapter.OnItemVoiceClickListener() {
            @Override
            public void onItemVoiceClick(View view, int position) {
                if (Constant.item_audio_send == mList.get(position).getContentType()) {


                    //如果ling一个动画正在运行， 停止第一个播放其他的
                    if (ivAudiosend != null) {
                        ivAudiosend.setBackgroundResource(R.drawable.adj);

                        ivAudiosend = null;
                    }
                    //播放动画
                    ivAudiosend = view.findViewById(R.id.ivAudiosend);
                    ivAudiosend.setBackgroundResource(R.drawable.audio_animation_right_list);
                    final AnimationDrawable animation = (AnimationDrawable) ivAudiosend.getBackground();
                    animation.start();

                    //播放音频  完成后改回原来的background
                    MediaManager.playSound(mList.get(position).getLocalPath(), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {

                            ivAudiosend.setBackgroundResource(R.drawable.adj);
                        }
                    });
                } else {


                    //如果ling一个动画正在运行， 停止第一个播放其他的
                    if (ivAudiorece != null) {
                        ivAudiorece.setBackgroundResource(R.drawable.adj);

                        ivAudiorece = null;
                    }
                    //播放动画
                    ivAudiorece = view.findViewById(R.id.ivAudiorece);
                    ivAudiorece.setBackgroundResource(R.drawable.audio_animation_right_list);
                    final AnimationDrawable animation2 = (AnimationDrawable) ivAudiorece.getBackground();
                    animation2.start();

                    //播放音频  完成后改回原来的background
                    MediaManager.playSound(mList.get(position).getNetPath(), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {

                            ivAudiorece.setBackgroundResource(R.drawable.adj);
                        }
                    });
                }

            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setScaleY(-1);//必须设置
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (SoftKeyboardUtils.isSoftShowing(ChatAc.this)) {
                    SoftKeyboardUtils.hideSystemSoftKeyboard(ChatAc.this);
                }
                if (bolck_titlebar.getVisibility() == View.VISIBLE) {
                    bolck_titlebar.setVisibility(View.GONE);
                }

                if (emojiPopup.isShowing()) {
                    emojiPopup.dismiss();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


            }
        });
        recyclerView.scrollToPosition(mList.size() - 1);
        mEditText.setOnClickListener(this);
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.scrollToPosition(mList.size() - 1);
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    sendBtn.setVisibility(View.VISIBLE);
                    ivMore.setVisibility(View.GONE);
                } else {
                    sendBtn.setVisibility(View.GONE);
                    ivMore.setVisibility(View.VISIBLE);
                }

            }
        });

        imageView.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
        ivAudio.setOnClickListener(this);
        initAudio();

        initRefresh();

    }

    private void initDataFromLocal() {
        List<Msg> msgList = App.getDaoSession()
                .getMsgDao().queryBuilder()
                .whereOr(MsgDao.Properties.SendID.eq(receverid),
                        MsgDao.Properties.ReceiveId.eq(receverid))
                .list();
        mList.addAll(msgList);
    }


    private void initRefresh() {

        final RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
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


                        mList.add(0, new Msg(null, "",
                                "", UUID.randomUUID().toString(), "", "",
                                "", 0, 0,
                                "", ""));
                        adapter.notifyItemInserted(0);
                        adapter.notifyItemRangeChanged(0, mList.size());
                        refreshLayout.finishLoadMore();
                    }
                }, 1000);
            }
        });

    }

    private void initAudio() {
        id_recorder_button.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) {
                //每完成一次录音
                //     Recorder recorder = new Recorder(seconds, filePath);

                OkHttpUtils.post()//
                        .addFile("voice", UUID.randomUUID() + ".amr", new File(filePath))//
                        .url(Constant.URL+"uploadVoice")
                        //      .params( )//
                        //     .headers()//
                        .build()//
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Msg msg = new Msg(null, userid, receverid, "", "",
                                        filePath, response, Constant.item_audio_send
                                        , seconds, "", "");
                                mList.add(msg);
                                App.getDaoSession().getMsgDao().insert(msg);
                                Gson gson = new Gson();
                                NettyLongChannel.sendMsg(ProtoConstant.SEND_MESSAGE, gson.toJson(msg));
                                adapter.notifyDataSetChanged();

                                insertNetDBChating("[语音]");
                                recyclerView.scrollToPosition(mList.size() - 1);

                            }
                        });


            }
        });
    }

    private static final int REQUEST_SELECT_IMAGES_CODE = 0x01;
    private ArrayList<String> mImagePaths;
    Handler handler = new Handler();

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.ivMore:


                if (SoftKeyboardUtils.isSoftShowing(ChatAc.this)) {

                    SoftKeyboardUtils.hideSystemSoftKeyboard(ChatAc.this);

                    if (bolck_titlebar.getVisibility() == View.GONE) {

                        handler.postDelayed(new Runnable() {
                            public void run() {
                                bolck_titlebar.setVisibility(View.VISIBLE);
                            }
                        }, 100);


                    } else {
                        bolck_titlebar.setVisibility(View.GONE);
                    }

                } else {
                    if (bolck_titlebar.getVisibility() == View.GONE) {
                        bolck_titlebar.setVisibility(View.VISIBLE);

                    } else {
                        bolck_titlebar.setVisibility(View.GONE);
                    }
                }


                break;
            case R.id.ivAlbum:

           /*     PermissionGen.with(ChatAc.this)
                        .addRequestCode(100)
                        .permissions(
                                Manifest.permission.READ_CONTACTS,
                                Manifest.permission.RECEIVE_SMS,
                                Manifest.permission.WRITE_CONTACTS)
                        .request();
*/

                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                        .compress(true)// 是否压缩
                        .compressQuality(60)// 图片压缩后输出质量
                        .forResult(new OnResultCallbackListener() {
                            @Override
                            public void onResult(List<LocalMedia> result) {
                                for (LocalMedia media : result) {
//                                    Log.i(TAG, "是否压缩:" + media.isCompressed());
//                                    Log.i(TAG, "压缩:" + media.getCompressPath());
//                                    Log.i(TAG, "原图:" + media.getPath());
//                                    Log.i(TAG, "是否裁剪:" + media.isCut());
//                                    Log.i(TAG, "裁剪:" + media.getCutPath());
//                                    Log.i(TAG, "是否开启原图:" + media.isOriginal());
//                                    Log.i(TAG, "原图路径:" + media.getOriginalPath());
//                                    Log.i(TAG, "Android Q 特有Path:" + media.getAndroidQToPath());

                                    OkHttpUtils.post()//
                                            .addFile("image", UUID.randomUUID() + ".png", new File(media.getCompressPath()))//
                                            .url(Constant.URL+"uploadImage")
                                            //      .params( )//
                                            //     .headers()//
                                            .build()//
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onError(Call call, Exception e, int id) {

                                                }

                                                @Override
                                                public void onResponse(String response, int id) {
                                                    if (response != null) {
                                                        //  ((ChatAdapter.ViewHolder3) holder).tvName.setText(response);

                                                        //   Msg msg= arrayList.get(position);
                                                        Msg msg = new Msg(null, userid, receverid, "", "",
                                                                media.getCompressPath(), response, Constant.item_image_send
                                                                , 0, "", "");
                                                        mList.add(msg);
                                                        App.getDaoSession().getMsgDao().insert(msg);
                                                        Gson gson = new Gson();
                                                        NettyLongChannel.sendMsg(ProtoConstant.SEND_MESSAGE, gson.toJson(msg));

                                                    }
                                                }
                                            });
                                }
                                adapter.notifyDataSetChanged();
                                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mList.size() - 1);

                            }
                        });

                break;
            case R.id.ivShot:
                PermissionGen.with(ChatAc.this)
                        .addRequestCode(100)
                        .permissions(
                                Manifest.permission.CAMERA)
                        .request();

                PictureSelector.create(this)
                        .openCamera(PictureMimeType.ofAll())
                        .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                        .isUseCustomCamera(true)
                        .forResult(PictureConfig.REQUEST_CAMERA);

             /*   PictureSelector.create(this)
                        .openCamera(PictureMimeType.ofImage())
                        .compress(true)// 是否压缩
                        .compressQuality(60)// 图片压缩后输出质量
                        .isUseCustomCamera(true)
                        .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                        .forResult(new OnResultCallbackListener() {
                            @Override
                            public void onResult(List<LocalMedia> result) {
                                for (LocalMedia media : result) {
                                    mList.add(new Msg(null, "", "", "", "", "",
                                            media.getCompressPath(), "", Constant.item_image_send
                                            , 0, ""));

                                }
                                adapter.notifyDataSetChanged();
                                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mList.size() - 1);

                            }
                        });*/
                break;
            case R.id.ivLocation:
                break;
            case R.id.ivRedPack:
                break;
            case R.id.btnSend:


                if (!(mEditText.getText().toString().trim().equals(""))) {
                    Msg msg = new Msg(null, userid, receverid, mEditText.getText().toString().trim(),
                            "", "", s,
                            Constant.item_text_send, 0, "", "");

                    mList.add(msg);
                   App.getDaoSession().getMsgDao().insert(msg);
                    adapter.notifyDataSetChanged();
                    mEditText.setText("");
                    recyclerView.scrollToPosition(mList.size() - 1);
                    Gson gson = new Gson();
                    NettyLongChannel.sendMsg(ProtoConstant.SEND_MESSAGE, gson.toJson(msg));

                    insertNetDBChating(mEditText.getText().toString().trim());

                }

                break;
            case R.id.ivEmo:

                recyclerView.scrollToPosition(mList.size() - 1);
                bolck_titlebar.setVisibility(View.GONE);
                emojiPopup.toggle();
                break;
            case R.id.etContent:

                recyclerView.scrollToPosition(mList.size() - 1);

                if (bolck_titlebar.getVisibility() == View.VISIBLE) {

                    bolck_titlebar.setVisibility(View.GONE);
                }
                break;
            case R.id.ivAudio:
                PermissionGen.with(ChatAc.this)
                        .addRequestCode(100)
                        .permissions(
                                Manifest.permission.RECORD_AUDIO
                                , Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .request();
                if (SoftKeyboardUtils.isSoftShowing(ChatAc.this)) {

                    SoftKeyboardUtils.hideSystemSoftKeyboard(ChatAc.this);
                    if (id_recorder_button.getVisibility() == View.GONE) {
                        emojiPopup.dismiss();
                        bolck_titlebar.setVisibility(View.GONE);
                        id_recorder_button.setVisibility(View.VISIBLE);
                        ivMore.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);//emoji开关
                        mEditText.setVisibility(View.GONE);

                    } else {
                        id_recorder_button.setVisibility(View.GONE);
                        ivMore.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);//emoji开关
                        mEditText.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (id_recorder_button.getVisibility() == View.GONE) {
                        emojiPopup.dismiss();
                        bolck_titlebar.setVisibility(View.GONE);
                        id_recorder_button.setVisibility(View.VISIBLE);
                        ivMore.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);//emoji开关
                        mEditText.setVisibility(View.GONE);

                    } else {
                        id_recorder_button.setVisibility(View.GONE);
                        ivMore.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);//emoji开关
                        mEditText.setVisibility(View.VISIBLE);
                    }
                }
                break;

        }
    }

    private void insertNetDBChating(String content) {
        Gson g = new Gson();
        String s = g.toJson(new PojoRecementMsg(null, userid, receverid, por,
                "", receverName, "",
                "", "", content, ""));
        OkHttpUtils.post()
                .url(Constant.URL + "addOneToRecenmentMsg")
                .addParams("userid", s)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 100)
    public void doSomething() {
        //  Toast.makeText(this, "Contact permission is granted", Toast.LENGTH_SHORT).show();
    }

    @PermissionFail(requestCode = 100)
    public void doFailSomething() {
        //   Toast.makeText(this, "Contact permission is not granted", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        finish();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)//
    public void ReChatData(TellChatAc msg) {
        if (userid.equals(msg.getMsg().getReceiveId())) {
            mList.add(msg.getMsg());
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(mList.size() - 1);
        }
    }
}

