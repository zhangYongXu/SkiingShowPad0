package cn.geeksworld.skiingshow.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.geeksworld.skiingshow.R;

import cn.geeksworld.skiingshow.model.SkiingModel;
import cn.geeksworld.skiingshow.views.MyRollPagerView;

/**
 * Created by xhs on 2018/3/14.
 */

public class MyListAdater extends BaseAdapter {
    private List<SkiingModel> itemList = new ArrayList<SkiingModel>();
    private Context context;
    private TextView titleTextView;
    private ImageView showImageView;

    public MyListAdater(Context context,List<SkiingModel> itemList) {
        super();
        this.itemList = itemList;
        this.context = context;
    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view;

        if (convertView==null) {
            //因为getView()返回的对象，adapter会自动赋给ListView
            view = inflater.inflate(R.layout.list_item, null);
        }else{
            view=convertView;
        }


        titleTextView = (TextView) view.findViewById(R.id.itemTitleTextView);//找到Textviewname
        titleTextView.setText(itemList.get(i).getName());//设置参数

        showImageView = (ImageView)view.findViewById(R.id.itemShowImageView);

//        try {
//            String namePath = itemList.get(i).getImagePath();
//            // get input stream
//            InputStream ims = viewGroup.getContext().getAssets().open(namePath);
//            // load image as Drawable
//            Drawable d = Drawable.createFromStream(ims, null);
//            // set image to ImageView
//            showImageView.setImageDrawable(d);
//        }
//        catch(IOException ex) {
//
//        }

        return view;

    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }
}
