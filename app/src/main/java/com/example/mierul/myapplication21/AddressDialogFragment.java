package com.example.mierul.myapplication21;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Hexa-Amierul.Japri on 4/9/2017.
 */

public class AddressDialogFragment extends BaseDialogFragment implements View.OnClickListener{

    public static final String KEY_ADDRESS_NEW = "key.address.new";
    public static final String KEY_ADDRESS_DEFAULT = "key.address.default";

    private EditText address;
    private EditText city;
    private EditText postcode;
    private EditText country;
    private CheckBox checkBox;

    private TextView address_error;
    private TextView city_error;
    private TextView postcode_error;
    private TextView country_error;

    private Button btn_continue;
    private ImageButton btn_close;

    private Map<String,String> addressMap;

    public static AddressDialogFragment newInstance(Map<String,String> address) {
        AddressDialogFragment fragment = new AddressDialogFragment();
        Bundle b = new Bundle();

        if(address != null){
            b.putSerializable(KEY_ADDRESS_DEFAULT,(Serializable) address);
        }

        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.AppTheme);

        if(getArguments().containsKey(KEY_ADDRESS_DEFAULT)){

            //since bing says hashmap is serializable
            addressMap = (Map<String,String>)getArguments().getSerializable(KEY_ADDRESS_DEFAULT);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_product_address_dialog,container,false);

        initView(view);

        return view;
    }

    private void initView(View view) {

        TextView tv_title = (TextView)view.findViewById(R.id.tv_lbl_title);
        tv_title.setText("Address");

        address = (EditText)view.findViewById(R.id.address_address);
        city = (EditText)view.findViewById(R.id.address_city);
        postcode = (EditText)view.findViewById(R.id.address_postcode);
        country = (EditText)view.findViewById(R.id.address_country);
        checkBox =(CheckBox)view.findViewById(R.id.checkbox_address);

        address_error = (TextView)view.findViewById(R.id.tv_address_error);
        city_error = (TextView)view.findViewById(R.id.tv_city_error);
        postcode_error = (TextView)view.findViewById(R.id.tv_postcode_error);
        country_error = (TextView)view.findViewById(R.id.tv_country_error);

        btn_continue = (Button)view.findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(this);
        btn_close = (ImageButton)view.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(this);

        if(addressMap != null){
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked){

                        //get address string
                        String address_value = addressMap.get(Constant.ADDRESS_ADDRESS.getString());
                        String city_value = addressMap.get(Constant.ADDRESS_CITY.getString());
                        String postcode_value = addressMap.get(Constant.ADDRESS_POSTCODE.getString());
                        String country_value = addressMap.get(Constant.ADDRESS_COUNTRY.getString());

                        //initialize current address
                        address.setText(address_value);
                        city.setText(city_value);
                        postcode.setText(postcode_value);
                        country.setText(country_value);

                        //disable
                        setEnable(false);

                    } else {

                        String empty = "";
                        //empty
                        address.setText(empty);
                        city.setText(empty);
                        postcode.setText(empty);

                        setEnable(true);
                    }
                }
            });
        } else {
            checkBox.setVisibility(View.GONE);
        }
    }

    private void setEnable(boolean enable){

        address.setEnabled(enable);
        city.setEnabled(enable);
        postcode.setEnabled(enable);
        country.setEnabled(enable);
    }

    private void passResultAndDismiss() {

        //save to adressMap
        addressMap.put(Constant.ADDRESS_ADDRESS.getString(),address.getText().toString());
        addressMap.put(Constant.ADDRESS_CITY.getString(),city.getText().toString());
        addressMap.put(Constant.ADDRESS_POSTCODE.getString(),postcode.getText().toString());
        addressMap.put(Constant.ADDRESS_COUNTRY.getString(),country.getText().toString());

        Intent i = new Intent();
        i.putExtra(KEY_ADDRESS_NEW, (Serializable) addressMap);
        getActivity().setIntent(i);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());
    }

    public boolean validate(){

        boolean validate = true;

        if(address.getText().toString().isEmpty()){
            showErrorMessage(address.getId());
            validate = false;
        } else {

            hideErrorMessage(address.getId());
        }

        if (city.getText().toString().isEmpty()){
            showErrorMessage(city.getId());
            validate = false;
        } else {
            hideErrorMessage(address.getId());
        }

        if(postcode.getText().toString().isEmpty()){
            showErrorMessage(postcode.getId());
            validate = false;
        } else {
            hideErrorMessage(postcode.getId());
        }

        return validate;
    }

    private void showErrorMessage(int id) {

        switch (id){
            case R.id.address_address:
                address_error.setVisibility(View.VISIBLE);
                address_error.setText("Address is empty");
                break;

            case R.id.address_city:
                city_error.setVisibility(View.VISIBLE);
                city_error.setText("City is empty");
                break;

            case R.id.address_postcode:
                postcode_error.setVisibility(View.VISIBLE);
                postcode_error.setText("Postcode is empty");
                break;
        }
    }

    private void hideErrorMessage(int id) {

        switch (id){
            case R.id.address_address:
                address_error.setVisibility(View.GONE);
                break;

            case R.id.address_city:
                city_error.setVisibility(View.GONE);
                break;

            case R.id.address_postcode:
                postcode_error.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_continue:
                if(validate()){
                    passResultAndDismiss();
                }
                break;
            case R.id.btn_close:
                dismiss();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.sv_address_dialog).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return true;
            }
        });
    }
}
