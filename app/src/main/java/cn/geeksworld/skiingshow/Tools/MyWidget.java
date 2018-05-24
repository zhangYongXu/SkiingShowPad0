package cn.geeksworld.skiingshow.Tools;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import cn.geeksworld.skiingshow.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xhs on 2017/11/13.
 */

public class MyWidget {
    /*
* 设置日期选择框
* */
    public static DatePickerDialog getDateDialog(Context context, DatePickerDialog.OnDateSetListener datePicker) {
        Calendar d = Calendar.getInstance(Locale.CHINA);
        //创建一个日历引用d，通过静态方法getInstance() 从指定时区 Locale.CHINA 获得一个日期实例
        Date myDate = new Date();
        //创建一个Date实例
        d.setTime(myDate);
        //设置日历的时间，把一个新建Date实例myDate传入
        int year = d.get(Calendar.YEAR);
        int month = d.get(Calendar.MONTH);
        int day = d.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(context, DatePickerDialog.THEME_HOLO_LIGHT, datePicker, year, month, day);
    }

    /**
     * 获取PopupWindow
     */
    public static PopupWindow getPopupWindow(Context context, View vie, int rId, boolean isBelowView) {
        //选择含有上下文的构造器
        PopupWindow popupWindow = new PopupWindow(context);
        //popupWindow显示的view
        Activity ac = (Activity) context;
        View view = ac.getLayoutInflater().inflate(rId, null);
        //获得屏幕的宽和高
        if (!isBelowView) {
            popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        }

//        view.getBackground().setAlpha(140);//这是设置弹出窗口的透明度的
        popupWindow.setContentView(view);

        //设置在popupWindow外点击的时候隐藏
        popupWindow.setOutsideTouchable(true);

        //设置背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //可以获得焦点,Button才可以点击,点击空白消失
        popupWindow.setFocusable(true);

        //第一个参数必须是当前Activity上面的组件view
        if (isBelowView)
            popupWindow.showAsDropDown(vie, 0, 0);
        else
            popupWindow.showAtLocation(vie, Gravity.BOTTOM, 0, 0);
        return popupWindow;
    }


    /**
     * 获取PopupWindow
     */
    public static PopupWindow getPopupWindow2(Context context, View vie, int rId, boolean isBelowView) {
        //选择含有上下文的构造器
        PopupWindow popupWindow = new PopupWindow(context);
        popupWindow.setAnimationStyle(R.style.ActionSheetDialogAnimation);
        //popupWindow显示的view
        Activity ac = (Activity) context;
        View view = ac.getLayoutInflater().inflate(rId, null);
        //获得屏幕的宽和高
        if (!isBelowView) {
            popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        }

//        view.getBackground().setAlpha(140);//这是设置弹出窗口的透明度的
        popupWindow.setContentView(view);

        //设置在popupWindow外点击的时候隐藏
        popupWindow.setOutsideTouchable(true);

        //设置背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //可以获得焦点,Button才可以点击,点击空白消失
        popupWindow.setFocusable(true);

        //第一个参数必须是当前Activity上面的组件view
        if (isBelowView)
            popupWindow.showAsDropDown(vie, 0, 0);
        else
            popupWindow.showAtLocation(vie, Gravity.BOTTOM, 0, 0);
        return popupWindow;
    }

}
