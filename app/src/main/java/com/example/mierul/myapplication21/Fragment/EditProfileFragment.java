package com.example.mierul.myapplication21.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.mierul.myapplication21.Base.BaseFragment;
import com.example.mierul.myapplication21.R;

/**
 * Created by Hexa-Amierul.Japri on 8/5/2017.
 */

public class EditProfileFragment extends BaseFragment {
    private int position;

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
            EditText editText = (EditText) view.findViewById(R.id.user_profile_edit);
            editText.requestFocus();
        } else {
            view = inflater.inflate(R.layout.fragment_edit_profile_address,container,false);
            //TODO stringbuffer address
        }

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
                break;
            case 1:
                mTitle = ProfileFragment.PROFILE_EMAIL;
                break;
            case 2:
                mTitle = ProfileFragment.PROFILE_ADDRESS;
                break;
            case 3:
                mTitle = ProfileFragment.PROFILE_CONTACT;
                break;
        }
        return mTitle;
    }

    @Override
    public void onPause() {
        hideSoftKeyboard();
        super.onPause();
    }
}
