package com.example.mierul.myapplication21.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mierul.myapplication21.Base.BaseFragment;
import com.example.mierul.myapplication21.FirebaseHelper;
import com.example.mierul.myapplication21.Event.FirebaseBooleanEvent;
import com.example.mierul.myapplication21.R;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by mierul on 3/26/2017.
 */

public class LoginFragment extends BaseFragment implements View.OnClickListener{

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

        username = (EditText)view.findViewById(R.id.input_username);
        password = (EditText)view.findViewById(R.id.input_password);

        view.findViewById(R.id.link_forgot_password).setOnClickListener(this);

        initView(view);

        return view;
    }

    private void initLayoutListener() {
        getView().findViewById(R.id.loginLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return true;
            }
        });

    }

    private void initView(View view) {

        Button buttonLogin = (Button)view.findViewById(R.id.btn_login);
        Button buttonRegister = (Button)view.findViewById(R.id.btn_register);
        TextView link_login = (TextView)view.findViewById(R.id.link_login);

        switch(type){
            case 1:
                buttonLogin.setText("Login");
                buttonRegister.setText("Sign Up");
                buttonLogin.setOnClickListener(this);
                buttonRegister.setOnClickListener(this);
                break;

            case 2:
                buttonLogin.setOnClickListener(this);
                buttonLogin.setText("Create Account");
                buttonRegister.setVisibility(View.GONE);
                link_login.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_login:
                if(validateForm()) {
                    String email = username.getText().toString();
                    String pass = password.getText().toString();

                    showProgressDialog();

                    switch(type){
                        case 1:
                            helper.signInWithEmailAndPassword(email,pass);
                            break;
                        case 2:
                            helper.createUserWithEmailAndPassword(email,pass);
                            break;
                    }
                }
                break;
            case R.id.btn_register:
                int type = 2;
                switchFragment(LoginFragment.newInstance(type));
                break;

            case R.id.link_forgot_password:
                //show dialog send reset password
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

    @Subscribe
    public void FirebaseHelperListener(FirebaseBooleanEvent result){
        if(result.getResult()){
            previousFragment();
        }
        hideProgressDialog();
    }

    @Override
    public void onResume() {
        if(getView()!=null){
            initLayoutListener();
        }
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        hideSoftKeyboard();
        super.onDestroyView();
    }
}
