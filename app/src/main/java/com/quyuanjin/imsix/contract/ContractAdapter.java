package com.quyuanjin.imsix.contract;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.quyuanjin.imsix.Constant;
import com.quyuanjin.imsix.R;
import com.quyuanjin.imsix.chatsession.ChatAdapter;

import java.util.ArrayList;

public class ContractAdapter extends RecyclerView.Adapter {
    private ArrayList<PojoContract> entityList;
    private Context context;

    public ContractAdapter(ArrayList<PojoContract> entityList, Context context) {
        this.entityList = entityList;
        this.context = context;
    }
/**
 *  根据数据传进来的昵称，根据昵称首字母来判断放置位置
 *  如果该字母有对应的sticky，则放入该sticky的位置的下一个，
 *  如果没有，则创建该字母的sticky，并且将该sticky放入合适的position
 *  然后保存数据到数据库，保存之后，下次取出直接插入
 *
 *  以一个字段
 */



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if (viewType == 0) {

            View view1 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_contract_sticky, parent, false);
            return new ViewHolder3(view1);

        } else  {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contract, parent, false);
            return new ViewHolder2(v);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0://sticky
                ((ViewHolder3) holder).tv_title.setText(entityList.get(position).getPinyin());

                break;

            case 1:
               // ((ViewHolder2) holder).nameTextView.setText(entityList.get(position).getNameTextView());
                if (entityList.size() != 0) {
                    PojoContract pojoContract = entityList.get(position);
                    ((ViewHolder2) holder).contentTextView.setText(pojoContract.getContentTextView());
                    ((ViewHolder2) holder).nameTextView.setText(pojoContract.getNameTextView());

                    ((ViewHolder2) holder).portraitImageView.setImageURI(Uri.parse(pojoContract.getPortraitImageView()));
                }
                break;
        }


    }

    @Override
    public int getItemCount() {
        return entityList.size();
    }


    @Override
    public int getItemViewType(int position) {
       if (entityList.get(position).getTag().equals(Constant.TAG_STICKY)) {
            return 0;
        } else{
            return 1;

    }}

    public class ViewHolder2 extends RecyclerView.ViewHolder {

        SimpleDraweeView portraitImageView;//头像
        TextView nameTextView;//用户昵称（自己设定的优先）
        TextView contentTextView;//个性签名

        public ViewHolder2(View itemView) {
            super(itemView);
            portraitImageView = itemView.findViewById(R.id.portraitImageView1);
            nameTextView = itemView.findViewById(R.id.nameTextView1);
            contentTextView = itemView.findViewById(R.id.contentTextView1);
        }
    }

    public class ViewHolder3 extends RecyclerView.ViewHolder {


        TextView tv_title;//ABCD

        public ViewHolder3(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
        }
    }
}
