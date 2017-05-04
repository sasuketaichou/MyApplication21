package com.example.mierul.myapplication21;

import android.app.Activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by mierul on 4/24/2017.
 */

public class FirebaseHelper {
    private final static String TAG = "FirebaseHelper";
    private Context context;
    private FirebaseAuth mAuth;

    public FirebaseHelper(Context context){
        this.context = context;
        getInstance();
    }

    private FirebaseAuth getInstance(){
        if(mAuth==null){
            mAuth = FirebaseAuth.getInstance();
        }
        return mAuth;
    }

    public void createUserWithEmailAndPassword(String email,String password){
        try {
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Log.e(TAG,"createUserWithEmailAndPassword",task.getException());
                        Toast.makeText(context,"Error : "+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                    postToBus(isLogin());
                }
            });
        } catch (Exception e ){
            Log.e(TAG,"createUserWithEmailAndPassword",e);
        }
    }

    public void signInWithEmailAndPassword(String user,String pass){
        try{
            mAuth.signInWithEmailAndPassword(user,pass)
                    .addOnCompleteListener((Activity)context, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Log.e(TAG,"signInWithEmailAndPassword",task.getException());
                        Toast.makeText(context,"Error : "+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                    postToBus(isLogin());
                }
            });

        } catch (Exception e ){
            Log.e(TAG,"signInWithEmailAndPassword",e);
        }
    }

    public void sign_out(){
        try {
            mAuth.signOut();
        } catch (Exception e ){
            Log.e(TAG,"sign out",e);
        }
    }

    public Profile getProfile(){
        Profile profile = null;
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            profile = new Profile();
            profile.setDisplayName(user.getDisplayName());
            profile.setEmail(user.getEmail());
            profile.setPhoto(user.getPhotoUrl());
        }
        return profile;
    }

    public void setProfile(Profile profile){

    }

    public boolean isLogin(){
        return mAuth.getCurrentUser()!=null;
    }

    public void postToBus(boolean result) {
        EventBus.getDefault().post(new BooleanEvent(result));
    }




}