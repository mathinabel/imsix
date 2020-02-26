package com.quyuanjin.imsix.addfriend.newfriendAc;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.quyuanjin.imsix.Constant;
import com.quyuanjin.imsix.R;
import com.quyuanjin.imsix.addfriend.PojoPullUnReadAddFriendMsgFromNet;

import com.quyuanjin.imsix.utils.SharedPreferencesUtils;


import java.util.ArrayList;


import netty.NettyLongChannel;
import netty.ProtoConstant;

public class NewFriendAdapter extends RecyclerView.Adapter<NewFriendAdapter.ViewHolder> {

    private ArrayList<PojoPullUnReadAddFriendMsgFromNet> entityList;
    private Context context;
    String name;
    String des;
    String sex;
    String por;
    String userid;

    public NewFriendAdapter(ArrayList<PojoPullUnReadAddFriendMsgFromNet> entityList, Context context) {
        this.entityList = entityList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        name = (String) SharedPreferencesUtils.getParam(parent.getContext().getApplicationContext(), "name", "");
        des = (String) SharedPreferencesUtils.getParam(parent.getContext().getApplicationContext(), "des", "");
        sex = (String) SharedPreferencesUtils.getParam(parent.getContext().getApplicationContext(), "sex", "");
        por = (String) SharedPreferencesUtils.getParam(parent.getContext().getApplicationContext(), "portrait", "");
        userid = (String) SharedPreferencesUtils.getParam(parent.getContext().getApplicationContext(), "userid", "");


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_add_friend_list, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (entityList.size() != 0) {
            PojoPullUnReadAddFriendMsgFromNet pojoPullUnReadAddFriendMsgFromNet = entityList.get(position);
            holder.nameTextView.setText(pojoPullUnReadAddFriendMsgFromNet.getNameTextView());
            holder.portraitImageView3.setImageURI(Uri.parse(pojoPullUnReadAddFriendMsgFromNet.getPortraitImageViewnetPath()));
            if (Constant.addfriend_send_but_wait_to_answer.equals(entityList.get(position).getReadType())) {
//自己发的已发送
                holder.acceptButton.setVisibility(View.GONE);
                holder.acceptStatusTextView.setVisibility(View.VISIBLE);
                holder.acceptStatusTextView.setText("待确认");
            } else if (Constant.be_asked_to_addfriend.equals(entityList.get(position).getReadType())) {
//别人发的，等待确认
                holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.acceptButton.setVisibility(View.GONE);
                        holder.acceptStatusTextView.setVisibility(View.VISIBLE);
                        //发送网络更新好友请求状态
                        PojoPullUnReadAddFriendMsgFromNet pojoPullUnReadAddFriendMsgFromNet = new PojoPullUnReadAddFriendMsgFromNet(entityList.get(position).getId(), name, entityList.get(position).getNameTextView(), sex, userid, entityList.get(position).getReceiverId(), "", "", "", por, Constant.addFriend_answer_agree, "");
                        Gson gson = new Gson();
                        NettyLongChannel.sendMsg(ProtoConstant.ADD_FRIEND_AGREE, gson.toJson(pojoPullUnReadAddFriendMsgFromNet));
                     //服务器也存储，存储两条，自身直接添加一条到各自表，通知界面从本地获取
                        //下次再打开时从数据库获取，与本地无二

                /*        App.getDaoSession().getPojoRecementMsgDao().insert(new PojoRecementMsg(null,
                                userid,entityList.get(position).getReceiverId(),
                                entityList.get(position).getPortraitImageViewnetPath(),
                                "1",entityList.get(position).getNameTextView(),
                                entityList.get(position).getCreateTime()
                                ,"","","你好",""));
                        App.getDaoSession().getPojoContractDao().insert(new PojoContract(null,
                                entityList.get(position).getPortraitImageViewnetPath(),
                                entityList.get(position).getNameTextView(),
                                entityList.get(position).getContentTextView(),
                                entityList.get(position).getSex(),
                                entityList.get(position).getUserid(),
                                Constant.TAG_ITEM,""));*/
                         // EventBus.getDefault().post(new EventBusPojoRecement());//通知最近联系人
                  //       EventBus.getDefault().post(new EventBusContract());//通知联系人
                    }
                });
            } else {
                holder.acceptButton.setVisibility(View.GONE);
                holder.acceptStatusTextView.setVisibility(View.VISIBLE);
                holder.acceptStatusTextView.setText("已同意");
            }


        }
    }

    @Override
    public int getItemCount() {
        return entityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView portraitImageView3;
        TextView nameTextView;
        Button acceptButton;
        TextView acceptStatusTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            portraitImageView3 = itemView.findViewById(R.id.portraitImageView3);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            acceptStatusTextView = itemView.findViewById(R.id.acceptStatusTextView);

        }
    }
}
