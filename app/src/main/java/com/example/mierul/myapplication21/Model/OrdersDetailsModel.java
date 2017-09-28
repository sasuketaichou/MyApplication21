package com.example.mierul.myapplication21.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mierul on 5/21/2017.
 */

public class OrdersDetailsModel implements Parcelable {
    private String productAddress;
    private String productNote;
    private String productName;
    private String numOrder;
    private String picKey;
    private String total;
    private long timestamp;

    public OrdersDetailsModel(){
    }

    protected OrdersDetailsModel(Parcel in) {
        productAddress = in.readString();
        productNote = in.readString();
        productName = in.readString();
        numOrder = in.readString();
        picKey = in.readString();
        total = in.readString();
        timestamp = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productAddress);
        dest.writeString(productNote);
        dest.writeString(productName);
        dest.writeString(numOrder);
        dest.writeString(picKey);
        dest.writeString(total);
        dest.writeLong(timestamp);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getProductAddress() {
        return productAddress;
    }

    public void setProductAddress(String productAddress) {
        this.productAddress = productAddress;
    }

    public String getProductNote() {
        return productNote;
    }

    public void setProductNote(String productNote) {
        this.productNote = productNote;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getNumOrder() {
        return numOrder;
    }

    public void setNumOrder(String numOrder) {
        this.numOrder = numOrder;
    }

    public String getPicKey() {
        return picKey;
    }

    public void setPicKey(String picKey) {
        this.picKey = picKey;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
