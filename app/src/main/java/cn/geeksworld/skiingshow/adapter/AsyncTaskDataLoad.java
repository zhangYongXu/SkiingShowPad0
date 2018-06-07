package cn.geeksworld.skiingshow.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import cn.geeksworld.skiingshow.Tools.Tool;
import cn.geeksworld.skiingshow.model.VideoModel;
import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * Created by xhs on 2018/6/6.
 */

public class AsyncTaskDataLoad extends AsyncTask {
    private VideoModel videoModel;
    private ImageView imageView;
    private String videoFullPath;
    private Bitmap bitmap;


    public AsyncTaskDataLoad(VideoModel inVideoModel,ImageView inImageView,String inVideoFullPath){
        videoModel = inVideoModel;
        imageView = inImageView;
        videoFullPath = inVideoFullPath;
    }
    @Override
    protected Object doInBackground(Object[] objects) {
        /*
        MediaMetadataRetriever retriever = null;
        try {
            if (bitmap == null) {
                retriever = new MediaMetadataRetriever();
                retriever.setDataSource(videoFullPath);

                bitmap = retriever.getFrameAtTime(0,MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                //更新图片
                publishProgress(1);


                //保存获取的视频截图到缓存
//                String path = FileUtils.saveToCacheJPGImage(bitmap, System.currentTimeMillis() + "");
//                mChallengeListData.thumbNailPath = path;
//
//                VideoDbHelper.update(mChallengeListData.uuid, mChallengeListData.thumbNailPath, mChallengeListData.duration + "");

            } else {
                Log.i("","");
            }

        } catch (IllegalArgumentException e) {
            //本地视频文件不存在
            Log.i("","");
        } finally {
            try {
                if (retriever != null) {
                    retriever.release();
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
       return videoModel;
        */

        FFmpegMediaMetadataRetriever mm = new FFmpegMediaMetadataRetriever();
        try{
            //获取视频文件数据
            mm.setDataSource(videoFullPath);
            //获取文件缩略图
            Bitmap bitmap=mm.getFrameAtTime();
            //更新图片
            publishProgress(1);
        }catch (Exception e){
            Log.i("","");
        }finally {
            mm.release();
        }
        return videoModel;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        imageView.setImageBitmap(bitmap);
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}
