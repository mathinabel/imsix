package com.quyuanjin.imsix.utils;

import android.content.Context;
import android.os.CountDownTimer;

import com.quyuanjin.imsix.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SDialog {
    private static int i = -1;
    public static void showSweetDialog(Context context) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("注册中");
        pDialog.show();
        pDialog.setCancelable(false);
        new CountDownTimer(800 * 10, 800) {
            public void onTick(long millisUntilFinished) {
                // you can change the progress bar color by ProgressHelper every 800 millis
                i++;
                switch (i) {
                    case 0:
                        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.blue_btn_bg_color));
                        break;
                    case 1:
                        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.material_deep_teal_50));
                        break;
                    case 2:
                        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.success_stroke_color));
                        break;
                    case 3:
                        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.material_deep_teal_20));
                        break;
                    case 4:
                        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.material_blue_grey_80));
                        break;
                    case 5:
                        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.warning_stroke_color));
                        break;
                    case 6:
                        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.success_stroke_color));
                        break;
                    case 7:
                        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.material_deep_teal_20));
                        break;
                    case 8:
                        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.material_blue_grey_80));
                        break;
                    case 9:
                        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.warning_stroke_color));
                        break;
                }
            }

            public void onFinish() {
                i = -1;
                pDialog.setTitleText("哪里出了问题!请检查后重新注册。")
                        .setConfirmText("OK")
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            }
        }.start();
    }

    public static void baseSweetDialog(Context context,String titleText){
        new SweetAlertDialog(context)
                .setTitleText(titleText)
                .show();
    }
    public static void aTitleWithATextNnderSweetDialog(Context context,String titleText,String contentText){
        new SweetAlertDialog(context)
                .setTitleText(titleText)
                .setContentText(contentText)
                .show();
    }



    public static void errorSweetDialog(Context context,String titleText,String contentText){
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(titleText)
                .setContentText(contentText)
                .show();
    }

    public static void warningSweetDialog(Context context,String titleText,String contentText,String confirmText){
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(titleText)
                .setContentText(contentText)
                .setConfirmText(confirmText)
                .show();
    }

    public static void successSweetDialog(Context context,String titleText,String contentText){
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(titleText)
                .setContentText(contentText)
                .show();
    }

    public static void messagewithCustomIconSweetDialog(Context context,String titleText,String contentText,int imageResId){
        new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText(titleText)
                .setContentText(contentText)
                .setCustomImage(imageResId)
                .show();
    }

    //Bind the listener to confirm button：
    public static void listenerSweetDialog(Context context,String titleText,String contentText){
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setConfirmText("Yes,delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }
    //Show the cancel button and bind listener to it：
    public static void cancelSweetDialog(Context context,String titleText,String contentText){
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setCancelText("No,cancel plx!")
                .setConfirmText("Yes,delete it!")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }
}
