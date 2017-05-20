package com.example.mierul.myapplication21;

import java.util.List;

/**
 * Created by Hexa-Amierul.Japri on 19/5/2017.
 */

public class FirebaseListEvent {
    public List<Object> list;

    public FirebaseListEvent(List<Object> list){
        this.list = list;
    }

    public List<?> getList(){
        return list;
    }

    public void add(String list){
        this.list.add(list);
    }
}
