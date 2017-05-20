package com.example.mierul.myapplication21.Model;

/**
 * Created by mierul on 5/20/2017.
 */

public class ProductProfileModel {
    public String cost;
    public String name;
    public String total_pieces;
    public String type;

    public ProductProfileModel(){
    }

    public String[] toArray(){
        return new String[]{name,cost,total_pieces,type};
    }
}
