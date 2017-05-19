package com.example.mierul.myapplication21.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    private RecyclerView recyclerView;
    private String name;
    private String email;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        initToolbar(view,getClass().getSimpleName(),true);

        initView(view);

        recyclerView = (RecyclerView)view.findViewById(R.id.profileRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    private void initView(View view) {
        view.findViewById(R.id.btn_signout).setOnClickListener(this);

        ((TextView)view.findViewById(R.id.user_profile_name)).setText(name);
        ((TextView)view.findViewById(R.id.user_profile_email)).setText(email);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        name = getArguments().getString("username");
        email = getArguments().getString("email");

        helper = new FirebaseHelper();
        //trigger getDetails to get data
        helper.getDetails();
        showProgressDialog();
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

    @Subscribe
    public void FirebaseHelperListener(ProfileDetailsModel model){
        //update ui after finish loading data
        ProfileAdapter adapter = new ProfileAdapter(model);
        recyclerView.setAdapter(adapter);
        hideProgressDialog();
    }
}
