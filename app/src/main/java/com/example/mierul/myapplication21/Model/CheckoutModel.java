package com.example.mierul.myapplication21.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mierul on 5/21/2017.
 */

public class CheckoutModel implements Parcelable{

    public String productName;
    public String numOrder;
    public String key;
    public String url;
    public String address;
    public String note;
    public String total;

    public CheckoutModel(String productName,String numOrder,String key,String url,String address,String note,String total){
        this.productName =productName;
        this.key = key;
        this.numOrder = numOrder;
        this.url = url;
        this.address = address;
        this.note = note;
        this.total = total;
    }

    protected CheckoutModel(Parcel in) {
        productName = in.readString();
        numOrder = in.readString();
        key = in.readString();
        url = in.readString();
        address = in.readString();
        note = in.readString();
        total = in.readString();
    }

    public static final Creator<CheckoutModel> CREATOR = new Creator<CheckoutModel>() {
        @Override
        public CheckoutModel createFromParcel(Parcel in) {
            return new CheckoutModel(in);
        }

        @Override
        public CheckoutModel[] newArray(int size) {
            return new CheckoutModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productName);
        dest.writeString(numOrder);
        dest.writeString(key);
        dest.writeString(url);
        dest.writeString(address);
        dest.writeString(note);
        dest.writeString(total);
    }
}
