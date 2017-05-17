package com.example.mierul.myapplication21.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mierul.myapplication21.ProductPictureEnum;

/**
 * Created by Hexa-Amierul.Japri on 17/5/2017.
 */

public class ProductPicturePagerAdapter extends PagerAdapter {
    private Context context;

    public ProductPicturePagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return ProductPictureEnum.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ProductPictureEnum productPictureEnum = ProductPictureEnum.values()[position];
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(productPictureEnum.getLayoutResId(),container,false);
        container.addView(layout);

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        ProductPictureEnum productPictureEnum = ProductPictureEnum.values()[position];
        return context.getString(productPictureEnum.getTitleResId());
    }
}
