package com.example.mierul.myapplication21;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Hexa-Amierul.Japri on 5/5/2017.
 */

public class AppUtils extends Application {
    private static FirebaseAuth mAuth;

    @Override
    public void onCreate() {
        super.onCreate();
        mAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseAuth getFirebaseInstance(){
        if(mAuth == null){
            mAuth = FirebaseAuth.getInstance();
            return mAuth;
        }
        return mAuth;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mAuth = null;
    }
}
