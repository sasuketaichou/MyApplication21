package com.example.mierul.myapplication21.Event;


/**
 * Created by Hexa-Amierul.Japri on 5/5/2017.
 */

public class ProfileAdapterEvent {

    private int position;

    public ProfileAdapterEvent(int position){
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


}
