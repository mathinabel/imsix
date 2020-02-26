package com.quyuanjin.imsix.app;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraXConfig;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.quyuanjin.imsix.chatsession.DaoMaster;
import com.quyuanjin.imsix.chatsession.DaoSession;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import service.NettyService;


public class App extends Application implements CameraXConfig.Provider{

    private static App mApplication;
    private static DaoSession daoSession;

    private ServiceConnection nettyService;
    public static NettyService services;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplication = this;
        initOkhttpHelp();
        initGreenDao();
        initSocketService();
    }

    private void initSocketService() {
        nettyService = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                NettyService.LocalBinder binder = (NettyService.LocalBinder) iBinder;
                binder.start();
                binder.end();
                services = binder.getservices();
               // services.myway();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                services = null;
            }
        };
        Intent start = new Intent(this, NettyService.class);

            startService(start);

        bindService(start, nettyService, BIND_AUTO_CREATE);
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "im.db");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    private void initOkhttpHelp() {
        Fresco.initialize(this);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
        OkHttpClient okHttpClient2 = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient2);
    }


    public static App getContext() {
        return mApplication;
    }



    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return Camera2Config.defaultConfig();
    }
}
