package cn.geeksworld.skiingshow.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import cn.geeksworld.skiingshow.R;
import cn.geeksworld.skiingshow.Tools.Tool;
import cn.geeksworld.skiingshow.views.MyRollPagerView;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by xhs on 2018/3/13.
 */

public class MyLLocalPagerAdapter extends MyLoopPagerAdapter {
    private final DisplayMetrics metrics;
    private int[] datas_imgs;
    private Context context;

    public MyLLocalPagerAdapter(MyRollPagerView viewPager, Context context, int[] datas_imgs) {
        super(viewPager);
        this.context = context;
        this.datas_imgs = datas_imgs;
        metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
    }

    @Override
    public View getView(ViewGroup container, int position) {
        position %= datas_imgs.length;
        if (position < 0) {
            position = datas_imgs.length + position;
        }
        ImageView view = new ImageView(context);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        int imgUrlid = datas_imgs[position];
        view.setImageResource(imgUrlid);
        //这样加载省内存
        //Bitmap bitmap = readBitMap(context,imgUrlid);
        //view.setImageBitmap(bitmap);
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp = view.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(view);
        }
        return view;
    }

//    public static Bitmap readBitMap(Context context, int resId){
//        BitmapFactory.Options opt = new BitmapFactory.Options();
//        opt.inPreferredConfig = Bitmap.Config.RGB_565;
//        opt.inPurgeable = true;
//        opt.inInputShareable = true;
//        //获取资源图片
//        InputStream is = context.getResources().openRawResource(resId);
//        return BitmapFactory.decodeStream(is,null,opt);
//    }

    @Override
    public int getRealCount() {
        return datas_imgs.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}
