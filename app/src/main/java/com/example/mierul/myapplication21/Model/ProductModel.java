package com.example.mierul.myapplication21.Model;


/**
 * Created by mierul on 3/16/2017.
 */

public class ProductModel {
    private String name;
    private String photo;

    public ProductModel(String name, String photo) {
        this.name = name;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
