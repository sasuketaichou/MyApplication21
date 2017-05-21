package com.example.mierul.myapplication21.Model;

/**
 * Created by mierul on 5/20/2017.
 */

public class ProductProfileModel {
    public String key;
    public long cost;
    public String name;
    public long total_pieces;
    public String type;

    public ProductProfileModel(){
    }

    public String[] toArray(){
        return new String[]{name,getCost(),getPieces(),type};
    }

    public String getCost(){
        return String.valueOf(cost);
    }

    public String getPieces(){
        return String.valueOf(total_pieces);
    }

    public String getKey(){
        return key;
    }

    public void addKey(String key) {
        this.key = key;
    }
}
