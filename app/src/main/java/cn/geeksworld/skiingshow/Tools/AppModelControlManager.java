package cn.geeksworld.skiingshow.Tools;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import cn.geeksworld.skiingshow.model.AppModeControlModel;

/**
 * Created by xhs on 2018/3/22.
 */

public class AppModelControlManager {

    static boolean isShow = true;
    static public void judgeAppControllTestMode(final Activity activity){
        AppModeControlModel.getNetModel(new AppModeControlModel.OnGetModelComplete() {
            @Override
            public void success(AppModeControlModel model) {
                if(null != model && model.getIsTestMode() == 1){//是测试模式,打开底部系统控制条
                    showSystemHandleView(activity);
                }else {
                    hiddenSystemHandleView(activity);
                }
            }
            @Override
            public void failed(String error) {
                hiddenSystemHandleView(activity);
            }
        });
    }
    //显示系统底部操作控制条
    static public void showSystemHandleView(Activity activity){

        View decorView = activity.getWindow().getDecorView();
        int uiOptions =View.SYSTEM_UI_FLAG_VISIBLE;
        decorView.setSystemUiVisibility(uiOptions);
        isShow = true;
    }
    //隐藏系统底部操作控制条
    static public void hiddenSystemHandleView(Activity activity){

        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        isShow = false;
    }

    static public boolean isShowSystemHandleView(){
       return isShow;
    }
}
