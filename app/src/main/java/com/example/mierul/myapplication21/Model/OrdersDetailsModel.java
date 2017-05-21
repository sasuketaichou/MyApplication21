package com.example.mierul.myapplication21.Model;

/**
 * Created by mierul on 5/21/2017.
 */

public class OrdersDetailsModel {
    public String productName;
    public String numOrder;
    public String key;

    public OrdersDetailsModel(String productName,String numOrder,String key){
        this.productName =productName;
        this.key = key;
        this.numOrder = numOrder;
    }


}
