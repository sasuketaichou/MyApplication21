package com.example.mierul.myapplication21;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by mierul on 4/24/2017.
 */

public class FirebaseHelper {
    private final static String TAG = "FirebaseHelper";
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public FirebaseHelper(Context context){
        this.context = context;
    }

    private boolean createUserWithEmailAndPassword(String email,String password,boolean result){
        mAuth.createUserWithEmailAndPassword(email,password);
        return true;
    }

    public boolean createUserWithEmailAndPassword(String email,String password){
        boolean result = false;
        try {
            if(createUserWithEmailAndPassword(email,password,result)){
                result = mAuth.getCurrentUser() != null;
            }

        } catch (Exception e ){
            Log.e(TAG,"createUserWithEmailAndPassword",e);
        }

        return result;
    }




}
