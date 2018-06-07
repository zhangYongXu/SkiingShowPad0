package cn.geeksworld.skiingshow.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.geeksworld.skiingshow.R;
import cn.geeksworld.skiingshow.Tools.AppModelControlManager;
import cn.geeksworld.skiingshow.Tools.ShareKey;
import cn.geeksworld.skiingshow.Tools.Tool;
import cn.geeksworld.skiingshow.adapter.MyGridViewAdapter;
import cn.geeksworld.skiingshow.adapter.RecyclerViewVideoItemAdapter;
import cn.geeksworld.skiingshow.model.MainVideoModel;
import cn.geeksworld.skiingshow.model.SkiingModel;
import cn.geeksworld.skiingshow.model.VideoModel;
import cn.geeksworld.skiingshow.views.LongTouchBtn;
import cn.geeksworld.skiingshow.views.SimpleVideoView;

/**
 * Created by xhs on 2018/3/15.
 */

public class DetailActivity2 extends AppCompatActivity{

    private DisplayMetrics outMetrics;
    private SimpleVideoView videoView;
    private RelativeLayout videoContainer;
    private RelativeLayout.LayoutParams params_landscape;
    private RelativeLayout.LayoutParams params_portrait;
    private ImageView play_bg;
    private AppCompatActivity mActivity;




    private SkiingModel skiingModel;
    private MainVideoModel mainVideoModel;

    private String localCommonDirPath;
    private List<VideoModel> cwVideoList = new ArrayList<VideoModel>();
    private List<VideoModel> zqVideoList = new ArrayList<VideoModel>();
    private VideoModel currentVideoModel;

    private GridView gridView;

    private MyGridViewAdapter gridViewAdapter;




    private ImageButton typeZhengQueImageButton;
    private ImageButton typeCuoWuImageButton;
    private ImageView typeBgImageView;



    private TextView fold1TextView;
    private TextView fold2TextView;


    private LongTouchBtn appControlBarBtn;

    private ImageView mainVideoImageView;

    private ImageView foldInfoLeftBgImageView;
    private ImageView foldInfoRightBgImageView;

    private ScrollView leftBtnContentRightScrollView;
    private ScrollView rightBtnContentRightScrollView;
    static ExecutorService fixedThreadPool;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppModelControlManager.hiddenSystemHandleView(this);

        setContentView(R.layout.activity_detail2);

        initNavigationView();

        initData();

        //loadData();

        initView();

//        initGridView();

        asyncloadData();

//        setFoldTextView();
//
//        showCurrentFaceImage();
//
//        showMainVideoFaceImageFromVideo();
//
//        prepareVideo();


    }
    //导航设置
    private void initNavigationView(){
        View inTitle = findViewById(R.id.inTitle);
        inTitle.findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoView.isFullScreen()){
                    videoView.setIsFullScreen(false);
                }
                finish();
            }
        });
        TextView titleView = inTitle.findViewById(R.id.title_name);
        titleView.setText("教学视频");

        final ImageView imageView = inTitle.findViewById(R.id.title_img_right);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.noVolume();
                if(videoView.isNoVolume()){
                    imageView.setImageDrawable(getResources().getDrawable(R.mipmap.simple_video_volum_close));
                }else {
                    imageView.setImageDrawable(getResources().getDrawable(R.mipmap.simple_video_volum_open));
                }
            }
        });
        imageView.setVisibility(View.VISIBLE);
    }
    //初始化处理数据 以及上个页面传来的数据
    private void initData(){
        mActivity = this;
        skiingModel = (SkiingModel)
                getIntent().getSerializableExtra("skiingModel");

        System.out.print("skiingModel:"+skiingModel.getImageName());

        localCommonDirPath = skiingModel.getLocalCommonDirPath() + "videoDetailJson/item"+(skiingModel.getOrderNum()+1)+"/";

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

        params_landscape = new RelativeLayout.LayoutParams(outMetrics.heightPixels, (int)(outMetrics.heightPixels * (9 / 16.0)));
        params_landscape.addRule(RelativeLayout.CENTER_IN_PARENT);
        params_landscape.addRule(RelativeLayout.CENTER_VERTICAL);
        params_landscape.addRule(RelativeLayout.CENTER_HORIZONTAL);
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */

    private void initView(){

        videoView = (SimpleVideoView) findViewById(R.id.videoView);
        play_bg = videoView.findViewById(R.id.play_bg);
        videoContainer = (RelativeLayout) findViewById(R.id.videoContainer);

        gridView = (GridView)findViewById(R.id.grid);





        float height = outMetrics.heightPixels;
        float width = outMetrics.widthPixels;

        int videoHeightPixel = (int) (width*(9.0/16.0));

        /* 弹出框测试
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
        String s = "width:"+width + " height:"+height + " videoHeightPixel:"+videoHeightPixel + " videoHeithtDp:"+videoHeithtDp;
        bb.setTitle("温馨提示");
        bb.setMessage(s);
        bb.show();
        */

        if(!ShareKey.TestImageAndVideo){
            RelativeLayout.LayoutParams linearParams =  (RelativeLayout.LayoutParams)videoContainer.getLayoutParams();
            linearParams.height = videoHeightPixel;
            videoContainer.setLayoutParams(linearParams);
        }






        typeZhengQueImageButton = (ImageButton) findViewById(R.id.typeZhengQueImageButton);
        typeCuoWuImageButton = (ImageButton) findViewById(R.id.typeCuoWuImageButton);
        typeBgImageView = (ImageView)findViewById(R.id.detailTypeBgImageView);


        fold1TextView = (TextView)findViewById(R.id.fold1TextView);
        fold2TextView = (TextView)findViewById(R.id.fold2TextView);

        appControlBarBtn = (LongTouchBtn) findViewById(R.id.appControlBarBtn);


        appControlBarBtn.setOnLongTouchListener(new LongTouchBtn.LongTouchListener() {
            @Override
            public void onLongTouch() {
               // Toast.makeText(mActivity, "长按结束", Toast.LENGTH_SHORT).show();
                appControlBarHandle();
            }
        },5000);

        leftBtnContentRightScrollView = (ScrollView)findViewById(R.id.leftBtnContentRightScrollView);
        rightBtnContentRightScrollView = (ScrollView)findViewById(R.id.rightBtnContentRightScrollView);

        foldInfoLeftBgImageView = (ImageView)findViewById(R.id.foldInfoLeftBgImageView);
        foldInfoRightBgImageView = (ImageView)findViewById(R.id.foldInfoRightBgImageView);
        foldInfoLeftBgImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFoldInfoLeftBgImageView();
            }
        });
        foldInfoRightBgImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFoldInfoRightBgImageView();
            }
        });

        mainVideoImageView = (ImageView)findViewById(R.id.mainVideoImageView);
        mainVideoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentVideoModel = null;
                selectVideoHandle();
            }
        });
    }

    private void initGridView(){
        int size = zqVideoList.size();
        int length = 140;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 10) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(5); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size); // 设置列数量=列表集合数

        gridViewAdapter= new MyGridViewAdapter(this,this, zqVideoList,skiingModel);
        gridView.setAdapter(gridViewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                VideoModel videoModel  = (VideoModel) zqVideoList.get(i);
                currentVideoModel = videoModel;
                selectVideoHandle();
            }
        });
    }

    private void selectedFoldInfoLeftBgImageView(){
        foldInfoLeftBgImageView.setBackgroundColor(getResources().getColor(R.color.activity_detial_fold_type_btn_selected_bg_color));
        foldInfoRightBgImageView.setBackgroundColor(getResources().getColor(R.color.activity_detial_fold_type_btn_normal_bg_color));
        leftBtnContentRightScrollView.setVisibility(View.VISIBLE);
        rightBtnContentRightScrollView.setVisibility(View.INVISIBLE);
    }
    private void  selectedFoldInfoRightBgImageView(){
        foldInfoLeftBgImageView.setBackgroundColor(getResources().getColor(R.color.activity_detial_fold_type_btn_normal_bg_color));
        foldInfoRightBgImageView.setBackgroundColor(getResources().getColor(R.color.activity_detial_fold_type_btn_selected_bg_color));
        leftBtnContentRightScrollView.setVisibility(View.INVISIBLE);
        rightBtnContentRightScrollView.setVisibility(View.VISIBLE);
    }
    private void selectVideoHandle(){
        setFoldTextView();
        if(currentVideoModel != null){
            showCurrentFaceImage();
            selectedFoldInfoRightBgImageView();
            fold2TextView.setText(currentVideoModel.getVideoActionIntroduction());
        }else {
            showMainVideoFaceImageFromVideo();
            selectedFoldInfoLeftBgImageView();
            fold2TextView.setText("");
        }
        prepareVideo();
        videoView.startPlayNow();

    }



    private void asyncloadData(){
        final Handler handler = new Handler();
        if (fixedThreadPool == null) {
            fixedThreadPool = Executors.newFixedThreadPool(4);
        }
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                loadData();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        initGridView();

                        setFoldTextView();

                        showCurrentFaceImage();

                        showMainVideoFaceImageFromVideo();

                        prepareVideo();
                    }
                });
            }
        });
    }

    private void loadData(){
        String  zqJsonString = getJson(this,skiingModel.getVideoZhengJueJsonFilePath());
        String  cwJsonString = getJson(this,skiingModel.getVideoCuoWuJsonFilePath());
        String  mainJsonString = getJson(this,skiingModel.getVideoMainJsonFilePath());

        System.out.print("zjJsonString:"+zqJsonString);
        if(null != zqJsonString && !zqJsonString.isEmpty() && !zqJsonString.equals("")){
            JSONArray zqJsonObj = (JSONArray) JSONArray.parse(zqJsonString);
            List<VideoModel> zqDatas = JSONArray.parseArray(Tool.parseJson(zqJsonObj), VideoModel.class);
            zqVideoList.clear();
            zqVideoList.addAll(zqDatas);
        }


        System.out.print("cwJsonString:"+cwJsonString);
        if(null != cwJsonString && !cwJsonString.isEmpty() && !cwJsonString.equals("")) {
            JSONArray cuJsonObj = (JSONArray) JSONArray.parse(cwJsonString);
            List<VideoModel> cuDatas = JSONArray.parseArray(Tool.parseJson(cuJsonObj), VideoModel.class);
            cwVideoList.clear();
            cwVideoList.addAll(cuDatas);
        }


        System.out.print("mainJsonString:"+mainJsonString);
        if(null != zqJsonString && !zqJsonString.isEmpty() && !zqJsonString.equals("")) {
            try {
                JSONObject obj = new JSONObject(mainJsonString);
                MainVideoModel model = JSONArray.parseObject(obj.toString(), MainVideoModel.class);
                mainVideoModel = model;
            }catch (Exception e){

            }
        }
    }



    private void showCurrentFaceImage(){
        if(null == currentVideoModel){
            showMainVideoFaceImageFromVideo();
            return;
        }

        showCurrentFaceImageFromVideo();
    }

    private void showCurrentFaceImageFromVideo(){
        String videoPath = getCurrentVideoPath();
        Bitmap bitmap = getVideoThumbnail(videoPath);

        play_bg.setImageBitmap(bitmap);


    }

    private void showMainVideoFaceImageFromVideo(){
        String videoPath = getMainVideoPath();
        Bitmap bitmap = getVideoThumbnail(videoPath);
        if(null == bitmap){
            return;
        }
        mainVideoImageView.setImageBitmap(bitmap);
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





    /*
    * Java文件操作 获取不带扩展名的文件名
    * */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }
    //从RAW文件夹获取文件uri
    private Uri uriWithFileName(String fileNmae) {
        String fileNameNoEx =  getFileNameNoEx(fileNmae);
        Resources res = getResources();
        final String packageName = getPackageName();
        int musicResId = res.getIdentifier(fileNameNoEx, "raw", packageName);

        String videoUri = "android.resource://" + packageName + "/" +musicResId;
        return Uri.parse(videoUri);
    }
    private Uri uriWithId(int id){
        Resources res = getResources();
        final String packageName = getPackageName();
        String videoUri = "android.resource://" + packageName + "/" + id;
        return Uri.parse(videoUri);
    }
    //从SD卡根据文件路径获取URI
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


    //准备播放
    private void prepareVideo(){
        try{
            setPlay();
        }catch (NoSuchFieldException e){
            e.printStackTrace();
            System.out.print(e.toString());
        }catch (IllegalAccessException e){
            e.printStackTrace();
            System.out.print(e.toString());
        }
    }
    //获取当前视频路径
    private String getCurrentVideoPath(){
        String videoPath = "";
        if(null != currentVideoModel){
            videoPath = ShareKey.getSDDir() + skiingModel.getLocalCommonSDDirPath() + "第" + (skiingModel.getOrderNum()+1) + "课/" + currentVideoModel.getLocalCommonVideoSDPath();
        }
        return videoPath;
    }
    //获取主视频路径
    private String getMainVideoPath(){
        String videoPath = "";
        if(null != mainVideoModel){
             videoPath = ShareKey.getSDDir() + skiingModel.getLocalCommonSDDirPath() + "第" + (skiingModel.getOrderNum()+1) + "课/" + mainVideoModel.getLocalCommonVideoSDPath();
        }
        return videoPath;
    }
    private void setPlay() throws NoSuchFieldException, IllegalAccessException {
        if(null == currentVideoModel){;
            String mainVideoSDPath = getMainVideoPath();
            Uri mainVideoUri = uriWithFilePath(mainVideoSDPath);

            videoView.setVideoUri(mainVideoUri);
            return;
        }


        String videoSDPath = getCurrentVideoPath();
        Uri videoUri = uriWithFilePath(videoSDPath);


        videoView.setVideoUri(videoUri);
    }

    // 检查是否存在SD卡
    public boolean checkTheSdCard() {
        boolean  bIfSDExist = false;
        /* 判断存储卡是否存在 */
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            bIfSDExist = true;
        } else {
            bIfSDExist = false;
            mMakeTextToast("请安装SD卡", true);
        }
        return bIfSDExist;
    }

    // 弹出提示对话框
    public void mMakeTextToast(String str, boolean isLong) {
        if (isLong == true) {
            Toast.makeText(this, str, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
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

    public void typeZhengQueBtnClcked(View view){
        typeBgImageView.setImageResource(R.mipmap.detail_type_bg_blue);

        mainVideoImageView.setVisibility(View.VISIBLE);

        gridView.setVisibility(View.VISIBLE);
    }
    public void typeCuoWuBtnClcked(View view){
        typeBgImageView.setImageResource(R.mipmap.detail_type_bg_red);

        mainVideoImageView.setVisibility(View.GONE);

        gridView.setVisibility(View.GONE);
    }

    public void backButtonClicked(View view){
        finish();
    }

    public void video_full(boolean fullScreen) {
        if (fullScreen) {
            //otherContainer.setVisibility(View.GONE);
            videoContainer.setLayoutParams(params_landscape);
            //videoView.setLayoutParams(params_landscape);
        } else {
            //otherContainer.setVisibility(View.VISIBLE);
            videoContainer.setLayoutParams(params_portrait);
            //videoView.setLayoutParams(params_portrait);
        }
    }


    @Override
    public void onBackPressed() {
        if (videoView.isFullScreen()) {
            videoView.setIsFullScreen(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null)
            videoView.suspend();
    }


    ////

    private void setFoldTextView(){

        if(mainVideoModel != null){
            fold1TextView.setText(mainVideoModel.getVideoLessonIntro());
        }
        if(currentVideoModel != null){
            fold2TextView.setText(currentVideoModel.getVideoActionIntroduction());
            //fold3TextView.setText(currentVideoModel.getVideoCuoWuAction());
        }
    }



    int count = 0;
    public void appControlBarBtnClicked(View view) {
        if(AppModelControlManager.isShowSystemHandleView()){
            return;
        }
        count++;
        if(count>=5){
           // Toast.makeText(this, "点击了五次", Toast.LENGTH_SHORT).show();
            count = 0;
            appControlBarHandle();
        }
    }

    private void appControlBarHandle(){
        AppModelControlManager.showSystemHandleView(this);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        AppModelControlManager.hiddenSystemHandleView(mActivity);
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 10000);//10秒后处理
    }
}
