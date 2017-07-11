package com.example.mierul.myapplication21.Fragment;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
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
    private int type = -1;

    private final static int LOGIN_PAGE = 1;
    private final static int SIGNUP_PAGE = 2;

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

                view.findViewById(R.id.link_forgot_password).setOnClickListener(this);
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
                        case LOGIN_PAGE:
                            helper.signInWithEmailAndPassword(email,pass);
                            break;
                        case SIGNUP_PAGE:
                            helper.createUserWithEmailAndPassword(email,pass);
                            break;
                    }
                }
                break;
            case R.id.btn_register:
                switchFragment(LoginFragment.newInstance(SIGNUP_PAGE));
                break;

            case R.id.link_forgot_password:

                switch (type){
                    case LOGIN_PAGE:
                        //show dialog send reset password
                        showPasswordResetDialog();
                        break;
                    case SIGNUP_PAGE:
                        //go to login page
                        switchFragment(LoginFragment.newInstance(LOGIN_PAGE));
                        break;
                }

                break;

            case R.id.link_login:
                switchFragment(LoginFragment.newInstance(LOGIN_PAGE));
                break;
        }

    }

    private void showPasswordResetDialog() {
        String mTitle = "Reset Password";
        String message = "Press RESET to send a reset password to your email.\nChange your password immediately after you have retrieve it.";

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.input_dialog,null);
        builder.setView(dialogView);

        TextView title = (TextView)dialogView.findViewById(R.id.dialogTitle);
        title.setText(mTitle);

        TextView description = (TextView)dialogView.findViewById(R.id.dialogDescription);
        description.setText(message);

        builder.setPositiveButton("RESET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText emailInput = (EditText)dialogView.findViewById(R.id.userInputEmail);
                String email = emailInput.getText().toString();

                if(!email.isEmpty()){
                    showProgressDialog();
                    helper.resetPassword(email);
                    dialog.dismiss();
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);
        builder.create().show();
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

        int id = result.getId();

        switch (id){
            case FirebaseHelper.SIGNINEMAILPASSWORD:
            case FirebaseHelper.CREATEUSEREMAILPASSWORD:
                //return previous fragment
                if(result.getResult()){
                    previousFragment();
                } else {
                    String errorMessage = result.getMessage();
                    snackBarToToast(errorMessage);
                }
                break;

            case FirebaseHelper.RESETPASSWORD:
                showLaunchEmailApp(result.getMessage());
                break;
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

    public void showLaunchEmailApp(String message){

        snackBarWithMessageAndListener(message, "EMAIL", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //testing
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setType("message/rfc822");
                try {
                    startActivity(Intent.createChooser(intent,"Launch email."));
                } catch (ActivityNotFoundException anfe){
                    anfe.printStackTrace();
                }

            }
        });

    }
}
