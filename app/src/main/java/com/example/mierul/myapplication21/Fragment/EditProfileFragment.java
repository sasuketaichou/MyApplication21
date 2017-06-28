package com.example.mierul.myapplication21.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.mierul.myapplication21.Base.BaseFragment;
import com.example.mierul.myapplication21.Constant;
import com.example.mierul.myapplication21.FirebaseHelper;
import com.example.mierul.myapplication21.Event.FirebaseBooleanEvent;
import com.example.mierul.myapplication21.Model.ProfileDetailsModel;
import com.example.mierul.myapplication21.OrderForm;
import com.example.mierul.myapplication21.R;
import com.example.mierul.myapplication21.RealmHelper;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hexa-Amierul.Japri on 8/5/2017.
 */

public class EditProfileFragment extends BaseFragment implements View.OnClickListener {
    private int position;
    private Constant childNode;
    private RealmHelper rHelper;
    private FirebaseHelper helper;
    private OrderForm form;
    private ProfileDetailsModel model;

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

        if(rHelper == null){
            rHelper = new RealmHelper(getContext());
        }

        if(helper == null){
            helper = new FirebaseHelper();
        }

        form = rHelper.getOrder(helper.getUid());
        if(form == null){
            form = new OrderForm();
            form.setId(helper.getUid());
        }

        String title = getTitle(position);
        setTitle(title);

        if(position == 4){
            helper.getDetails();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(position == 4 || position == 2){
            CheckBox checkBox =(CheckBox)view.findViewById(R.id.checkbox_address);

            if(position == 4){
                if(form.getAddress().isEmpty()){
                    checkBox.setVisibility(View.INVISIBLE);
                } else {
                    checkBox.setOnCheckedChangeListener(checkListener());
                }
            } else {
                checkBox.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = null;

        switch (position){
            case 1:
                view = inflater.inflate(R.layout.fragment_edit_profile_password,container,false);
                break;
            case 0:
            case 3:
            case 5:
                view = inflater.inflate(R.layout.fragment_edit_profile,container,false);
                EditText edit = (EditText) view.findViewById(R.id.user_profile_edit);
                edit.requestFocus();
                String hint = getTitle(position);
                edit.setHint(hint);
                break;
            case 2:
            case 4:
                view = inflater.inflate(R.layout.fragment_edit_profile_address,container,false);
                break;
            default:
                break;
        }

        view.findViewById(R.id.btn_ok).setOnClickListener(this);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);

        //TODO make auto show softkeyboard
        //without lagging ui
        return view;
    }

    private CompoundButton.OnCheckedChangeListener checkListener() {
        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                EditText address = (EditText)getView().findViewById(R.id.address_address);
                EditText city = (EditText)getView().findViewById(R.id.address_city);
                EditText postcode = (EditText)getView().findViewById(R.id.address_postcode);
                ((EditText)getView().findViewById(R.id.address_country)).setText("Malaysia");

                if(isChecked){
                    if(model != null){

                        address.setText(model.address.get("address"));
                        address.setEnabled(false);

                        city.setText(model.address.get("city"));
                        city.setEnabled(false);

                        postcode.setText(model.address.get("postcode"));
                        postcode.setEnabled(false);
                    }
                } else{

                    address.setEnabled(true);
                    city.setEnabled(true);
                    postcode.setEnabled(true);
                }
            }
        };

        return listener;
    }

    private String getTitle(int position){
        String mTitle;

        switch(position){
            case 0:
                mTitle = Constant.PROFILE_NAME.getTitle();
                childNode = Constant.NODE_NAME;
                break;
            case 1:
                mTitle = "CHANGE PASSWORD";
                childNode = Constant.NODE_EMAIL;
                break;
            case 2:
                mTitle = Constant.PROFILE_ADDRESS.getTitle();
                childNode = Constant.NODE_ADDRESS;
                break;
            case 3:
                mTitle = Constant.PROFILE_CONTACT.getTitle();
                childNode = Constant.NODE_CONTACT;
                break;
            case 4:
                mTitle = Constant.PRODUCT_ADDRESS.getTitle();
                break;
            case 5:
                mTitle = Constant.PRODUCT_NOTE.getTitle();
                break;
            default:
                mTitle = "EDIT";
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
            case R.id.btn_ok:

                switch (position){

                    case 1:

                        EditText newPasswordInput = (EditText)getView().findViewById(R.id.user_profile_password_new);
                        EditText confirmPasswordInput = (EditText)getView().findViewById(R.id.user_profile_password_confirm);
                        EditText currentPasswordInput = (EditText)getView().findViewById(R.id.user_profile_password_current);

                        String newPassword = newPasswordInput.getText().toString();
                        String confirmPassword = confirmPasswordInput.getText().toString();
                        String currentPassword = currentPasswordInput.getText().toString();

                        if(newPassword.equals(confirmPassword)){
                            if(!currentPassword.isEmpty()){
                                //proceed
                                showProgressDialog();
                                helper.setPassword(newPassword,currentPassword);
                            } else {
                                snackBarToToast("Please fill in current password.");
                            }
                        } else {
                            snackBarToToast("Password did not match.");
                        }
                        break;
                    case 0:
                        showProgressDialog();
                    case 3:

                        EditText editText =(EditText)getView().findViewById(R.id.user_profile_edit);
                        helper.setDetails(childNode.getNode(), editText.getText().toString());
                        if(position == 0){
                            helper.setDisplayName(editText.getText().toString());
                        }
                        break;
                    case 2:
                    case 4:

                        View view = getView();
                        String address = ((EditText)view.findViewById(R.id.address_address)).getText().toString();
                        String city = ((EditText)view.findViewById(R.id.address_city)).getText().toString();
                        String country = "Malaysia"; //((EditText)view.findViewById(R.id.address_country)).getText().toString();
                        String postcode = ((EditText)view.findViewById(R.id.address_postcode)).getText().toString();

                        if(address.isEmpty()){
                            snackBarToToast("Please enter address");
                            break;
                        }

                        if(city.isEmpty()){
                            snackBarToToast("Please enter city");
                            break;
                        }

                        if(postcode.isEmpty()){
                            snackBarToToast("Please enter postcode");
                            break;
                        }

                        Map<String,Object> userAddress = addressToMap(address,city,postcode,country);

                        if(position == 2){
                            helper.setAddress(userAddress);
                            if(form.getAddress().isEmpty()) {
                                form.setAddress(userAddress);
                                rHelper.saveOrder(form);
                            }
                        } else {
                            form.setAddress(userAddress);
                            rHelper.saveOrder(form);
                            previousFragment();
                        }
                        break;

                    case 5:

                        EditText noteEditText =(EditText)getView().findViewById(R.id.user_profile_edit);
                        form.setNote(noteEditText.getText().toString());
                        rHelper.saveOrder(form);
                        previousFragment();
                        break;
                }
                break;

            case R.id.btn_cancel:
                previousFragment();
                break;
        }
    }

    @Subscribe
    public void FirebaseHelperEditProfileListener(final FirebaseBooleanEvent event){

        if(position == 0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideProgressDialog();

                    if(event.getResult()){
                        previousFragment();
                    } else {
                        if(event.getMessage() != null){
                            snackBarToToast(event.getMessage());
                        }
                    }
                }
            }, 5000);
        } else if(event.getResult()){
            hideProgressDialog();
            previousFragment();
        } else {
            if(event.getMessage() != null){
                hideProgressDialog();
                snackBarToToast(event.getMessage());
            } else {
                hideProgressDialog();
                snackBarToToast("Edit failed. Please try again.");
            }
        }
    }

    @Subscribe
    public void FirebaseHelperGetProfileListener(ProfileDetailsModel model){

        //to get address
        this.model = model;
    }

    public Map<String,Object> addressToMap(String address,String city,String postcode,String country){

        Map<String,Object> map = new HashMap<>();
        map.put("address",address);
        map.put("city",city);
        map.put("postcode",postcode);
        map.put("country",country);

        return map;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgressDialog();
    }
}
