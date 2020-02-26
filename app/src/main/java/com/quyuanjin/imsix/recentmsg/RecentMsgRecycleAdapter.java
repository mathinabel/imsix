package com.quyuanjin.imsix.recentmsg;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.facebook.drawee.view.SimpleDraweeView;
import com.quyuanjin.imsix.Constant;
import com.quyuanjin.imsix.R;

import java.util.ArrayList;

public class RecentMsgRecycleAdapter extends RecyclerView.Adapter<RecentMsgRecycleAdapter.ViewHolder> {
    private ArrayList<PojoRecementMsg> entityList;


    private Context context;

    /*   private RecentMsgRecycleAdapter.OnItemLongClickListener onItemLongClickListener;

      //设置回调接口
     public interface OnItemLongClickListener {
          void onItemLongClick(View view, int position);
      }

      public void setOnItemLongClickListener(RecentMsgRecycleAdapter.OnItemLongClickListener mOnItemClickLitener) {
          this.onItemLongClickListener = mOnItemClickLitener;

      }*/
    public RecentMsgRecycleAdapter(Context context, ArrayList<PojoRecementMsg> entityList) {
        this.entityList = entityList;
        this.context = context;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_recent_msg, parent, false);


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (entityList.size() != 0) {
            PojoRecementMsg pojoRecementMsg = entityList.get(position);
            holder.unreadCountTextView.setText(pojoRecementMsg.getUnreadCount());
            holder.nameTextView.setText(pojoRecementMsg.getName());
            holder.promptTextView.setText(pojoRecementMsg.getPrompt());
            holder.timeTextView.setText(pojoRecementMsg.getTime());
            holder.contentTextView.setText(pojoRecementMsg.getContentText());
            if (!"".equals(pojoRecementMsg.getPortrait())){
                Log.d("888",pojoRecementMsg.getPortrait());
                  //  holder.portraitImageView.setImageURI(Uri.parse(pojoRecementMsg.getPortrait()));

               // holder.portraitImageView.setImageURI(Uri.parse("http://120.79.178.226:8080/File/portrait/upload/666.png"));
            }
            if (pojoRecementMsg.getStatus().equals(Constant.send_state_failed)) {
                holder.statusImageView.setVisibility(View.VISIBLE);
            } else {
                holder.statusImageView.setVisibility(View.GONE);

            }
            if (pojoRecementMsg.getSlient().equals(Constant.slient_state_failed)) {
                holder.slient.setVisibility(View.VISIBLE);
            } else {
                holder.slient.setVisibility(View.GONE);

            }

        }
        //通过为条目设置点击事件触发回调
     /*   if (onItemLongClickListener != null) {
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onItemLongClickListener.onItemLongClick(view, position);
                }
            });
        }*/
    }

    @Override
    public int getItemCount() {
        return entityList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView portraitImageView;//头像
        TextView unreadCountTextView;//未读消息数
        TextView nameTextView;//用户昵称（自己设定的优先）
        TextView timeTextView;//时间
        TextView promptTextView;//草稿
        ImageView statusImageView;//发送失败
        TextView contentTextView;//草稿内容|最近消息
        ImageView slient;

        public ViewHolder(View itemView) {
            super(itemView);
            portraitImageView = itemView.findViewById(R.id.portraitImageView1);
            unreadCountTextView = itemView.findViewById(R.id.unreadCountTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            promptTextView = itemView.findViewById(R.id.promptTextView);
            statusImageView = itemView.findViewById(R.id.statusImageView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            slient = itemView.findViewById(R.id.slient);
        }
    }

}
