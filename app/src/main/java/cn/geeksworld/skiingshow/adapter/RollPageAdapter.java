package cn.geeksworld.skiingshow.adapter;

import android.view.View;
import android.view.ViewGroup;


import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import cn.geeksworld.skiingshow.R;
import android.widget.ImageView;
/**
 * Created by xhs on 2018/3/13.
 */

public class RollPageAdapter extends StaticPagerAdapter {

    private int[] res={
            R.mipmap.b1,
            R.mipmap.b2,
            R.mipmap.b3,
            R.mipmap.b4
    };

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView imageView=new ImageView(container.getContext());
        imageView.setImageResource(res[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return imageView;
    }


    @Override
    public int getCount() {
        return res.length;
    }
}
