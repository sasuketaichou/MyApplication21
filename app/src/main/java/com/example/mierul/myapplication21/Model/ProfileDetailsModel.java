package com.example.mierul.myapplication21.Model;

/**
 * Created by Hexa-Amierul.Japri on 5/5/2017.
 */

public class ProfileDetailsModel {
    public String name;
    public String address;
    public String contact;

    public ProfileDetailsModel(){
    }

    public ProfileDetailsModel(String name,String address,String contact){
        this.name = name;
        this.address = address;
        this.contact = contact;
    }
}
