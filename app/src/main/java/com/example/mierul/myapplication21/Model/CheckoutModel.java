package com.example.mierul.myapplication21.Model;

/**
 * Created by mierul on 5/21/2017.
 */

public class CheckoutModel {

    public String productName;
    public String numOrder;
    public String key;
    public String url;

    public CheckoutModel(String productName,String numOrder,String key,String url){
        this.productName =productName;
        this.key = key;
        this.numOrder = numOrder;
        this.url = url;
    }
}
