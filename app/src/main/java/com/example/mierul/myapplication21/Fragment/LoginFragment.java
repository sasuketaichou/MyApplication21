package com.example.mierul.myapplication21.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mierul.myapplication21.Base.BaseFragment;
import com.example.mierul.myapplication21.FirebaseHelper;
import com.example.mierul.myapplication21.R;


/**
 * Created by mierul on 3/26/2017.
 */

public class LoginFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "LoginFragment";

    private FirebaseHelper helper;
    private EditText username,password;
    private int type;

    public static LoginFragment newInstance(int type){

        Bundle args = new Bundle();
        args.putInt("type",type);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        helper = new FirebaseHelper(getContext());
        type= getArguments().getInt("type");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login,container,false);

        initToolbar(view,LoginFragment.class.getSimpleName(),true);

        username = (EditText)view.findViewById(R.id.input_username);
        password = (EditText)view.findViewById(R.id.input_password);

        switch(type){
            case 1:
                initViewLogin(view);
                break;
            case 2:
                initViewSignup(view);
                break;
        }

        return view;
    }

    private void initViewSignup(View view) {

        Button buttonLogin = (Button)view.findViewById(R.id.btn_login);
        buttonLogin.setOnClickListener(this);
        buttonLogin.setText("Create Account");

        Button buttonRegister = (Button)view.findViewById(R.id.btn_register);
        buttonRegister.setVisibility(View.GONE);

        TextView link_login = (TextView)view.findViewById(R.id.link_login);
        link_login.setVisibility(View.GONE);


    }

    private void initViewLogin(View view) {

        Button buttonLogin = (Button)view.findViewById(R.id.btn_login);
        buttonLogin.setOnClickListener(this);
        buttonLogin.setText("Login");

        Button buttonRegister = (Button)view.findViewById(R.id.btn_register);
        buttonRegister.setOnClickListener(this);
        buttonRegister.setText("Sign Up");

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_login:

                if(validateForm()) {
                    String email = username.getText().toString();
                    String pass = password.getText().toString();

                    switch(type){
                        case 1:
                            if(helper.signInWithEmailAndPassword(email,pass)){
                                previousFragment();
                            }
                            break;
                        case 2:
                            if(helper.createUserWithEmailAndPassword(email,pass)){
                                previousFragment();
                            }
                            break;
                    }
                }
                break;
            case R.id.btn_register:
                int type = 2;
                replaceFragment(LoginFragment.newInstance(type));
                break;
        }

    }

    private boolean validateForm() {
        boolean valid = true;

        String email = username.getText().toString();
        if (TextUtils.isEmpty(email)) {
            username.setError("Required.");
            valid = false;
        } else {
            username.setError(null);
        }

        String pass = password.getText().toString();
        if (TextUtils.isEmpty(pass)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //previousFragment();
        }
        return true;
    }
}
