package com.example.mierul.myapplication21.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mierul on 5/21/2017.
 */

public class OrdersDetailsModel implements Parcelable {
    public String productAddress;
    public String productNote;
    public String productName;
    public String numOrder;
    public String key;
    public String total;

    public OrdersDetailsModel(String productName,String numOrder,String key,String productAddress,String productNote,String total){
        this.productName =productName;
        this.key = key;
        this.numOrder = numOrder;
        this.productAddress = productAddress;
        this.productNote = productNote;
        this.total = total;
    }

    public OrdersDetailsModel(){
    }


    protected OrdersDetailsModel(Parcel in) {
        productAddress = in.readString();
        productNote = in.readString();
        productName = in.readString();
        numOrder = in.readString();
        key = in.readString();
        total = in.readString();
    }

    public static final Creator<OrdersDetailsModel> CREATOR = new Creator<OrdersDetailsModel>() {
        @Override
        public OrdersDetailsModel createFromParcel(Parcel in) {
            return new OrdersDetailsModel(in);
        }

        @Override
        public OrdersDetailsModel[] newArray(int size) {
            return new OrdersDetailsModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productAddress);
        dest.writeString(productNote);
        dest.writeString(productName);
        dest.writeString(numOrder);
        dest.writeString(key);
        dest.writeString(total);
    }
}
