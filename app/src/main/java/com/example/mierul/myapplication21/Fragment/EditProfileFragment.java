package com.example.mierul.myapplication21.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.mierul.myapplication21.Base.BaseFragment;
import com.example.mierul.myapplication21.FirebaseHelper;
import com.example.mierul.myapplication21.FirebaseHelperEvent;
import com.example.mierul.myapplication21.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Hexa-Amierul.Japri on 8/5/2017.
 */

public class EditProfileFragment extends BaseFragment implements View.OnClickListener {
    private int position;
    private FirebaseHelper.Node childNode;

    public static EditProfileFragment newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt("position",position);
        EditProfileFragment fragment = new EditProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = null;

        if(position != 2 ){
            view = inflater.inflate(R.layout.fragment_edit_profile,container,false);
            view.findViewById(R.id.user_profile_edit).requestFocus();
        } else {
            view = inflater.inflate(R.layout.fragment_edit_profile_address,container,false);
        }
        view.findViewById(R.id.btn_user_ok).setOnClickListener(this);
        view.findViewById(R.id.btn_user_cancel).setOnClickListener(this);

        String title = getTitle(position);
        initToolbar(view,title,true);
        //TODO make auto show softkeyboard
        //without lagging ui
        return view;
    }

    private String getTitle(int position){
        String mTitle = "";

        switch(position){
            case 0:
                mTitle = ProfileFragment.PROFILE_NAME;
                childNode = FirebaseHelper.Node.name;
                break;
            case 1:
                mTitle = ProfileFragment.PROFILE_EMAIL;
                childNode = FirebaseHelper.Node.email;
                break;
            case 2:
                mTitle = ProfileFragment.PROFILE_ADDRESS;
                childNode = FirebaseHelper.Node.address;
                break;
            case 3:
                mTitle = ProfileFragment.PROFILE_CONTACT;
                childNode = FirebaseHelper.Node.contact;
                break;
        }
        return mTitle;
    }

    @Override
    public void onPause() {
        hideSoftKeyboard();
        super.onPause();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_user_ok:
                FirebaseHelper helper = new FirebaseHelper();
                if(position != 2 && getView() != null) {
                    EditText editText =(EditText)getView().findViewById(R.id.user_profile_edit);
                    helper.setDetails(childNode, editText.getText().toString());
                } else if (getView()!= null){
                    View view = getView();
                    String address = ((EditText)view.findViewById(R.id.address_address)).getText().toString();
                    String city = ((EditText)view.findViewById(R.id.address_city)).getText().toString();
                    String country = "Malaysia"; //((EditText)view.findViewById(R.id.address_country)).getText().toString();
                    String postcode = ((EditText)view.findViewById(R.id.address_postcode)).getText().toString();
                    String comma = ", ";

                    String userAddress = address.concat(comma).concat(city).concat(comma).concat(postcode).concat(comma).concat(country);
                    helper.setDetails(childNode,userAddress);
                }
                break;

            case R.id.btn_user_cancel:
                previousFragment();
                break;
        }
    }

    @Subscribe
    public void FirebaseHelperEditProfileListener(FirebaseHelperEvent event){
        if(event.getResult()){
            previousFragment();
        }
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
