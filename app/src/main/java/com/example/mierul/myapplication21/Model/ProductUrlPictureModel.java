package com.example.mierul.myapplication21.Model;

/**
 * Created by Hexa-Amierul.Japri on 18/5/2017.
 */

public class ProductUrlPictureModel {
    public String pushId1;
    public String pushId2;
    public String pushId3;
    public String pushId4;

    public ProductUrlPictureModel(){
    }

    public String[] toArray(){
        return new String[]{pushId1,pushId2,pushId3,pushId4};
    }

}
