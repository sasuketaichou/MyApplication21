package com.example.mierul.myapplication21;

import java.util.List;

/**
 * Created by Hexa-Amierul.Japri on 19/5/2017.
 */

public class FirebaseListEvent {
    public List<?> list;

    public FirebaseListEvent(List<?> list){
        this.list = list;
    }

    public List<?> getList(){
        return list;
    }

}
