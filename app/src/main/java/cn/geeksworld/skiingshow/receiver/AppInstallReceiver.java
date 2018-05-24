package cn.geeksworld.skiingshow.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import cn.geeksworld.skiingshow.Tools.AppModelControlManager;

/**
 * Created by xhs on 2018/3/29.
 */

public class AppInstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(Intent.ACTION_PACKAGE_ADDED)){//app在安装

        }else if(action.equals(Intent.ACTION_PACKAGE_REMOVED)){//app在卸载
            alert(context);
        }
    }

    private void alert(final Context context){
        AlertDialog.Builder bb = new AlertDialog.Builder(context);
        bb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                View decorView = ((AppCompatActivity)context).getWindow().getDecorView();
                int uiOptions =View.SYSTEM_UI_FLAG_VISIBLE;
                decorView.setSystemUiVisibility(uiOptions);
                dialogInterface.dismiss();
            }
        });
        bb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                View decorView = ((AppCompatActivity)context).getWindow().getDecorView();
                int uiOptions =View.SYSTEM_UI_FLAG_VISIBLE;
                decorView.setSystemUiVisibility(uiOptions);
                dialogInterface.dismiss();
            }
        });
        bb.setTitle("温馨提示");
        bb.setMessage("程序将要卸载");
        bb.show();

    }
}


