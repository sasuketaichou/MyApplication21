package com.example.mierul.myapplication21.Fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mierul.myapplication21.Adapter.ProfileAdapter;
import com.example.mierul.myapplication21.Base.BaseFragment;
import com.example.mierul.myapplication21.FirebaseHelper;
import com.example.mierul.myapplication21.Model.ProfileDetailsModel;
import com.example.mierul.myapplication21.Model.ProfileFirebaseModel;
import com.example.mierul.myapplication21.Event.ProfileAdapterEvent;
import com.example.mierul.myapplication21.R;

import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

/**
 * Created by Hexa-Amierul.Japri on 4/5/2017.
 */

public class ProfileFragment extends BaseFragment implements View.OnClickListener {
    private FirebaseHelper helper;
    private String name;
    private String email;
    private ProfileAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        initView(view);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.profileRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProfileAdapter(new ProfileDetailsModel());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void initView(View view) {
        view.findViewById(R.id.btn_signout).setOnClickListener(this);

        ((TextView)view.findViewById(R.id.user_profile_name)).setText(name);
        ((TextView)view.findViewById(R.id.user_profile_email)).setText(email);

        ImageView userPhoto = (ImageView)view.findViewById(R.id.user_profile_photo);
        userPhoto.setOnClickListener(this);

        Uri uri = helper.getProfile().getPhoto();

        Bitmap bitmap= getBitmapFromUri(uri);
        if(uri != null){
            RoundedBitmapDrawable roundDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
            roundDrawable.setCircular(true);
            
            userPhoto.setImageDrawable(roundDrawable);
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        Bitmap bitmap =null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(helper == null){
            helper = new FirebaseHelper();
        }

        if(helper.isLogin()){
            //trigger getDetails to get data
            helper.getDetails();

            showProgressDialog();

            //get display name and email
            ProfileFirebaseModel model =helper.getProfile();
            name = model.getDisplayName();
            email = model.getEmail();
        }
    }

    public static ProfileFragment newInstance(ProfileFirebaseModel model) {

        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("username",model.getDisplayName());
        args.putString("email",model.getEmail());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_signout:
                helper.sign_out();
                previousFragment();
                break;
            case R.id.user_profile_photo:
               //open camera
                replaceFragment(new CameraFragment());
                break;
        }
    }

    @Subscribe
    public void ProfileAdapterListener(ProfileAdapterEvent profileAdapterEvent){
        int position = profileAdapterEvent.getPosition();
        replaceFragment(EditProfileFragment.newInstance(position));
    }

    @Subscribe
    public void FirebaseHelperListener(ProfileDetailsModel model){
        //update ui after finish loading data
        adapter.refresh(model);
        hideProgressDialog();
    }
}
