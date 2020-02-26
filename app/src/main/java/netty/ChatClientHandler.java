package netty;


import android.util.Log;

import com.google.gson.Gson;
import com.quyuanjin.imsix.Constant;
import com.quyuanjin.imsix.app.App;
import com.quyuanjin.imsix.chatsession.Msg;
import com.quyuanjin.imsix.utils.ToastUtils;

import eventbus.ClienthandlerBus.TellChatAc;
import eventbus.ClienthandlerBus.TellContr;
import eventbus.ClienthandlerBus.TellContractAddFirendMsgBack;

import org.greenrobot.eventbus.EventBus;

import eventbus.ClienthandlerBus.TellReceMent;
import eventbus.ClienthandlerBus.TellRecementAddFirendMsgBack;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ChatClientHandler extends SimpleChannelInboundHandler<String> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        if (!msg.contains("HTTP/1.1") && !msg.contains("Host:") && !msg.contains("Proxy-Connection: keep-alive") && !msg.contains("User2Model-Agent:")) {
            System.out.println(msg);
            String[] recemsg = msg.split("\\|");
            //recemsg1是各种对应的json
            switch (recemsg[0]) {
                //应对消息类型存进不同的表然后进行  event bus
                case ProtoConstant.SEND_MESSAGE_BACK:

                    Gson gson = new Gson();
                    Msg msg1 = gson.fromJson(recemsg[1], Msg.class);
                    Log.d("888",String.valueOf(msg1.getReceiveId()));
                    if (msg1.getContentType() == Constant.item_text_send) {
                        msg1.setContentType(Constant.item_text_receive);
                    } else if (msg1.getContentType() == Constant.item_image_send) {
                        msg1.setContentType(Constant.item_image_receive);
                    } else if (msg1.getContentType() == Constant.item_audio_send) {
                        msg1.setContentType(Constant.item_audio_receive);
                    } else if (msg1.getContentType() == Constant.item_video_send) {
                        msg1.setContentType(Constant.item_video_receive);
                    } else {
                        msg1.setContentType(404);
                    }

                    App.getDaoSession().getMsgDao().insert(msg1);
                    //通知
                    EventBus.getDefault().post(new TellReceMent(msg1));//从服务器拉取
                    EventBus.getDefault().post(new TellChatAc(msg1));

                    break;
                //加好友
                case ProtoConstant.ADD_FRIEND_BACK:
                    //通知相关界面去取就行了
                    //通知主活动去服务器取，然后存储

                    //通知联系人fragment去显示红点
                    EventBus.getDefault().post(new TellContr());
                    break;
                case ProtoConstant.ADD_FRIEND_AGREE_BACK:
                    //存表
                    //  Gson gson5 = new Gson();
                    //  PojoPullUnReadAddFriendMsgFromNet msg5 = gson5.fromJson(recemsg[1], PojoPullUnReadAddFriendMsgFromNet.class);
                    //   App.getDaoSession().getPojoPullUnReadAddFriendMsgFromNetDao().insert(msg5);
                    //通知联系人表和最近联系人表从网络上获取
                    EventBus.getDefault().post(new TellRecementAddFirendMsgBack());
                    EventBus.getDefault().post(new TellContractAddFirendMsgBack());
                    break;


                default:
                    break;

            }

        }


    }
}