package com.quyuanjin.imsix.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.quyuanjin.imsix.Constant;
import com.quyuanjin.imsix.R;
import com.quyuanjin.imsix.utils.SharedPreferencesUtils;
import com.yzq.zxinglibrary.encode.CodeCreator;

public class MyDialog extends Dialog implements View.OnClickListener {
    private Context context;
    String userid;

    public MyDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        setContentView(R.layout.dialog_show_image);
        //设置点击布局外则Dialog消失
        setCanceledOnTouchOutside(true);
        userid = (String) SharedPreferencesUtils.getParam(context.getApplicationContext(), "userid", "");

    }

    public void showDialog() {
    //    Window window = getWindow();
        //设置弹窗动画
  //      window.setWindowAnimations(R.style.style_dialog);
        //设置Dialog背景色
      //  window.setBackgroundDrawableResource(R.color.blue_light);
     //   WindowManager.LayoutParams wl = window.getAttributes();
        //设置弹窗位置
    //    wl.gravity = Gravity.CENTER;
     //   window.setAttributes(wl);


        Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        Bitmap bitmap = CodeCreator.createQRCode(userid, 800, 800, logo);
        ImageView imageView = findViewById(R.id.iv_image);
        imageView.setImageBitmap(bitmap);
        findViewById(R.id.iv_image).setOnClickListener(this);


        show();

    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}
