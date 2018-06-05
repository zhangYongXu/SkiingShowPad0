package cn.geeksworld.skiingshow.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


import cn.geeksworld.skiingshow.R;
import cn.geeksworld.skiingshow.Tools.AppModelControlManager;
import cn.geeksworld.skiingshow.Tools.BackMain;
import cn.geeksworld.skiingshow.Tools.Tool;
import cn.geeksworld.skiingshow.adapter.MyListAdater;
import cn.geeksworld.skiingshow.model.SkiingModel;

/**
 * Created by xhs on 2018/3/14.
 */

public class ListActivity extends AppCompatActivity {

    private List<SkiingModel> itemList =  new ArrayList<SkiingModel>();
    private ListView listView;
    private MyListAdater listAdater;

    private int listType;
    private String listTypeSring;
    private String listJsonFilePath;

    private TextView listTypeTextView;

    private BackMain backMain;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppModelControlManager.hiddenSystemHandleView(this);

        setContentView(R.layout.activity_list);

        initNavigationView();

        initData();

        loadData();

        initView();

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
        titleView.setText("课程列表");
    }
    private void initData(){
        listType = getIntent().getIntExtra("homeListType",0);

        if(HomeActivity.ListTypeTongYongShuang == listType){
            listTypeSring = "双板教学通用版";
            listJsonFilePath = "jsonData/er_tong_shuang_ban_jiao_cheng/lesson_json_er_tong_shuang_ban.txt";
        }else if(HomeActivity.ListTypeErTongShuang == listType){
            listTypeSring = "双板教学儿童版";
            listJsonFilePath = "jsonData/xxx/xxx.txt";
        }else if(HomeActivity.ListTypeErTongDan == listType){
            listTypeSring = "单板教学儿童版";
            listJsonFilePath = "jsonData/xxx/xxx.txt";
        }else if(HomeActivity.ListTypeTongYongDan== listType){
            listTypeSring = "单板教学通用版";
            listJsonFilePath = "jsonData/xxx/xxx.txt";
        }else {
            listTypeSring = "";
            listJsonFilePath = "";
        }
    }
    private void initView(){
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(ListActivity.this,DetailActivity.class);
                SkiingModel skingModel = (SkiingModel)adapterView.getAdapter().getItem(i);
                intent.putExtra("skiingModel",skingModel);
                startActivity(intent);
            }
        });

        listTypeTextView = (TextView) findViewById(R.id.listTypeTextView);
        listTypeTextView.setText(listTypeSring);

        listAdater = new MyListAdater(this,itemList);
        listView.setAdapter(listAdater);
    }



    private void loadData(){
        String jsonString = getJson(this,listJsonFilePath);
        System.out.print("jsonString:"+jsonString);
        if(null != jsonString && !jsonString.isEmpty() && !jsonString.equals("")) {
            List<SkiingModel> datas = JSONArray.parseArray(jsonString, SkiingModel.class);
            itemList = datas;
        }


    }

    public static String getJson(Context mContext, String fileName) {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();
        AssetManager am = mContext.getAssets();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    am.open(fileName),"UTF-8"));
            String next = "";

            while (null != (next = br.readLine())) {
                sb.append(next);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            sb.delete(0, sb.length());
        }
        return sb.toString().trim();
    }
    public void backButtonClicked(View view){
        finish();
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
