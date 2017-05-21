package com.example.mierul.myapplication21.Model;

/**
 * Created by Hexa-Amierul.Japri on 18/5/2017.
 */

public class ProductUrlPictureModel {
    public String key;
    public String image_1;
    public String image_2;
    public String image_3;
    public String image_4;

    public ProductUrlPictureModel(){
    }

    public String[] toArray(){
        return new String[]{image_1,image_2,image_3,image_4};
    }

    public String getKey(){
        return key;
    }

    public void addKey(String key) {
        this.key = key;
    }
}
