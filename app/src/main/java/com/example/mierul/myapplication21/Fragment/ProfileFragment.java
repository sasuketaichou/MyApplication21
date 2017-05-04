package com.example.mierul.myapplication21.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mierul.myapplication21.Base.BaseFragment;
import com.example.mierul.myapplication21.FirebaseHelper;
import com.example.mierul.myapplication21.Profile;
import com.example.mierul.myapplication21.R;

/**
 * Created by Hexa-Amierul.Japri on 4/5/2017.
 */

public class ProfileFragment extends BaseFragment implements View.OnClickListener {
    private FirebaseHelper helper;
    private Profile profile;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        initToolbar(view,getClass().getSimpleName(),true);

        initView(view);

        return view;
    }

    private void initView(View view) {
        view.findViewById(R.id.btn_signout).setOnClickListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new FirebaseHelper(getContext());
        profile = helper.getProfile();
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
}
