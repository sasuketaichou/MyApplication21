package com.example.mierul.myapplication21.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mierul.myapplication21.R;

/**
 * Created by Hexa-Amierul.Japri on 17/5/2017.
 */

public class ProductPicturePagerAdapter extends PagerAdapter {
    private Context context;
    private String[] imageUrl;

    public ProductPicturePagerAdapter(Context context,String[] imageUrl) {
        this.context = context;
        this.imageUrl = imageUrl;

    }

    @Override
    public int getCount() {
        return imageUrl.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.product_picture,container,false);
        ImageView imageView = (ImageView)rootView.findViewById(R.id.product_pic_iv);

        Glide.with(context)
                .load(imageUrl[position])
                .into(imageView);

        container.addView(rootView);
        return rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
