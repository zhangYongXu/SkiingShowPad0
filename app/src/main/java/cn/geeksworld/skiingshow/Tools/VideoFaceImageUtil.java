package cn.geeksworld.skiingshow.Tools;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import java.util.List;

import cn.geeksworld.skiingshow.model.MainVideoModel;
import cn.geeksworld.skiingshow.model.VideoModel;
import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * Created by xhs on 2018/6/7.
 */

public class VideoFaceImageUtil {
    public static void getVideoThumbnailFFmpeg(final Activity activity, final String videoPath, final VideoModel videoModel, final ImageView imageView) {
        if(null != videoModel.bitmap){
            imageView.setImageBitmap(videoModel.bitmap);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FFmpegMediaMetadataRetriever mm = new FFmpegMediaMetadataRetriever();
                    mm.setDataSource(videoPath);
                    Bitmap bitmap = mm.getFrameAtTime();
                    videoModel.bitmap = bitmap;

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(null != imageView){
                                imageView.setImageBitmap(videoModel.bitmap);
                            }
                        }
                    });
                    Log.i("ee","");
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                    Log.i("IllegalArgException",e.toString());
                }catch (Exception e){
                    e.printStackTrace();
                    Log.i("Exception",e.toString());
                }
            }
        }).start();
    }

    public static void getVideoThumbnailFFmpeg(final Activity activity, final String videoPath, final MainVideoModel mainVideoModel, final ImageView imageView) {
        if(null != mainVideoModel.bitmap){
            imageView.setImageBitmap(mainVideoModel.bitmap);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FFmpegMediaMetadataRetriever mm = new FFmpegMediaMetadataRetriever();
                    mm.setDataSource(videoPath);
                    Bitmap bitmap = mm.getFrameAtTime();
                    mainVideoModel.bitmap = bitmap;

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(null != imageView){
                                imageView.setImageBitmap(mainVideoModel.bitmap);
                            }
                        }
                    });
                    Log.i("ee","");
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                    Log.i("IllegalArgException",e.toString());
                }catch (Exception e){
                    e.printStackTrace();
                    Log.i("Exception",e.toString());
                }
            }
        }).start();
    }


    public static void getVideoThumbnailFFmpeg(final Activity activity, final String videoPath, final MainVideoModel mainVideoModel, final List<ImageView> imageViewList) {
        if(null != mainVideoModel.bitmap){
            for(ImageView imageView :imageViewList){
                imageView.setImageBitmap(mainVideoModel.bitmap);
            }
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FFmpegMediaMetadataRetriever mm = new FFmpegMediaMetadataRetriever();
                    mm.setDataSource(videoPath);
                    Bitmap bitmap = mm.getFrameAtTime();
                    mainVideoModel.bitmap = bitmap;

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for(ImageView imageView :imageViewList){
                                imageView.setImageBitmap(mainVideoModel.bitmap);
                            }
                        }
                    });
                    Log.i("ee","");
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                    Log.i("IllegalArgException",e.toString());
                }catch (Exception e){
                    e.printStackTrace();
                    Log.i("Exception",e.toString());
                }
            }
        }).start();
    }
}
