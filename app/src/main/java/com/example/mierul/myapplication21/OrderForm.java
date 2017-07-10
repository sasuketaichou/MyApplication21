package com.example.mierul.myapplication21;

import java.util.HashMap;
import java.util.Map;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Hexa-Amierul.Japri on 22/5/2017.
 */

public class OrderForm extends RealmObject {
    @PrimaryKey
    public String id;
    public String address;
    public String city;
    public String postcode;
    public String country;
    public String note;

    public Map<String,String> getMapAddress() {

        Map<String,String> map = new HashMap<>();
        map.put("address",address);
        map.put("city",city);
        map.put("postcode",postcode);
        map.put("country",country);

        return map;
    }

    public String getAddress(){
        String space = " ";
        String fullAddress = address+space+city+space+postcode+space+country;
        return address == null? "": fullAddress;
    }

    public void setAddress(Map<String,String> map) {
        address = map.get("address");
        city = map.get("city");
        postcode = map.get("postcode");
        country = map.get("country");
    }

    public String getNote() {
        return note == null? "":note;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
