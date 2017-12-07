package com.merteroglu.unsplashandgooglemaps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.merteroglu.unsplashandgooglemaps.Models.Images;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mert on 4.12.2017.
 */

public class ViewPageAdapter extends PagerAdapter {
    Activity activity;
    List<Images> images;
    LayoutInflater layoutInflater;

    public ViewPageAdapter(Activity activity, List<Images> images) {
        this.activity = activity;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.viewpager_item,container,false);

        final ImageView image;
        image = itemView.findViewById(R.id.imgAna);
        DisplayMetrics dis = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dis);
        int height = dis.heightPixels;
        int width = dis.widthPixels;
        image.setMinimumHeight(height);
        image.setMinimumWidth(width);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity,ShowImageFull.class);
                i.putExtra("link",images.get(position).getURL());
                i.putExtra("color",images.get(position).getColor());
                activity.startActivity(i);
            }
        });

        try{
            Picasso.with(activity.getApplicationContext())
                    .load(images.get(position).getURL())
                    .into(image);
        }catch (Exception e){

        }

        container.addView(itemView);
        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}
