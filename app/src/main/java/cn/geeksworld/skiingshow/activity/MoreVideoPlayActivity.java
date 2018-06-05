package cn.geeksworld.skiingshow.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import cn.geeksworld.skiingshow.R;
import cn.geeksworld.skiingshow.Tools.AppModelControlManager;
import cn.geeksworld.skiingshow.Tools.Tool;
import cn.geeksworld.skiingshow.views.Simple2VideoView;
import cn.geeksworld.skiingshow.views.SimpleVideoView;

/**
 * Created by xhs on 2018/6/4.
 */

public class MoreVideoPlayActivity extends AppCompatActivity {
    public static final String IntentStringExtraideoPathKeyVideoPath = "videoPath";

    private DisplayMetrics outMetrics;

    private RelativeLayout.LayoutParams params_portrait;
    private RelativeLayout.LayoutParams params_landscape;

    private RelativeLayout videoContainer;
    private Simple2VideoView videoView;
    private ImageView play_bg;
    private String videoPath;

    private View inTitle;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_video_play);
        AppModelControlManager.hiddenSystemHandleView(this);
        initNavigationView();
        initData();
        initView();
        showFaceImageFromVideo();
        prepareVideo();
    }
    //导航设置
    private void initNavigationView(){
        inTitle = findViewById(R.id.inTitle);
        inTitle.findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoView.isFullScreen()) {
                    videoView.setIsFullScreen(false);
                } else {
                    finish();
                }
            }
        });
        TextView titleView = inTitle.findViewById(R.id.title_name);
        titleView.setText("更多视频播放");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            video_full(true);
           // Toast.makeText(getApplicationContext(), "横屏", Toast.LENGTH_SHORT).show();
        }else{
            video_full(false);
            //Toast.makeText(getApplicationContext(), "竖屏", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData(){
        if (Build.VERSION.SDK_INT < 17 ) {
            outMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        }else {
            Context context = getApplicationContext();
            DisplayMetrics dm = new DisplayMetrics();
            outMetrics = dm;
            WindowManager windowMgr = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            windowMgr.getDefaultDisplay().getRealMetrics(dm);
        }

        params_portrait = new RelativeLayout.LayoutParams(outMetrics.widthPixels, (int)(outMetrics.widthPixels * (9 / 16.0)));
        params_portrait.addRule(RelativeLayout.CENTER_IN_PARENT);
        params_portrait.addRule(RelativeLayout.CENTER_VERTICAL);
        params_portrait.addRule(RelativeLayout.CENTER_HORIZONTAL);

        params_landscape = new RelativeLayout.LayoutParams(outMetrics.heightPixels, (int)(outMetrics.heightPixels * (9 / 16.0)));
        params_landscape.addRule(RelativeLayout.CENTER_IN_PARENT);
        params_landscape.addRule(RelativeLayout.CENTER_VERTICAL);
        params_landscape.addRule(RelativeLayout.CENTER_HORIZONTAL);

        videoPath = getIntent().getStringExtra(IntentStringExtraideoPathKeyVideoPath);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    private void initView(){
        videoContainer = (RelativeLayout) findViewById(R.id.videoContainer);
        videoView = (Simple2VideoView) findViewById(R.id.videoView);
        play_bg = videoView.findViewById(R.id.play_bg);

        videoContainer.setLayoutParams(params_portrait);


    }

    //准备播放
    private void prepareVideo(){
        if(Tool.isNull(videoPath)){
            return;
        }
        setPlay();
    }

    private void setPlay(){

        Uri videoUri = uriWithFilePath(videoPath);

        videoView.setVideoUri(videoUri);
    }

    private Uri uriWithFilePath(String filePath){

        //String path = "/storage/extsd/video/er_tong_dan_ban_jiao_cheng_item1_cuo_wu_4.mp4";
        String path = filePath;
        File file = new File(path);
        if(file.exists()){
            Uri uri = Uri.parse(path);
            return uri;
        }else {
            // Toast.makeText(this,"视频文件不存在:"+path,Toast.LENGTH_LONG).show();


            tipNoVideoFile(path);

            String videoUri = filePath;
            return Uri.parse(videoUri);
        }
    }

    //视频文件找不到 提示
    private void tipNoVideoFile(String path){
        AlertDialog.Builder bb = new AlertDialog.Builder(this);
        bb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        bb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        bb.setTitle("温馨提示");
        bb.setMessage("视频文件不存在，请检查SD卡,该文件路径为："+ path );
        bb.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null)
            videoView.suspend();
    }

    private void showFaceImageFromVideo(){
        Bitmap bitmap = getVideoThumbnail(videoPath);

        play_bg.setImageBitmap(bitmap);

    }
    public static Bitmap getVideoThumbnail(String videoPath) {
        try {
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(videoPath);
            Bitmap bitmap = media.getFrameAtTime(0,MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            return bitmap;
        }catch (IllegalArgumentException e){
            Log.i("exception  xx","");
        }
        return null;
    }

    public void video_full(boolean fullScreen) {

        if (fullScreen) {
            videoContainer.setLayoutParams(params_landscape);
            inTitle.setVisibility(View.GONE);
        } else {
            videoContainer.setLayoutParams(params_portrait);
            inTitle.setVisibility(View.VISIBLE);
        }
    }
}
