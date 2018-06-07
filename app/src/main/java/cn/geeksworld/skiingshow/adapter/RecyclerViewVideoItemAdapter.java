package cn.geeksworld.skiingshow.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.geeksworld.skiingshow.R;
import cn.geeksworld.skiingshow.Tools.ShareKey;
import cn.geeksworld.skiingshow.Tools.VideoFaceImageUtil;
import cn.geeksworld.skiingshow.model.SkiingModel;
import cn.geeksworld.skiingshow.model.VideoModel;
import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * Created by xhs on 2018/3/30.
 */



public class RecyclerViewVideoItemAdapter extends RecyclerView.Adapter {


    private SkiingModel skiingModel;

    List<VideoModel> list;//存放数据
    Context context;
    Activity activity;

    private final DisplayMetrics metrics;


    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    private RecyclerViewVideoItemAdapter.OnItemClickListener mItemClickListener;
    public void setItemClickListener(RecyclerViewVideoItemAdapter.OnItemClickListener itemClickListener){
        mItemClickListener = itemClickListener;
    }

    public RecyclerViewVideoItemAdapter(Context _context,Activity _activity,List<VideoModel> _list,SkiingModel _skiingModel) {

        this.list = _list;
        this.context = _context;
        this.activity = _activity;
        this.skiingModel = _skiingModel;

        metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = new MyFragViewHolder(LayoutInflater.from(context).inflate(R.layout.detail_list, parent, false));
        return holder;
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    //在这里可以获得每个子项里面的控件的实例，比如这里的TextView,子项本身的实例是itemView，
// 在这里对获取对象进行操作
    //holder.itemView是子项视图的实例，holder.textView是子项内控件的实例
    //position是点击位置

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //子项的点击事件监听
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "点击子项"+ position, Toast.LENGTH_SHORT).show();
                if (mItemClickListener!=null) {
                    mItemClickListener.onItemClick(position);
                }

            }
        });

        MyFragViewHolder fragHolder = (MyFragViewHolder) holder;

        VideoModel videoModel = (VideoModel) list.get(position);
        //设置textView显示内容为list里的对应项

        fragHolder.detailItemTextView.setText(videoModel.getVideoTitle());
        //显示图片
        showCurrentFaceImageFromVideo(fragHolder.detailItemImageView,videoModel);
    }

    private void showCurrentFaceImageFromVideo(final ImageView imageView,final VideoModel videoModel){
        if(null != videoModel.bitmap){
            imageView.setImageBitmap(videoModel.bitmap);
            return;
        }

        String videoPath = getCurrentVideoPath(videoModel);
        getVideoThumbnail(videoPath,videoModel,imageView);

        //测试
        if(ShareKey.TestImageAndVideo){
            imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.test_video_image));
        }
    }
    //获取当前视频路径
    private String getCurrentVideoPath(VideoModel videoModel){
        String videoPath = ShareKey.getSDDir() + skiingModel.getLocalCommonSDDirPath() + "第" + (skiingModel.getOrderNum()+1) + "课/" + videoModel.getLocalCommonVideoSDPath();
        return videoPath;
    }
    private Uri uriWithFilePath(String filePath){
        //String path = "/storage/extsd/video/er_tong_dan_ban_jiao_cheng_item1_cuo_wu_4.mp4";
        String path = filePath;
        File file = new File(path);
        if(file.exists()){
            Uri uri = Uri.parse(path);
            return uri;
        }else {
            Toast.makeText(context,"视频文件不存在:"+path,Toast.LENGTH_LONG).show();
            String videoUri = filePath;
            return Uri.parse(videoUri);
        }
    }
    public void getVideoThumbnail(final String videoPath, final VideoModel videoModel,final ImageView imageView) {
        VideoFaceImageUtil.getVideoThumbnailFFmpeg(activity,videoPath,videoModel,imageView);
    }

    public Bitmap getVideoThumbnail(final String videoPath) {
        try {
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            Uri uri = uriWithFilePath(videoPath);
            media.setDataSource(context,uri);
            Bitmap bitmap = media.getFrameAtTime(0,MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            Log.i("eess","");
            return bitmap;
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            Log.i("IllegalArgException",e.toString());
        }catch (Exception e){
            e.printStackTrace();
            Log.i("Exception",e.toString());
        }
        return null;
    }

    //要显示的子项数量
    @Override
    public int getItemCount() {
        return list.size();
    }

    //这里定义的是子项的类，不要在这里直接对获取对象进行操作
    public class MyFragViewHolder extends RecyclerView.ViewHolder {

        TextView detailItemTextView;
        ImageView detailItemImageView;


        public MyFragViewHolder(View itemView) {
            super(itemView);
            detailItemTextView = itemView.findViewById(R.id.detailItemTextView);
            detailItemImageView = itemView.findViewById(R.id.detailItemImageView);

        }
    }


    /*之下的方法都是为了方便操作，并不是必须的*/

    //在指定位置插入，原位置的向后移动一格
    public boolean addItem(int position, VideoModel model) {
        if (position < list.size() && position >= 0) {
            list.add(position, model);
            notifyItemInserted(position);
            return true;
        }
        return false;
    }

    //去除指定位置的子项
    public boolean removeItem(int position) {
        if (position < list.size() && position >= 0) {
            list.remove(position);
            notifyItemRemoved(position);
            return true;
        }
        return false;
    }

    //清空显示数据
    public void clearAll() {
        list.clear();
        notifyDataSetChanged();
    }
}
