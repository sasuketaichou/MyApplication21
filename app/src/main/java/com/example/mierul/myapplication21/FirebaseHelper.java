package com.example.mierul.myapplication21;

import android.app.Activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mierul.myapplication21.Model.ProfileDetailsModel;
import com.example.mierul.myapplication21.Model.ProfileFirebaseModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by mierul on 4/24/2017.
 */

public class FirebaseHelper {
    private final static String TAG = "FirebaseHelper";
    private Context context;

    public enum Node {
        name,
        address,
        contact,
        email
    }

    public FirebaseHelper(Context context){
        this.context = context;
    }

    public FirebaseHelper(){
    }

    public void createUserWithEmailAndPassword(String email,String password){
        try {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Log.e(TAG,"createUserWithEmailAndPassword",task.getException());
                        Toast.makeText(context,"Error : "+task.getException(),Toast.LENGTH_SHORT).show();
                    } else {
                        //for new user, create empty Profile branch
                        setNewUserDetails();
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
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
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
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
        } catch (Exception e ){
            Log.e(TAG,"sign out",e);
        }
    }

    public boolean isLogin(){
        return FirebaseAuth.getInstance().getCurrentUser()!=null;
    }

    public void postToBus(boolean result) {
        EventBus.getDefault().post(new FirebaseHelperEvent(result));
    }

    public void postToBus(ProfileDetailsModel model) {
        EventBus.getDefault().post(model);
    }

    public ProfileFirebaseModel getProfile(){

        ProfileFirebaseModel profile = null;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            profile = new ProfileFirebaseModel();
            profile.setDisplayName(user.getDisplayName());
            profile.setEmail(user.getEmail());
            profile.setPhoto(user.getPhotoUrl());
        }
        return profile;
    }


    public void getDetails() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ProfileDetailsModel model = dataSnapshot.getValue(ProfileDetailsModel.class);
                postToBus(model);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO create error message event
                Log.e(TAG,"getDetails",databaseError.toException());
            }
        };
        DatabaseReference usersProfile = getUsersProfileRef();
        usersProfile.addValueEventListener(postListener);
    }

    public void setDetails(Node child,String value){
        DatabaseReference usersProfile = getUsersProfileRef();
        usersProfile
                .child(child.name())
                .setValue(value)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()){
                            Log.e(TAG,"setDetails",task.getException());
                        }
                        postToBus(task.isSuccessful());
                    }
                });
    }

    private void setNewUserDetails(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference usersProfile = getUsersProfileRef();
        usersProfile.setValue(new ProfileDetailsModel("Edit your name","Edit your address","Add your contact number",mAuth.getCurrentUser().getEmail()));
    }

    private DatabaseReference getRootRef(){
        return FirebaseDatabase.getInstance().getReference();
    }

    private DatabaseReference getUsersRef(){
        String uId = getUid();
        return uId.isEmpty()? null:getRootRef().child("users").child(uId);
    }

    private DatabaseReference getOrdersRef(){
        String uId = getUid();
        return uId.isEmpty()? null:getRootRef().child("orders").child(uId);
    }

    private DatabaseReference getUsersProfileRef(){
        return getUsersRef().child("Profile");
    }

    private DatabaseReference getUsersOrderRef(){
        return getUsersRef().child("Order");
    }

    private String getUid(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = "empty";
        try {
            uid = mAuth.getCurrentUser().getUid();
        } catch (NullPointerException npe){
            Log.e(TAG,"getUid",npe);
        }
        return uid;
    }

    public void downloadProductPicture(View imageView){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        //TODO get image to place on viewpager



    }
}
