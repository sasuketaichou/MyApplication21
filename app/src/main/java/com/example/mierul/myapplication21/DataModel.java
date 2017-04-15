package com.example.mierul.myapplication21;

import android.graphics.Bitmap;

/**
 * Created by mierul on 3/16/2017.
 */

public class DataModel {
    private String name;
    private Bitmap photo;

    public DataModel(String name, Bitmap photo) {
        this.name = name;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
