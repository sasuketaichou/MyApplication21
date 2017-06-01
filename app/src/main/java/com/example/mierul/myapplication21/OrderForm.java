package com.example.mierul.myapplication21;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Hexa-Amierul.Japri on 22/5/2017.
 */

public class OrderForm extends RealmObject {
    @PrimaryKey
    String id;
    String address;
    String note;

    public String getAddress() {
        return address == null ? "": address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId(){
        return id;
    }
}
