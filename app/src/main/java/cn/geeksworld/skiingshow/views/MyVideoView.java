package cn.geeksworld.skiingshow.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by xhs on 2017/12/9.
 */

public class MyVideoView extends VideoView {

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(getWidth(), widthMeasureSpec);
        int height = getDefaultSize(getHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);

    }
}