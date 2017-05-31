package com.example.mierul.myapplication21.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mierul on 5/21/2017.
 */

public class CheckoutModel implements Parcelable{

    public String productName;
    public String numOrder;
    public String picKey;
    public String ordKey;
    public String url;
    public String address;
    public String note;
    public String total;
    public String usrOrdKey;

    public CheckoutModel(){
    }

    protected CheckoutModel(Parcel in) {
        productName = in.readString();
        numOrder = in.readString();
        picKey = in.readString();
        url = in.readString();
        address = in.readString();
        note = in.readString();
        total = in.readString();
        ordKey = in.readString();
        usrOrdKey = in.readString();
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
        dest.writeString(picKey);
        dest.writeString(url);
        dest.writeString(address);
        dest.writeString(note);
        dest.writeString(total);
        dest.writeString(ordKey);
        dest.writeString(usrOrdKey);
    }
}
