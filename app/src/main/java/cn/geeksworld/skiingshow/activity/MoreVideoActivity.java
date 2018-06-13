package cn.geeksworld.skiingshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.geeksworld.skiingshow.R;
import cn.geeksworld.skiingshow.Tools.AppModelControlManager;
import cn.geeksworld.skiingshow.Tools.BackMain;
import cn.geeksworld.skiingshow.Tools.ShareKey;
import cn.geeksworld.skiingshow.adapter.RecyclerViewMoreVideoItemAdapter;
import cn.geeksworld.skiingshow.adapter.RecyclerViewVideoItemAdapter;
import cn.geeksworld.skiingshow.model.VideoModel;

/**
 * Created by xhs on 2018/6/1.
 */

public class MoreVideoActivity extends AppCompatActivity {

    private RecyclerView recycleView;
    private RecyclerViewMoreVideoItemAdapter itemAdapter;

    private List<File> dataList = new LinkedList<>();
    private BackMain backMain;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_video);
        AppModelControlManager.hiddenSystemHandleView(this);
        initNavigationView();
        initView();
        initRecycleView();
        loadData();
        initBackMain();
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
        titleView.setText("更多视频");
    }

    private void initView(){
        recycleView = (RecyclerView) findViewById(R.id.recycleView);

    }
    private void initRecycleView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(linearLayoutManager);//这里用线性显示 类似于listview

        recycleView.setFocusableInTouchMode(false);
        recycleView.requestFocus();

        itemAdapter = new RecyclerViewMoreVideoItemAdapter(this, dataList);

        itemAdapter.setItemClickListener(new RecyclerViewMoreVideoItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                File file = dataList.get(position);
                String videoPath = file.getAbsolutePath();
                Intent intent=new Intent(MoreVideoActivity.this,MoreVideoPlayActivity.class);
                intent.putExtra(MoreVideoPlayActivity.IntentStringExtraideoPathKeyVideoPath,videoPath);
                startActivity(intent);
            }
        });
        recycleView.setAdapter(itemAdapter);

    }
    private void loadData(){

       String otherVideoPath = ShareKey.getSDDir() + "更多内容视频";

        File file = new File(otherVideoPath);

        // 判断SD卡是否存在，并且是否具有读写权限
        if (file.exists()) {
            File[] files = file.listFiles();// 读取文件夹下文件
            dataList.clear();
            dataList.addAll(Arrays.asList(files));
            itemAdapter.notifyDataSetChanged();

            Log.i("getExternalStorageState","getExternalStorageState");
        }
    }

    @Override
    protected void onResume() {
        timeStart();
        super.onResume();
    }
    private void initBackMain(){
        backMain = new BackMain(1000 * 60 * 2, 1000, this);//  2分钟无操作回主页
    }

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
