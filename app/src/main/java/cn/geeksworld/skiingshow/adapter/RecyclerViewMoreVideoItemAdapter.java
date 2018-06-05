package cn.geeksworld.skiingshow.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import cn.geeksworld.skiingshow.R;
import cn.geeksworld.skiingshow.Tools.ShareKey;
import cn.geeksworld.skiingshow.model.SkiingModel;
import cn.geeksworld.skiingshow.model.VideoModel;

/**
 * Created by xhs on 2018/3/30.
 */



public class RecyclerViewMoreVideoItemAdapter extends RecyclerView.Adapter {


    List<File> list;//存放数据
    Context context;

    private final DisplayMetrics metrics;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    private RecyclerViewMoreVideoItemAdapter.OnItemClickListener mItemClickListener;
    public void setItemClickListener(RecyclerViewMoreVideoItemAdapter.OnItemClickListener itemClickListener){
        mItemClickListener = itemClickListener;
    }

    public RecyclerViewMoreVideoItemAdapter(Context _context, List<File> _list) {
        this.list = _list;
        this.context = _context;

        metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = new MyFragViewHolder(LayoutInflater.from(context).inflate(R.layout.list_more_video_item, parent, false));
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


        File mp4File = list.get(position);
        //设置textView显示内容为list里的对应项

        String name = getFileName(mp4File.getAbsolutePath());

       fragHolder.titleTextView.setText(name);
        //显示图片
        showCurrentFaceImageFromVideo(fragHolder.showImageView,mp4File);
    }

    public String getFileName(String pathandname){
        int start=pathandname.lastIndexOf("/");
        int end=pathandname.lastIndexOf(".");
        if (start!=-1 && end!=-1) {
            return pathandname.substring(start+1, end);
        }
        else {
            return null;
        }
    }

    private void showCurrentFaceImageFromVideo(ImageView imageView,File mp4File){
        String videoPath = mp4File.getAbsolutePath();
        Bitmap bitmap = getVideoThumbnail(videoPath);
        imageView.setImageBitmap(bitmap);

        //测试
        if(ShareKey.TestImageAndVideo){
            imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.test_video_image));
        }
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

    //要显示的子项数量
    @Override
    public int getItemCount() {
        return list.size();
    }

    //这里定义的是子项的类，不要在这里直接对获取对象进行操作
    public class MyFragViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        ImageView showImageView;


        public MyFragViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            showImageView = itemView.findViewById(R.id.videoShowImageView);

        }
    }


    /*之下的方法都是为了方便操作，并不是必须的*/

    //在指定位置插入，原位置的向后移动一格
    public boolean addItem(int position, File model) {
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
