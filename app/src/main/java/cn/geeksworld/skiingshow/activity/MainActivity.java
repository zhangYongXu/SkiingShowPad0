package cn.geeksworld.skiingshow.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.VideoView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.hintview.TextHintView;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import cn.geeksworld.skiingshow.Tools.AppModelControlManager;
import cn.geeksworld.skiingshow.model.AppModeControlModel;
import cn.geeksworld.skiingshow.views.MyRollPagerView;
import cn.geeksworld.skiingshow.adapter.MyLLocalPagerAdapter;
import cn.geeksworld.skiingshow.R;

public class MainActivity extends AppCompatActivity {

    private MyLLocalPagerAdapter adapter_vp_header;
    private MyLLocalPagerAdapter adapter_vp_Footer;
    private MyRollPagerView viewPagerHeader;
    private MyRollPagerView viewPagerFooter;
    private int pageNumber = 1;
    private RequestManager glide;

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppModelControlManager.hiddenSystemHandleView(this);

        setContentView(R.layout.activity_main);

        glide = Glide.with(this);
        initView();



    }




    //中间按钮点击事件
    public void homeButtonClicked(View view){
       // Toast.makeText(this, "homeButtonClicked", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(MainActivity.this,HomeActivity.class);
        startActivity(intent);
    }
    private void initView() {

        viewPagerHeader =  (MyRollPagerView) findViewById(R.id.pageViewPagerHeader);
        viewPagerHeader.getViewPager().setOffscreenPageLimit(2);

        viewPagerFooter =  (MyRollPagerView) findViewById(R.id.pageViewPagerFooter);
        viewPagerFooter.getViewPager().setOffscreenPageLimit(2);

        videoView = (VideoView)findViewById(R.id.videoView);

        playVideo();
        loadBanners();

    }
    private void loadBanners() {
//        datas_banner.add("http://bmob-cdn-5476.b0.upaiyun.com/2017/10/25/9151395040c3e6da8087f773fe717c46.png");
//        datas_banner.add("http://bmob-cdn-5476.b0.upaiyun.com/2017/10/25/848e8f4740dd4457807bea6a26d2e5a5.png");
//        datas_banner.add("http://bmob-cdn-5476.b0.upaiyun.com/2017/10/25/e937864240528fff80f8eef60cd58052.png");
        int[] headerRes={
                R.mipmap.b1,
                R.mipmap.b2,
                R.mipmap.b3,
                R.mipmap.b4,
                R.mipmap.b5,
                R.mipmap.b6,
                R.mipmap.b7,
                R.mipmap.b8,
                R.mipmap.b9
        };
        adapter_vp_header = new MyLLocalPagerAdapter(viewPagerHeader, this, headerRes);
        viewPagerHeader.setAdapter(adapter_vp_header);

        int[] footerRes={
                R.mipmap.t1,
                R.mipmap.t2,
                R.mipmap.t3,
                R.mipmap.t4,
                R.mipmap.t5,
                R.mipmap.t6,
                R.mipmap.t7,
                R.mipmap.t8,
                R.mipmap.t9
        };
        adapter_vp_Footer = new MyLLocalPagerAdapter(viewPagerFooter, this, footerRes);
        viewPagerFooter.setAdapter(adapter_vp_Footer);
    }

    private  void  playVideo(){
        String uri="android.resource://"+getPackageName()+"/"+R.raw.home_logo;
        videoView.setVideoURI(Uri.parse(uri));
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.setLooping(true);//设置视频重复播放
            }
        });
        videoView.start();//播放视频
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        videoView.start();
    }
}
