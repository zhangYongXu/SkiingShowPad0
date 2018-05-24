package cn.geeksworld.skiingshow.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import cn.geeksworld.skiingshow.Tools.ShareKey;
import cn.geeksworld.skiingshow.model.SkiingModel;
import cn.geeksworld.skiingshow.model.VideoModel;
import cn.geeksworld.skiingshow.R;

/**
 * Created by xhs on 2018/3/19.
 */

public class MyGridViewAdapter extends BaseAdapter {

    private ImageView showImageView;
    private TextView titleTextView;
    private SkiingModel skiingModel;
    private String localCommonDirPath;

    Context context;
    List<VideoModel> list;

    public MyGridViewAdapter(Context _context, List<VideoModel> _list,SkiingModel _skiingModel) {
        this.list = _list;
        this.context = _context;
        this.skiingModel = _skiingModel;
        localCommonDirPath = skiingModel.getLocalCommonDirPath() + "videoDetailJson/item"+skiingModel.getOrderNum()+"/";

    }

    public void setList(List<VideoModel> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View convertView;

        if (view == null) {
            //因为getView()返回的对象，adapter会自动赋给ListView
            convertView = inflater.inflate(R.layout.detail_list, null);
        }else{
            convertView=view;
        }

        //LayoutInflater layoutInflater = LayoutInflater.from(context);
        //convertView = layoutInflater.inflate(R.layout.detail_list, null);


        showImageView = (ImageView)convertView.findViewById(R.id.detailItemImageView);
        titleTextView = (TextView)convertView.findViewById(R.id.detailItemTextView);
        titleTextView.setText(list.get(i).getVideoTitle());
        /*
        try {
            String namePath = localCommonDirPath + list.get(i).getLocalCommonFaceImagePath();
            // get input stream
            InputStream ims = viewGroup.getContext().getAssets().open(namePath);
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            showImageView.setImageDrawable(d);
        }
        catch(IOException ex) {

        }
        */


        /*
        Bitmap bitmap = null;
        try {
            String namePath = localCommonDirPath + list.get(i).getLocalCommonFaceImagePath();
            // get input stream
            InputStream is = viewGroup.getContext().getAssets().open(namePath);

            BitmapFactory.Options options = new BitmapFactory.Options();
            //更改颜色方案以减少内存使用(rgb_565),
            options.inPreferredConfig = Bitmap.Config.RGB_565;

            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is,null,options);
            //关闭数据流

            showImageView.setImageBitmap(bitmap);
            is.close();

        }catch(IOException ex) {

        }
        */
        showCurrentFaceImageFromVideo(showImageView,list.get(i));

        /*
        Uri videoUri = uriWithFileName(getFileNameNoEx(list.get(i).getVideoName()));
        Drawable drawable = getThumbnail(videoUri);
        */

        return convertView;
    }


    private void showCurrentFaceImageFromVideo(ImageView imageView,VideoModel videoModel){
        String videoPath = getCurrentVideoPath(videoModel);
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
    //获取当前视频路径
    private String getCurrentVideoPath(VideoModel videoModel){
        String videoPath = ShareKey.getSDDir() + skiingModel.getLocalCommonSDDirPath() + "第" + (skiingModel.getOrderNum()+1) + "课/" + videoModel.getLocalCommonVideoSDPath();
        return videoPath;
    }
    /*
    * Java文件操作 获取不带扩展名的文件名
    * */
    /*
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }
    private Uri uriWithFileName(String fileNmae){
        String fileNameNoEx =  getFileNameNoEx(fileNmae);
        Resources res = context.getResources();
        final String packageName = context.getPackageName();
        int musicResId = res.getIdentifier(fileNameNoEx, "raw", packageName);

        String videoUri = "android.resource://" + packageName + "/" +musicResId;
        return Uri.parse(videoUri);
    }
    private Drawable getThumbnail(Uri aVideoUri)
    {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context, aVideoUri);
        Bitmap bitmap = retriever
                .getFrameAtTime(1*1000*1000, MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);
        Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);

        return drawable;
    }
    */
}
