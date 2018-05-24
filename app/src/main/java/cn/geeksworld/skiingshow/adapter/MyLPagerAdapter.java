package cn.geeksworld.skiingshow.adapter;

import android.app.Activity;
import android.content.Context;
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

import java.util.ArrayList;

/**
 * Created by xhs on 2017/10/24.
 * RollPagerView自动轮播适配器
 */

public class MyLPagerAdapter extends MyLoopPagerAdapter {

    private final DisplayMetrics metrics;
    private ArrayList<String> datas_imgs;
    private Context context;

    public MyLPagerAdapter(MyRollPagerView viewPager, Context context, ArrayList<String> datas_imgs) {
        super(viewPager);
        this.context = context;
        this.datas_imgs = datas_imgs;
        metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
    }

    @Override
    public View getView(ViewGroup container, int position) {
        position %= datas_imgs.size();
        if (position < 0) {
            position = datas_imgs.size() + position;
        }
        ImageView view = new ImageView(context);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        String imgUrl = datas_imgs.get(position);
        if (Tool.isNull(imgUrl))
            view.setImageResource(R.mipmap.test_bana);
        else if (imgUrl instanceof String)
            Glide.with(context).load(imgUrl).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).override(metrics.widthPixels, metrics.widthPixels * 3 / 4).crossFade().thumbnail(0.6f).into(view);
        else
            view.setImageResource(Integer.parseInt(imgUrl));
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp = view.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(view);
        }
        return view;
    }

    @Override
    public int getRealCount() {
        return datas_imgs.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}
