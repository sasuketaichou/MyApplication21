package com.example.mierul.myapplication21.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mierul.myapplication21.Adapter.ProfileAdapter;
import com.example.mierul.myapplication21.Base.BaseFragment;
import com.example.mierul.myapplication21.FirebaseHelper;
import com.example.mierul.myapplication21.Model.ProfileDetailsModel;
import com.example.mierul.myapplication21.Model.ProfileFirebaseModel;
import com.example.mierul.myapplication21.ProfileAdapterEvent;
import com.example.mierul.myapplication21.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Hexa-Amierul.Japri on 4/5/2017.
 */

public class ProfileFragment extends BaseFragment implements View.OnClickListener {
    private FirebaseHelper helper;
    private ProfileFirebaseModel profileFirebase;
    private ProfileDetailsModel profileDetails;

    public static String PROFILE_NAME = "NAME";
    public static String PROFILE_ADDRESS = "ADDRESS";
    public static String PROFILE_EMAIL = "EMAIL";
    public static String PROFILE_CONTACT = "CONTACT";


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        initToolbar(view,getClass().getSimpleName(),true);

        initView(view);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.profileRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ProfileAdapter(profileDetails));

        return view;
    }

    private void initView(View view) {
        view.findViewById(R.id.btn_signout).setOnClickListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new FirebaseHelper();
        profileFirebase = helper.getProfile();
        profileDetails = helper.getDetails();
    }

    public static Fragment newInstance() {

        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_signout:
                helper.sign_out();
                previousFragment();
                break;
        }
    }

    @Subscribe
    public void ProfileAdapterListener(ProfileAdapterEvent profileAdapterEvent){
        int position = profileAdapterEvent.getPosition();
        replaceFragment(EditProfileFragment.newInstance(position));
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
