package com.example.mierul.myapplication21.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.mierul.myapplication21.Base.BaseFragment;
import com.example.mierul.myapplication21.Constant;
import com.example.mierul.myapplication21.FirebaseHelper;
import com.example.mierul.myapplication21.FirebaseBooleanEvent;
import com.example.mierul.myapplication21.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Hexa-Amierul.Japri on 8/5/2017.
 */

public class EditProfileFragment extends BaseFragment implements View.OnClickListener {
    private int position;
    private Constant childNode;

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

        switch (position){
            case 0:
            case 1:
            case 3:
                view = inflater.inflate(R.layout.fragment_edit_profile,container,false);
                view.findViewById(R.id.user_profile_edit).requestFocus();
                break;
            case 2:
            case 4:
                view = inflater.inflate(R.layout.fragment_edit_profile_address,container,false);
                break;
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
        Constant mTitle = null;

        switch(position){
            case 0:
                mTitle = Constant.PROFILE_NAME;
                childNode = Constant.NODE_NAME;
                break;
            case 1:
                mTitle = Constant.PROFILE_EMAIL;
                childNode = Constant.NODE_EMAIL;
                break;
            case 2:
                mTitle = Constant.PROFILE_ADDRESS;
                childNode = Constant.NODE_ADDRESS;
                break;
            case 3:
                mTitle = Constant.PROFILE_CONTACT;
                childNode = Constant.NODE_CONTACT;
                break;
        }
        return mTitle.getTitle();
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
                if(position != 2 && position!= 4 && getView() != null) {
                    EditText editText =(EditText)getView().findViewById(R.id.user_profile_edit);
                    helper.setDetails(childNode.getNode(), editText.getText().toString());
                } else if (getView()!= null){
                    View view = getView();
                    String address = ((EditText)view.findViewById(R.id.address_address)).getText().toString();
                    String city = ((EditText)view.findViewById(R.id.address_city)).getText().toString();
                    String country = "Malaysia"; //((EditText)view.findViewById(R.id.address_country)).getText().toString();
                    String postcode = ((EditText)view.findViewById(R.id.address_postcode)).getText().toString();
                    String comma = ", ";


                    String userAddress = address+comma+city+comma+postcode+comma+country;
                    if(position == 2){
                        helper.setDetails(childNode.getNode(),userAddress);
                    }

                    //Todo for position 4, sent edited adress to db and show in SecondFragment
                }
                break;

            case R.id.btn_user_cancel:
                previousFragment();
                break;
        }
    }

    @Subscribe
    public void FirebaseHelperEditProfileListener(FirebaseBooleanEvent event){
        if(event.getResult()){
            previousFragment();
        } else {
            //Todo tell user the error
        }
    }
}
