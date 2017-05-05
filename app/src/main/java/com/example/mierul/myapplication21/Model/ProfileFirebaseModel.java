package com.example.mierul.myapplication21.Model;

import android.net.Uri;

/**
 * Created by mierul on 4/18/2017.
 */

public class ProfileFirebaseModel {
    private String email;
    private String displayName;
    private String providerData;
    private String providerId;
    private String providers;
    private String uId;
    private Uri photo;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProviderData() {
        return providerData;
    }

    public void setProviderData(String providerData) {
        this.providerData = providerData;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviders() {
        return providers;
    }

    public void setProviders(String providers) {
        this.providers = providers;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public Uri getPhoto() {
        return photo;
    }

    public void setPhoto(Uri photo) {
        this.photo = photo;
    }

}
