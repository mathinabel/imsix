package com.quyuanjin.imsix.chatsession;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.quyuanjin.imsix.R;
import com.quyuanjin.imsix.utils.BubbleImageView;
import com.quyuanjin.imsix.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import netty.NettyLongChannel;
import netty.ProtoConstant;
import okhttp3.Call;
import okhttp3.Request;

public class ChatAdapter extends RecyclerView.Adapter {
    private ArrayList<Msg> arrayList;
    private Context context;

    private OnItemLongClickListener onItemLongClickListener;
    private OnItemVoiceClickListener onItemVoiceClickListener;
    private String s;

    //设置回调接口
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemClickLitener) {
        this.onItemLongClickListener = mOnItemClickLitener;

    }

    public interface OnItemVoiceClickListener {
        void onItemVoiceClick(View view, int position);
    }

    public void setOnItemVoiceClickListener(OnItemVoiceClickListener onItemVoiceClickListener) {
        this.onItemVoiceClickListener = onItemVoiceClickListener;

    }

    public ChatAdapter(Context context, ArrayList<Msg> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        s = (String) SharedPreferencesUtils.getParam(parent.getContext().getApplicationContext(), "CutPath", "");

        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_text_receive, parent, false);

            return new ViewHolder(view);

        } else if (viewType == 1) {
            View view1 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_text_send, parent, false);
            return new ViewHolder1(view1);
        } else if (viewType == 2) {
            View view2 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_image_receive, parent, false);
            return new ViewHolder2(view2);
        } else if (viewType == 3) {
            View view3 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_image_send, parent, false);
            return new ViewHolder3(view3);
        } else if (viewType == 4) {
            View view4 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_audio_receive, parent, false);
            return new ViewHolder4(view4);
        } else if (viewType == 5) {
            View view5 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_audio_send, parent, false);
            return new ViewHolder5(view5);
        } else if (viewType == 6) {
            View view6 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_video_receive, parent, false);
            return new ViewHolder6(view6);
        } else if (viewType == 7) {
            View view7 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_video_send, parent, false);
            return new ViewHolder7(view7);
        } else if (viewType == 8) {
            View view8 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_location_receive, parent, false);
            return new ViewHolder8(view8);
        } else {
            View view2 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_location_send, parent, false);
            return new ViewHolder1(view2);
        }
    }

    private RequestOptions mOptions = new RequestOptions()
            .centerCrop()
            .format(DecodeFormat.PREFER_RGB_565)
            .placeholder(R.mipmap.default_img);

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        switch (holder.getItemViewType()) {
            case 0://收到消息
                ((ViewHolder) holder).simpleDraweeView.setImageURI(Uri.parse(arrayList.get(position).getNetPath()));

                ((ViewHolder) holder).textView.setText(arrayList.get(position).getMsg());
                //通过为条目设置点击事件触发回调
                if (onItemLongClickListener != null) {
                    ((ViewHolder) holder).textView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            onItemLongClickListener.onItemLongClick(view, position);
                            return false;
                        }
                    });
                }


                break;
            case 1://发送消息
                ((ViewHolder1) holder).textView.setText(arrayList.get(position).getMsg());
                if (!("".equals(s))) {
                    ((ViewHolder1) holder).simpleDraweeView.setImageURI(Uri.parse(s));
                } else {
                    ((ViewHolder1) holder).simpleDraweeView.setImageURI(Uri.parse(arrayList.get(position).getNetPath()));
                }

                //通过为条目设置点击事件触发回调
                if (onItemLongClickListener != null) {
                    ((ViewHolder1) holder).textView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            onItemLongClickListener.onItemLongClick(view, position);
                            return false;
                        }
                    });
                }

                break;


            case 2://picture received

                Glide.with(((ViewHolder2) holder).bubbleImageView.getContext()).
                        load(Uri.parse(arrayList.get(position).getNetPath())).apply(mOptions).into(((ViewHolder2) holder).bubbleImageView);
                ((ViewHolder2) holder).bubbleImageView.setProgressVisible(false);
                ((ViewHolder2) holder).bubbleImageView.showShadow(false);
                ((ViewHolder2) holder).imageView.setImageURI(Uri.parse(arrayList.get(position).getNetPath()));

                break;
            case 3:


                Glide.with(((ViewHolder3) holder).bubbleImageView.getContext()).
                        load(Uri.fromFile(new File(arrayList.get(position).getLocalPath()))).apply(mOptions).into(((ViewHolder3) holder).bubbleImageView);

                ((ViewHolder3) holder).bubbleImageView.setProgressVisible(false);
                ((ViewHolder3) holder).bubbleImageView.showShadow(false);

                ((ViewHolder3) holder).bubbleImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                if (!("".equals(s))) {
                    ((ViewHolder3) holder).imageView.setImageURI(Uri.parse(s));
                } else {
                    ((ViewHolder3) holder).imageView.setImageURI(Uri.parse(arrayList.get(position).getNetPath()));
                }
                break;
            case 4://voice received

                ((ViewHolder4) holder).llAudio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemVoiceClickListener.onItemVoiceClick(view, position);
                    }
                });
                ((ViewHolder4) holder).tvDuration.setText(String.valueOf((int) arrayList.get(position).getRecorderTime()));
                ((ViewHolder4) holder).my_image_view.setImageURI(Uri.parse(arrayList.get(position).getNetPath()));

                break;
            case 5:

                ((ViewHolder5) holder).rlAudio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemVoiceClickListener.onItemVoiceClick(view, position);
                    }
                });

                ((ViewHolder5) holder).tvDuration.setText(String.valueOf((int) arrayList.get(position).getRecorderTime()));
                if (!("".equals(s))) {
                    ((ViewHolder5) holder).my_image_view.setImageURI(Uri.parse(s));
                } else {
                    ((ViewHolder5) holder).my_image_view.setImageURI(Uri.parse(arrayList.get(position).getNetPath()));
                }
                break;
            case 6://video

                break;
            case 7:

                break;
            case 8://location

                break;
        }
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {


        if (arrayList.get(position).getContentType() == (0)) {
            return 0;
        } else if (arrayList.get(position).getContentType() == (1)) {
            return 1;
        } else if (arrayList.get(position).getContentType() == (2)) {

            return 2;
        } else if (arrayList.get(position).getContentType() == (3)) {
            return 3;
        } else if (arrayList.get(position).getContentType() == (4)) {
            return 4;
        } else if (arrayList.get(position).getContentType() == (5)) {
            return 5;
        } else if (arrayList.get(position).getContentType() == (6)) {
            return 6;
        } else if (arrayList.get(position).getContentType() == (7)) {
            return 7;
        } else if (arrayList.get(position).getContentType() == (8)) {
            return 8;
        } else {
            return 9;
        }


    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        SimpleDraweeView simpleDraweeView;

        public ViewHolder(View view) {
            super(view);
            simpleDraweeView = view.findViewById(R.id.my_image_view);
            textView = view.findViewById(R.id.tvText);
        }
    }

    static class ViewHolder1 extends RecyclerView.ViewHolder {

        TextView textView;
        SimpleDraweeView simpleDraweeView;

        public ViewHolder1(View view) {
            super(view);
            textView = view.findViewById(R.id.tvText);
            simpleDraweeView = view.findViewById(R.id.my_image_view);
        }
    }

    static class ViewHolder2 extends RecyclerView.ViewHolder {

        BubbleImageView bubbleImageView;
        SimpleDraweeView imageView;

        public ViewHolder2(View view) {
            super(view);
            bubbleImageView = view.findViewById(R.id.bivPic11);
            imageView = view.findViewById(R.id.ivAvatar11);

        }
    }

    static class ViewHolder3 extends RecyclerView.ViewHolder {
        BubbleImageView bubbleImageView;
        SimpleDraweeView imageView;

        public ViewHolder3(View view) {
            super(view);
            bubbleImageView = view.findViewById(R.id.bivPic12);
            imageView = view.findViewById(R.id.ivAvatar12);
        }
    }

    static class ViewHolder4 extends RecyclerView.ViewHolder {
        TextView tvDuration;
        RelativeLayout llAudio;
        SimpleDraweeView my_image_view;

        public ViewHolder4(View view) {
            super(view);
            tvDuration = view.findViewById(R.id.tvDuration11);
            llAudio = view.findViewById(R.id.llAudio);
            my_image_view = view.findViewById(R.id.my_image_view1);
        }
    }

    static class ViewHolder5 extends RecyclerView.ViewHolder {
        TextView tvDuration;
        RelativeLayout rlAudio;
        SimpleDraweeView my_image_view;

        public ViewHolder5(View view) {
            super(view);

            tvDuration = view.findViewById(R.id.tvDuration);
            rlAudio = view.findViewById(R.id.rlAudio);
            my_image_view = view.findViewById(R.id.my_image_view2);
        }
    }

    static class ViewHolder6 extends RecyclerView.ViewHolder {


        public ViewHolder6(View view) {
            super(view);


        }
    }

    static class ViewHolder7 extends RecyclerView.ViewHolder {


        public ViewHolder7(View view) {
            super(view);


        }
    }

    static class ViewHolder8 extends RecyclerView.ViewHolder {


        public ViewHolder8(View view) {
            super(view);


        }
    }
}
