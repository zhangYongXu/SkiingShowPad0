package cn.geeksworld.skiingshow.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
        requestWritePermission();


    }
    private static final int WRITE_PERMISSION = 0x01;
    private void requestWritePermission(){
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) !=  PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERMISSION);
        }

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) !=  PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},WRITE_PERMISSION);
        }
    }
    private void loadBanners() {
//        datas_banner.add("http://bmob-cdn-5476.b0.upaiyun.com/2017/10/25/9151395040c3e6da8087f773fe717c46.png");
//        datas_banner.add("http://bmob-cdn-5476.b0.upaiyun.com/2017/10/25/848e8f4740dd4457807bea6a26d2e5a5.png");
//        datas_banner.add("http://bmob-cdn-5476.b0.upaiyun.com/2017/10/25/e937864240528fff80f8eef60cd58052.png");
        int[] headerRes={
                R.mipmap.banner1,
                R.mipmap.banner2,
                R.mipmap.banner3,
                R.mipmap.banner4,
                R.mipmap.banner5
        };
        adapter_vp_header = new MyLLocalPagerAdapter(viewPagerHeader, this, headerRes);
        viewPagerHeader.setAdapter(adapter_vp_header);

        int[] footerRes={
                R.mipmap.banner6,
                R.mipmap.banner7,
                R.mipmap.banner8,
                R.mipmap.banner9,
                R.mipmap.banner10
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

    @Override
    protected void onResume() {
        super.onResume();
        AppModelControlManager.hiddenSystemHandleView(this);
    }
}
