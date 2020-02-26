package service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.quyuanjin.imsix.app.App;
import com.quyuanjin.imsix.utils.SharedPreferencesUtils;

import netty.NettyLongChannel;
import netty.ProtoConstant;
import netty.utils.NetUtils;

public class NettyService extends Service {

    private LocalBinder mbinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public NettyService getservices() {
            return NettyService.this;
        }

        public void start() {
        }

        public void end() {
        }
    }

    public NettyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {


        super.onCreate();
    }

    String userid;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        userid = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "userid", "");
        String command = intent.getStringExtra("command");

        if ("longConnect".equals(command)) {
            myway();
        } else {

        }


        return super.onStartCommand(intent, flags, startId);


    }

    public void myway() {


        final Thread t = new Thread(new Runnable() {
            public void run() {

                if (NetUtils.isConnected(NettyService.this) && userid != null) {
                    Log.d("net", "执行了sConnected");

                    if (NettyLongChannel.initNetty()) {
                        try {
                            NettyLongChannel.sendAndReflash(ProtoConstant.LONG_CONNECT, userid + "\r\n");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                    }
                }

            }
        });
        t.start();


    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
