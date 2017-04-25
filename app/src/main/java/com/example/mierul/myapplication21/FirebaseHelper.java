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

/**
 * Created by mierul on 4/24/2017.
 */

public class FirebaseHelper {
    private final static String TAG = "FirebaseHelper";
    private final Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public FirebaseHelper(Context context){
        this.context = context;
    }

    public boolean createUserWithEmailAndPassword(String email,String password){
        boolean result = false;
        try {
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Log.e(TAG,"createUserWithEmailAndPassword",task.getException());
                        Toast.makeText(context,"Error : "+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
            result = mAuth.getCurrentUser() != null;

        } catch (Exception e ){
            Log.e(TAG,"createUserWithEmailAndPassword",e);
        }

        return result;
    }

    public boolean signInWithEmailAndPassword(String user,String pass){
        boolean result = false;
        try{
            mAuth.signInWithEmailAndPassword(user,pass)
                    .addOnCompleteListener((Activity)context, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                    } else {
                        Log.e(TAG,"signInWithEmailAndPassword",task.getException());
                        Toast.makeText(context,"Error : "+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
            result = mAuth.getCurrentUser() != null;
            Log.v("naruto","result : "+result);

        } catch (Exception e ){
            Log.e(TAG,"signInWithEmailAndPassword",e);
        }
        
        return result;
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




}
