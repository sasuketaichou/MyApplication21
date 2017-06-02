package com.example.mierul.myapplication21;

import android.app.Activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mierul.myapplication21.Event.FirebaseBooleanEvent;
import com.example.mierul.myapplication21.Event.FirebaseListEvent;
import com.example.mierul.myapplication21.Model.CheckoutModel;
import com.example.mierul.myapplication21.Model.OrdersDetailsModel;
import com.example.mierul.myapplication21.Model.ProductProfileModel;
import com.example.mierul.myapplication21.Model.ProductUrlPictureModel;
import com.example.mierul.myapplication21.Model.UserDetails.ProfileAddressModel;
import com.example.mierul.myapplication21.Model.UserDetails.ProfileDetailsModel;
import com.example.mierul.myapplication21.Model.ProfileFirebaseModel;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mierul on 4/24/2017.
 */

public class FirebaseHelper {
    private final static String TAG = "FirebaseHelper";
    private Context context;

    private final String ROOT_USERS = "users";
    private final String ROOT_PRODUCT = "product";
    private final String ROOT_ORDERS = "orders";
    private final String CHILD_PROFILE = "Profile";
    private final String CHILD_URL = "url";
    private final String CHILD_ORDER = "Order";
    private final String CHILD_IMAGE = "Image";
    private final String CHILD_ADDRESS = "address";

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
                    postToBus(new FirebaseBooleanEvent(isLogin()));
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
                    postToBus(new FirebaseBooleanEvent(isLogin()));
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

    private void postToBus(Object result){
        EventBus.getDefault().post(result);
    }

    public ProfileFirebaseModel getProfile(){

        ProfileFirebaseModel profile = null;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            profile = new ProfileFirebaseModel();
            profile.setDisplayName(user.getDisplayName());
            profile.setEmail(user.getEmail());
            profile.setPhoto(user.getPhotoUrl());
        }
        return profile;
    }

    public void getDetails() {
        DatabaseReference usersProfile = getUsersProfileRef();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ProfileDetailsModel model = dataSnapshot.getValue(ProfileDetailsModel.class);
                postToBus(model);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG,"getDetails",databaseError.toException());
            }
        };
        usersProfile.addValueEventListener(postListener);
    }

    public void setDetails(String child,String value){
        DatabaseReference usersProfile = getUsersProfileRef();
        usersProfile
                .child(child)
                .setValue(value)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()){
                            Log.e(TAG,"setDetails",task.getException());

                        }
                        postToBus(new FirebaseBooleanEvent(task.isSuccessful()));
                    }
                });
    }

    public void setAddress(Map<String,Object> map) {

        DatabaseReference usersAddress = getUsersAddressRef();
        usersAddress.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

    }

    private void setNewUserDetails(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference usersProfile = getUsersProfileRef();
        DatabaseReference usersAddress = getUsersAddressRef();

        String empty = "";

        ProfileDetailsModel detailsModel = new ProfileDetailsModel();
        detailsModel.name = empty;
        detailsModel.contact = empty;
        detailsModel.email = mAuth.getCurrentUser().getEmail();

        ProfileAddressModel addressModel = new ProfileAddressModel();
        addressModel.address = empty;
        addressModel.city = empty;
        addressModel.postcode = empty;
        addressModel.country = empty;

        usersProfile.setValue(detailsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
        usersAddress.setValue(addressModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    private DatabaseReference getRootRef(){
        return FirebaseDatabase.getInstance().getReference();
    }

    private DatabaseReference getUsersRef(){
        String uId = getUid();
        return uId.isEmpty()? null:getRootRef().child(ROOT_USERS).child(uId);
    }

    private DatabaseReference getOrdersRef(){
        String uId = getUid();
        return uId.isEmpty()? null:getRootRef().child(ROOT_ORDERS).child(uId);
    }

    private DatabaseReference getUsersProfileRef(){
            return getUsersRef().child(CHILD_PROFILE);
    }

    private DatabaseReference getUsersAddressRef(){
        return getUsersProfileRef().child(CHILD_ADDRESS);
    }

    private DatabaseReference getUsersOrderRef(){
        return getUsersRef().child(CHILD_ORDER);
    }

    public String getUid(){
        String id ="";
        try{
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            if(mAuth.getCurrentUser() != null)
                id = mAuth.getCurrentUser().getUid();
        } catch (NullPointerException npe){
            Log.e(TAG,"getUid",npe);
        }
        return id;
    }

    public void downloadProductPicture(ImageView imageView){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesFolderRef = storageRef.child("image");
        StorageReference productRef = imagesFolderRef.child("productOne");
        StorageReference images = productRef.child("4.jpg");

        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(images)
                .into(imageView);
    }

    public void getProductPicture(){

        DatabaseReference productImageRef = getRootRef().child(ROOT_PRODUCT)
                .child(CHILD_IMAGE);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> productChildImage = dataSnapshot.getChildren();

                List<ProductUrlPictureModel> productUrlPictureModels = new ArrayList<>();
                for(DataSnapshot mSnapshot: productChildImage){
                    ProductUrlPictureModel model = mSnapshot.getValue(ProductUrlPictureModel.class);
                    model.addKey(mSnapshot.getKey());
                    productUrlPictureModels.add(model);

                }
                postToBus(new FirebaseListEvent(productUrlPictureModels));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG,"getProductPicture",databaseError.toException());

            }
        };
        productImageRef.addValueEventListener(eventListener);

    }

    public void getProductProfile(String key){

        DatabaseReference productProfileRef = getRootRef().child(ROOT_PRODUCT)
                .child(CHILD_PROFILE)
                .child(key);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ProductProfileModel model = dataSnapshot.getValue(ProductProfileModel.class);
                postToBus(model);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG,"getProductProfile",databaseError.toException());

            }
        };
        productProfileRef.addValueEventListener(eventListener);



    }

    public void testing(){
        //BROKEN
        DatabaseReference ref = getRootRef().child(CHILD_URL).child(CHILD_IMAGE).child(ROOT_PRODUCT);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("naruto",String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("naruto",databaseError.toString());
            }
        });

        Log.v("naruto","runnning");
    }

    public void addOrder(OrdersDetailsModel model) {

        DatabaseReference orderRef = getOrdersRef();
        String ordersId =orderRef.push().getKey();

        DatabaseReference userOrderRef = getUsersOrderRef();
        String usrOrdKey = userOrderRef.push().getKey();
        userOrderRef.child(usrOrdKey).setValue(ordersId);

        DatabaseReference ordersUserKey = orderRef.child(ordersId);
        model.usrOrdKey = usrOrdKey;
        ordersUserKey.setValue(model);

    }

    public void getOrder() {

        DatabaseReference userOrderRef = getUsersOrderRef();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> order = dataSnapshot.getChildren();

                List<String> list = new ArrayList<>();
                for (DataSnapshot mSnapshot: order){
                    String key = mSnapshot.getValue(String.class);
                    list.add(key);
                }
                if(list.size()>0){
                    getOrderByKey(list);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        userOrderRef.addValueEventListener(eventListener);
    }

    private void getOrderByKey(final List<String> key){

        DatabaseReference orderRef = getOrdersRef();
        final List<CheckoutModel> list = new ArrayList<>();

        for (int i=0;i<key.size();i++){

            final String ordKey = key.get(i);
            DatabaseReference orderKeyRef = orderRef.child(ordKey);
            final int end = i;
            orderKeyRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    OrdersDetailsModel model = dataSnapshot.getValue(OrdersDetailsModel.class);
                    if(model != null){
                        CheckoutModel checkoutModel = new CheckoutModel();

                        checkoutModel.productName =model.productName;
                        checkoutModel.numOrder =model.numOrder;
                        checkoutModel.picKey= model.picKey;
                        checkoutModel.address =model.productAddress;
                        checkoutModel.note=model.productNote;
                        checkoutModel.total = model.total;
                        checkoutModel.ordKey = ordKey;
                        checkoutModel.usrOrdKey = model.usrOrdKey;

                        list.add(checkoutModel);
                    }

                    if(end == key.size()-1 && list.size()>0){
                        //postobus after reach end loop
                        postToBus(new FirebaseListEvent(list));
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    public void removeOrderByKey(List<Map<String,String>> mapKey){

        if(mapKey!= null){
            for(Map<String,String> key : mapKey){
                removeOrderAtOrders(key.get(Constant.NODE_ORDKEY.getNode()));
                removeOrderAtUsersOrder(key.get(Constant.NODE_USRORDKEY.getNode()));
            }
        }
    }

    private void removeOrderAtOrders(String ordKey){

        DatabaseReference orderRef = getOrdersRef();
        orderRef.child(ordKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG,"removeOrderAtOrders is success");
                } else {
                    Log.e(TAG,"removeOrderAtOrders",task.getException());
                }
            }
        });

    }

    private void removeOrderAtUsersOrder(String usrOrdKey) {

        DatabaseReference usersOrderRef = getUsersOrderRef();
        usersOrderRef.child(usrOrdKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG,"removeOrderAtUsersOrder is success");
                } else {
                    Log.e(TAG,"removeOrderAtUsersOrder",task.getException());
                }
            }
        });

    }

    public void setDisplayName(String displayName) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();

        if (user != null) {
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.v(TAG, "User profile updated.");
                            }
                        }
                    });
        }
    }
}
