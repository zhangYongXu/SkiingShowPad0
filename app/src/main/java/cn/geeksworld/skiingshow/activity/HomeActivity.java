package cn.geeksworld.skiingshow.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import cn.geeksworld.skiingshow.R;
import cn.geeksworld.skiingshow.Tools.AppModelControlManager;
import cn.geeksworld.skiingshow.Tools.BackMain;

/**
 * Created by xhs on 2018/3/13.
 */

public class HomeActivity extends AppCompatActivity {


    public static final int ListTypeTongYongShuang =1;
    public static final int ListTypeErTongShuang =2;
    public static final int ListTypeErTongDan =3;
    public static final int ListTypeTongYongDan =4;



    private BackMain backMain;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppModelControlManager.hiddenSystemHandleView(this);

        setContentView(R.layout.activity_home);
        initBackMain();
        initNavigationView();
        //根据设置，判断是否在test模式下
        //AppModelControlManager.judgeAppControllTestMode(this);
    }

    //导航设置
    private void initNavigationView(){
        View inTitle = findViewById(R.id.inTitle);
        inTitle.findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView titleView = inTitle.findViewById(R.id.title_name);
        titleView.setText("滑雪教程");
    }

    public void backButtonClicked(View view){
        finish();
    }

    //通用双板
    public void homeToListButtonErTongShuangClicked(View view){
        toListActivity(ListTypeTongYongShuang);
    }
    //儿童双板
    public void homeToListButtonChengRenShuangClicked(View view){
        //toListActivity(ListTypeErTongShuang);
    }
    //儿童单板
    public void homeToListButtonErTongDanClicked(View view){
        //toListActivity(ListTypeErTongDan);
    }
    //通用单板
    public void homeToListButtonChengRenDanClicked(View view){
        //toListActivity(ListTypeTongYongDan);
    }


    private void toListActivity(int listType){
        Intent intent=new Intent(HomeActivity.this,ListActivity.class);
        intent.putExtra("homeListType",listType);
        startActivity(intent);
    }

    public void moreContentImageViewClicked(View view){
        Intent intent=new Intent(HomeActivity.this,MoreVideoActivity.class);
        startActivity(intent);
    }



    //////////////////  在几分钟之内无操作就返回首页 /////////////////
    private void initBackMain(){
        backMain = new BackMain(1000 * 60 * 2, 1000, this);//  2分钟无操作回主页
    }

    @Override
    protected void onResume() {
        timeStart();
        super.onResume();

        AppModelControlManager.hiddenSystemHandleView(this);
    }


    //region 无操作 返回主页
    private void timeStart() {
        new Handler(getMainLooper()).post(new Runnable() {

            @Override
            public void run() {

                backMain.start();

            }
        });
    }
    @Override
    /**
     * 主要的方法，重写dispatchTouchEvent
     *
     * @param ev
     * @return
     */
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            //获取触摸动作，如果ACTION_UP，计时开始。
            case MotionEvent.ACTION_UP:
                backMain.start();
                break;
            //否则其他动作计时取消
            default:
                backMain.cancel();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
    @Override
    protected void onPause() {
        super.onPause();
        backMain.cancel();
    }
}
