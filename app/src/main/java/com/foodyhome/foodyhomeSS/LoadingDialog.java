package com.foodyhome.foodyhomeSS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingDialog {
    public Activity activity;
    public AlertDialog dialog;
    LoadingDialog(Activity myActivity){
        activity=myActivity;
    }
    @SuppressLint("InflateParams")
    void startLoadingDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater=activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.custom_loading_dialog,null));
        builder.setCancelable(false);
        dialog=builder.create();
        dialog.show();
    }
    void dismissDialog(){
     dialog.dismiss();
    }
}
