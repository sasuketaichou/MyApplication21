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
    public String picKey;
    public String total;
    public String usrOrdKey;

    public OrdersDetailsModel(){
    }


    protected OrdersDetailsModel(Parcel in) {
        productAddress = in.readString();
        productNote = in.readString();
        productName = in.readString();
        numOrder = in.readString();
        picKey = in.readString();
        total = in.readString();
        usrOrdKey = in.readString();
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
        dest.writeString(picKey);
        dest.writeString(total);
        dest.writeString(usrOrdKey);
    }
}
