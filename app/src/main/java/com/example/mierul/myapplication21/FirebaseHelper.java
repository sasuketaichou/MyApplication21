package com.example.mierul.myapplication21;

import android.app.Activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.mierul.myapplication21.Model.ProfileDetailsModel;
import com.example.mierul.myapplication21.Model.ProfileFirebaseModel;
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
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public FirebaseHelper(Context context){
        this.context = context;
    }

    public FirebaseHelper(){
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
        } catch (NullPointerException npe ){
            Log.e(TAG,"createUserWithEmailAndPassword",npe);
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

        } catch (NullPointerException npe ){
            Log.e(TAG,"signInWithEmailAndPassword",npe);
        }
    }

    public void sign_out(){
        try {
            mAuth.signOut();
        } catch (Exception e ){
            Log.e(TAG,"sign out",e);
        }
    }

    public boolean isLogin(){
        return mAuth.getCurrentUser()!=null;
    }

    public void postToBus(boolean result) {
        EventBus.getDefault().post(new FirebaseHelperEvent(result));
    }

    public ProfileFirebaseModel getProfile(){
        ProfileFirebaseModel profile = null;
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            profile = new ProfileFirebaseModel();
            profile.setDisplayName(user.getDisplayName());
            profile.setEmail(user.getEmail());
            profile.setPhoto(user.getPhotoUrl());
        }
        return profile;
    }


    public ProfileDetailsModel getDetails() {
        ProfileDetailsModel details = new ProfileDetailsModel();
        //TODO grab data from firebase
        return details;
    }
}
