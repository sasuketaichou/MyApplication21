package com.example.mierul.myapplication21;

/**
 * Created by Hexa-Amierul.Japri on 17/5/2017.
 */

public enum ProductPictureEnum {

    FIRST(R.string.first,R.layout.product_picture),
    SECOND(R.string.second,R.layout.product_picture),
    THIRD(R.string.third,R.layout.product_picture);

    private int mLayoutResId;
    private int mTitleResId;

    ProductPictureEnum(int titleResId,int layoutResId) {
        mLayoutResId = layoutResId;
        mTitleResId = titleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

    public int getTitleResId(){
        return mTitleResId;
    }
}
