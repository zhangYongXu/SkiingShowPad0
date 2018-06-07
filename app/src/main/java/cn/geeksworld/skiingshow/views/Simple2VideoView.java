package cn.geeksworld.skiingshow.views;

/**
 * Created by xhs on 2017/12/20.
 */

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.VideoView;

import cn.geeksworld.skiingshow.R;
import cn.geeksworld.skiingshow.Tools.Tool;
import cn.geeksworld.skiingshow.activity.DetailActivity;
import cn.geeksworld.skiingshow.activity.MoreVideoActivity;
import cn.geeksworld.skiingshow.activity.MoreVideoPlayActivity;
//import com.xhs.sc.activity.Page1VideoPlayAc;
//import com.xhs.sc.activity.Page2VideoPlayAc;

public class Simple2VideoView extends RelativeLayout implements OnClickListener {

    private Context context;
    private View mView;
    private VideoView mVideoView;//视频控件
    private  MediaPlayer mediaPlayer;

    private ImageView mBigPlayBtn;//大的播放按钮
    private ImageView mBigPauseBtn;//大的暂停按钮
    private ImageView play_bg;//播放背景
    private ImageButton mPlayBtn;//播放按钮
    private ImageView mFullScreenBtn;//全屏按钮
    private SeekBar mPlayProgressBar;//播放进度条
    private TextView time_left;//播放时间
    private TextView time_right;//播放总时间
    private LinearLayout mControlPanel;

    private Uri mVideoUri = null;


    private int mVideoDuration;//视频毫秒数
    private int mCurrentProgress;//毫秒数

    private Runnable mUpdateTask;
    private Thread mUpdateThread;

    private final int UPDATE_PROGRESS = 0;
    private final int EXIT_CONTROL_PANEL = 1;
    private boolean stopThread = true;//停止更新进度线程标志

    private Point screenSize = new Point();//屏幕大小
    private boolean mIsFullScreen = false;//是否全屏标志

    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    private float startX = 0;
    private float endX = 0;
    private float startY = 0;
    private float endY = 0;
    private DisplayMetrics metrics;
    private int count = 0;
    private AudioManager mAudioManager;
    private int currentLight;
    private TextView light_progress;
    private View light_progress_container;
    private int screenWidth;
    private ImageView light_icon;

    private  TextView TipTextView;
    public Simple2VideoView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public Simple2VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public Simple2VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_PROGRESS:
                    mPlayProgressBar.setProgress(mCurrentProgress);
                    setPlayTime(mCurrentProgress);
                    break;
                case EXIT_CONTROL_PANEL:
                    //执行退出动画
                    if (mControlPanel.getVisibility() != View.GONE) {
                        mControlPanel.setVisibility(View.GONE);
                        mBigPauseBtn.setVisibility(INVISIBLE);
                    }
                    break;
            }
        }
    };

    //初始化控件
    private void init(final Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        mView = LayoutInflater.from(context).inflate(R.layout.simple_video2_view, this);
        mBigPlayBtn = mView.findViewById(R.id.big_play_button);
        mBigPauseBtn = mView.findViewById(R.id.big_pase_button);
        play_bg = mView.findViewById(R.id.play_bg);
        mPlayBtn = mView.findViewById(R.id.play_button);
        mFullScreenBtn = mView.findViewById(R.id.full_screen_button);
        mPlayProgressBar = mView.findViewById(R.id.progress_bar);
        light_progress_container = mView.findViewById(R.id.light_progress_container);
        light_icon = mView.findViewById(R.id.light_icon);
        light_progress = mView.findViewById(R.id.light_progress);
        time_left = mView.findViewById(R.id.time_left);
        time_right = mView.findViewById(R.id.time_right);
        mControlPanel = mView.findViewById(R.id.control_panel);
        mVideoView = mView.findViewById(R.id.video_view);
        //获取屏幕大小
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(screenSize);


        //设置控制面板初始可见
        mControlPanel.setVisibility(View.VISIBLE);


        //设置大的暂停按钮不可见
        mBigPauseBtn.setVisibility(INVISIBLE);
        //设置大的播放按钮可见
        mBigPlayBtn.setVisibility(View.VISIBLE);
        //设置媒体控制器
//      mMediaController = new MediaController(context);
//      mMediaController.setVisibility(View.GONE);
//      mVideoView.setMediaController(mMediaController);

        mVideoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer = mp;
                if(isNoVolume()){
                    mediaPlayer.setVolume(0,0);
                }
                //视频加载完成后才能获取视频时长
                initVideo();
            }
        });

        //视频播放完成监听器
        mVideoView.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayBtn.setSelected(false);
                mBigPlayBtn.setVisibility(VISIBLE);
                mVideoView.seekTo(0);
                mPlayProgressBar.setProgress(0);
                setPlayTime(0);
                stopThread = true;
                //sendHideControlPanelMessage();
            }
        });


        //mView.setOnClickListener(this);

        setScrollListenner(context);

        TipTextView = mView.findViewById(R.id.TipTextView);
    }

    private boolean isMoving = false;
    private boolean isLeftOrRightMoving = false;
    private boolean isUpDownMoving = false;
    private float lastX = 0;
    private float lastY = 0;
    private void setScrollListenner(final Context context) {
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        final float maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        ContentResolver resolver = context.getContentResolver();
        try {
            currentLight = Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        screenWidth = metrics.widthPixels;
        light_progress.setText(currentLight / 255 + "%");
        mView.setClickable(true);
        mView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //当手指按下的时候
                    startX = event.getX();
                    startY = event.getY();

                    isMoving = false;
                    isLeftOrRightMoving = false;
                    isUpDownMoving = false;
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    light_progress_container.setVisibility(View.GONE);
                    if(!isMoving){//如果没有移动，就是点击
                        mviewClickedHandle();
                    }
                    //快进快退后 如果播放中 就隐藏进度条
                    if(mVideoView.isPlaying()){
                        if(mControlPanel.getVisibility()==VISIBLE){
                            mControlPanel.setVisibility(View.GONE);
                        }
                    }
                    isMoving = false;
                }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    //当手指离开的时候
                    endX = event.getX();
                    endY = event.getY();


                    float dx = endX - startX;
                    float dy = endY - startY;

                    if(Math.abs(dx)>scaledTouchSlop || Math.abs(dy)>scaledTouchSlop){//滑动距离要大小最小距离
                        isMoving = true;
                        int orientation = getOrientation(dx, dy);
                        switch (orientation) {
                            case 't':
                                //action = "上";
                                TipTextView.setText("向上滑");
                                if(isLeftOrRightMoving){
                                    return false;
                                }
                                isUpDownMoving = true;
                                if(lastY>endY){//手指头 实际在往上去 亮度 音量 增大
                                    if (startX < screenWidth / 3.0) {//左边屏幕
                                        addScreenLightHandle();
                                    }else if(startX > (screenWidth / 3.0)*2) {//右边屏幕
                                        count++;
                                        if (count == 10) {//降低频率
                                            addVolumeHandle(maxVolume);
                                            count = 0;
                                        }
                                    }
                                }else {//手指头 实际在往下去 亮度 音量 减小
                                    if (startX < screenWidth / 3.0) {//左边屏幕
                                        reduceScreenLightHandle();
                                    }else if(startX > (screenWidth / 3.0)*2) {//右边屏幕
                                        count++;
                                        if (count == 10) {
                                            reduceVolumeHandle(maxVolume);
                                            count = 0;
                                        }
                                    }
                                }

                                break;
                            case 'b':
                                //action = "下";
                                TipTextView.setText("向下滑");
                                if(isLeftOrRightMoving){
                                    return false;
                                }
                                isUpDownMoving = true;
                                if(lastY>endY){//手指头 实际在往上去 亮度 音量 增大
                                    if (startX < screenWidth / 3.0) {//左边屏幕
                                        addScreenLightHandle();
                                    }else if(startX > (screenWidth / 3.0)*2) {//右边屏幕
                                        count++;
                                        if (count == 10) {//降低频率
                                            addVolumeHandle(maxVolume);
                                            count = 0;
                                        }
                                    }
                                }else {//手指头 实际在往下去 亮度 音量 减小
                                    if (startX < screenWidth / 3.0) {//左边屏幕
                                        reduceScreenLightHandle();
                                    }else if(startX > (screenWidth / 3.0)*2) {//右边屏幕
                                        count++;
                                        if (count == 10) {
                                            reduceVolumeHandle(maxVolume);
                                            count = 0;
                                        }
                                    }
                                }
                                break;
                            case 'l':
                                //action = "左";
                                TipTextView.setText("向左滑");
                                if(isUpDownMoving){
                                    return false;
                                }
                                isLeftOrRightMoving = true;
                                light_progress_container.setVisibility(View.GONE);
                                if(lastX>endX){//上一个位置应大于当前位置 ，手指头实际往左边去
                                    videoBackwordHandle();
                                }
                                else {//手指头实际往右边去
                                    videoFowardHandle();
                                }
                                //视频 快进快退时 显示进度条
                                if(mControlPanel.getVisibility()!=VISIBLE){
                                    mControlPanel.setVisibility(View.VISIBLE);
                                }
                                break;
                            case 'r':
                                //action = "右";
                                TipTextView.setText("向右边滑");
                                if(isUpDownMoving){
                                    return false;
                                }
                                isLeftOrRightMoving = true;
                                light_progress_container.setVisibility(View.GONE);
                                if(lastX>endX){//上一个位置应大于当前位置 ，手指头实际往左边去
                                    videoBackwordHandle();
                                }else {//手指头实际往右边去
                                    videoFowardHandle();
                                }
                                int vis = mControlPanel.getVisibility();
                                //视频 快进快退时 显示进度条
                                if(mControlPanel.getVisibility()!=VISIBLE){
                                    mControlPanel.setVisibility(View.VISIBLE);
                                }
                                break;

                        }
                        lastX = endX;
                        lastY = endY;
                    }
                }
                return false;
            }
        });
    }
    /**
     * 根据距离差判断 滑动方向
     * @param dx X轴的距离差
     * @param dy Y轴的距离差
     * @return 滑动的方向
     */
    private int getOrientation(float dx, float dy) {
        Log.e("Tag","========X轴距离差："+dx);
        Log.e("Tag","========Y轴距离差："+dy);
        if (Math.abs(dx)>Math.abs(dy)){
            //X轴移动
            return dx>0?'r':'l';
        }else{
            //Y轴移动
            return dy>0?'b':'t';
        }
    }
    // 亮度增加
    private void addScreenLightHandle(){
        currentLight = (int) (currentLight + (255 - currentLight) * (startY - endY) / metrics.heightPixels);
        WindowManager.LayoutParams params = ((Activity) context).getWindow().getAttributes();
        params.screenBrightness = currentLight / 255f;
        light_icon.setImageResource(R.mipmap.light_icon);
        light_progress_container.setVisibility(View.VISIBLE);
        if (currentLight > 249) {
            light_progress.setText(100 + "%");
        } else {
            float cl = currentLight;
            if(cl<0){
                cl = 0;
            }
            light_progress.setText((int) (cl * 100.0 / 255.0) + "%");
        }
        ((Activity) context).getWindow().setAttributes(params);
    }

    // 亮度减小
    private void reduceScreenLightHandle(){
        currentLight = (int) (currentLight - currentLight * (endY - startY) / metrics.heightPixels);
        WindowManager.LayoutParams params = ((Activity) context).getWindow().getAttributes();
        params.screenBrightness = currentLight / 255f;

        light_icon.setImageResource(R.mipmap.light_icon);
        light_progress_container.setVisibility(View.VISIBLE);
        if (currentLight > 249) {
            light_progress.setText(100 + "%");
        } else {
            float cl = currentLight;
            if(cl<0){
                cl = 0;
            }
            light_progress.setText((int) (cl * 100.0 / 255.0) + "%");
        }
        ((Activity) context).getWindow().setAttributes(params);
    }

    //音量增加
    private void addVolumeHandle(float maxVolume){
        mAudioManager.adjustStreamVolume(
                AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
        light_icon.setImageResource(R.mipmap.volume_icon);
        light_progress_container.setVisibility(View.VISIBLE);
        float progress = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        light_progress.setText((int) (progress / maxVolume * 100.0) + "%");
    }

    // 音量减小
    private void reduceVolumeHandle(float maxVolume){
        mAudioManager.adjustStreamVolume(
                AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);//| AudioManager.FLAG_SHOW_UI
        light_icon.setImageResource(R.mipmap.volume_icon);
        light_progress_container.setVisibility(View.VISIBLE);
        float progress = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        light_progress.setText((int) (progress / maxVolume * 100.0) + "%");
    }

    /**
     * 控制静音
     * */
    private boolean isNoVolue = false;//是否静音
    //设置静音 或恢复静音 默认是有声音的
    public void noVolume(){
        if(mediaPlayer != null && !isNoVolue){
            mediaPlayer.setVolume(0,0);
            isNoVolue = true;
        }else {
            mediaPlayer.setVolume(1,1);
            isNoVolue = false;
        }
    }
    //获取是否静音
    public boolean isNoVolume(){
        return isNoVolue;
    }




    private boolean isUse = false;
    //视频快退处理
    private void videoBackwordHandle(){
        int totalS = mVideoDuration;
        int current = mPlayProgressBar.getProgress();
        current = current- (int)(mVideoDuration*0.02);
        if(current>0){
            isUse =  true;
            mPlayProgressBar.setProgress(current);
        }

    }
    //视频快退进理
    private void videoFowardHandle(){
        int totalS = mVideoDuration;
        int current = mPlayProgressBar.getProgress();
        current = current+ (int)(mVideoDuration*0.02);
        if(current<totalS){
            isUse =  true;
            mPlayProgressBar.setProgress(current);
        }
    }


    //初始化视频，设置视频时间和进度条最大值
    private void initVideo() {
        //初始化时间和进度条
        mVideoDuration = mVideoView.getDuration();//毫秒数
        int seconds = mVideoDuration / 1000;
        time_left.setText("00:00");
        time_right.setText("" +
                ((seconds / 60 > 9) ? (seconds / 60) : ("0" + seconds / 60)) + ":" +
                ((seconds % 60 > 9) ? (seconds % 60) : ("0" + seconds % 60)));
        mPlayProgressBar.setMax(mVideoDuration);
        mPlayProgressBar.setProgress(0);
        //更新进度条和时间任务
        mUpdateTask = new Runnable() {
            @Override
            public void run() {
                while (!stopThread) {
                    mCurrentProgress = mVideoView.getCurrentPosition();
                    handler.sendEmptyMessage(UPDATE_PROGRESS);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        mBigPlayBtn.setOnClickListener(this);
        mBigPauseBtn.setOnClickListener(this);
        mPlayBtn.setOnClickListener(this);
        mFullScreenBtn.setOnClickListener(this);


        mControlPanel.setVisibility(VISIBLE);

        //进度条进度改变监听器
        mPlayProgressBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.sendEmptyMessageDelayed(EXIT_CONTROL_PANEL, 3000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(EXIT_CONTROL_PANEL);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (fromUser || isUse) {
                    isUse = false;
                    mVideoView.seekTo(progress);//设置视频
                    setPlayTime(progress);//设置时间
                }
            }
        });
    }

    //整个被点击时处理
    private void mviewClickedHandle(){

        //播放/暂停按钮
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
            mPlayBtn.setSelected(false);
            mBigPlayBtn.setVisibility(VISIBLE);
            mBigPauseBtn.setVisibility(INVISIBLE);

            mControlPanel.setVisibility(View.VISIBLE);

        } else {
            playFromPauseState();
            mControlPanel.setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.big_play_button) {
            //大的播放按钮
            if (!mVideoView.isPlaying()) {
                startPlay();
                mControlPanel.setVisibility(View.GONE);
            }
        }else if(v.getId() == R.id.big_pase_button){
            if(mVideoView.isPlaying()){
                mVideoView.pause();
                mPlayBtn.setSelected(false);
                mBigPlayBtn.setVisibility(VISIBLE);
            }
        } else if (v.getId() == R.id.play_button) {
            //播放/暂停按钮
            if (mVideoView.isPlaying()) {
                mVideoView.pause();
                mPlayBtn.setSelected(false);
                mBigPlayBtn.setVisibility(VISIBLE);
                mBigPauseBtn.setVisibility(INVISIBLE);
                mControlPanel.setVisibility(View.VISIBLE);
            } else {
                playFromPauseState();
                mControlPanel.setVisibility(View.GONE);
            }
            //sendHideControlPanelMessage();
        } else if (v.getId() == R.id.full_screen_button) {
            //全屏
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                screenWidth = metrics.heightPixels;
                setIsFullScreen(true);
            } else {
                screenWidth = metrics.widthPixels;
                setIsFullScreen(false);
            }
            //sendHideControlPanelMessage();
        }
    }

    private void startPlay() {
        mBigPlayBtn.setVisibility(View.GONE);
        mVideoView.setBackground(null);
        play_bg.setVisibility(View.GONE);
        mVideoView.start();
        mPlayBtn.setSelected(true);
        mBigPlayBtn.setVisibility(GONE);
        //开始更新进度线程
        mUpdateThread = new Thread(mUpdateTask);
        stopThread = false;
        mUpdateThread.start();
    }

    public void startPlayNow(){
        if(!mVideoView.isPlaying()){
            startPlay();
            mControlPanel.setVisibility(View.GONE);
        }
    }

    //从暂停状态播放
    private void playFromPauseState() {
        if (mUpdateThread == null || !mUpdateThread.isAlive()) {
            //开始更新进度线程
            mUpdateThread = new Thread(mUpdateTask);
            stopThread = false;
            mUpdateThread.start();
        }
        mVideoView.start();
        play_bg.setVisibility(View.GONE);
        mPlayBtn.setSelected(true);
        mBigPlayBtn.setVisibility(GONE);
        mBigPauseBtn.setVisibility(INVISIBLE);
    }



    //设置当前时间
    private void setPlayTime(int millisSecond) {
        int currentSecond = millisSecond / 1000;
        String currentTime = ((currentSecond / 60 > 9) ? (currentSecond / 60 + "") : ("0" + currentSecond / 60)) + ":" +
                ((currentSecond % 60 > 9) ? (currentSecond % 60 + "") : ("0" + currentSecond % 60));
        time_left.setText(currentTime);
    }

    //设置控件的宽高
//    public void setSize() {
//        ViewGroup.LayoutParams lp = this.getLayoutParams();
//        if (mIsFullScreen) {
//            lp.width = screenSize.y;
//            lp.height = screenSize.x;
//        } else {
//            lp.width = mWidth;
//            lp.height = mHeigth;
//        }
//        this.setLayoutParams(lp);
//    }

    //两秒后隐藏控制面板
    private void sendHideControlPanelMessage() {
        handler.removeMessages(EXIT_CONTROL_PANEL);
        handler.sendEmptyMessageDelayed(EXIT_CONTROL_PANEL, 3000);
    }

    //设置视频路径
    public void setVideoUri(Uri uri) {
        this.mVideoUri = uri;
        mVideoView.setVideoURI(mVideoUri);
    }

    //获取视频路径
    public Uri getVideoUri() {
        return mVideoUri;
    }

    //设置视频初始画面
    public void setInitPicture(Drawable d) {
        mVideoView.setBackground(d);
    }

    //挂起视频
    public void suspend() {
        if (mVideoView != null) {
//            mVideoView.suspend();
            mVideoView.destroyDrawingCache();
        }
    }


    //设置视频进度
    public void setVideoProgress(int millisSecond, boolean isPlaying) {
        mVideoView.setBackground(null);
        mBigPlayBtn.setVisibility(View.GONE);
        mPlayProgressBar.setProgress(millisSecond);
        setPlayTime(millisSecond);
        if (mUpdateThread == null || !mUpdateThread.isAlive()) {
            mUpdateThread = new Thread(mUpdateTask);
            stopThread = false;
            mUpdateThread.start();
        }
        mVideoView.seekTo(millisSecond);
        if (isPlaying) {
            mVideoView.start();
            mPlayBtn.setSelected(true);
            mBigPlayBtn.setVisibility(GONE);
        } else {
            mVideoView.pause();
            mPlayBtn.setSelected(true);
            mBigPlayBtn.setVisibility(VISIBLE);
        }
    }

    //获取视频进度
    public int getVideoProgress() {
        return mVideoView.getCurrentPosition();
    }

    //判断视频是否正在播放
    public boolean isPlaying() {
        return mVideoView.isPlaying();
    }

    //判断是否为全屏状态
    public boolean isFullScreen() {
        return mIsFullScreen;
    }


    //设置是否全屏
    public void setIsFullScreen(boolean mIsFullScreen) {
        this.mIsFullScreen = mIsFullScreen;
        Tool.setScreenLandscape(context);
//        if (context instanceof Page1VideoPlayAc) {
//            ((Page1VideoPlayAc) context).video_full(mIsFullScreen);
//        } else if (context instanceof Page2VideoPlayAc) {
//            ((Page2VideoPlayAc) context).video_full(mIsFullScreen);
//        } else
        if (context instanceof DetailActivity) {
            ((DetailActivity) context).video_full(mIsFullScreen);
        }else if(context instanceof MoreVideoPlayActivity){
            ((MoreVideoPlayActivity) context).video_full(mIsFullScreen);
        }
//        setSize();
    }

}