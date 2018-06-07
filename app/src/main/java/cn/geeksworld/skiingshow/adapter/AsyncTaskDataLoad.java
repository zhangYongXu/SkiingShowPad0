package cn.geeksworld.skiingshow.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.widget.ImageView;

import cn.geeksworld.skiingshow.Tools.Tool;
import cn.geeksworld.skiingshow.model.VideoModel;

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

            }


        } catch (IllegalArgumentException e) {
            //本地视频文件不存在

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
