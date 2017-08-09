package com.example.mierul.myapplication21.Fragment;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
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
import com.example.mierul.myapplication21.DialogUtil;
import com.example.mierul.myapplication21.FirebaseHelper;
import com.example.mierul.myapplication21.Event.FirebaseBooleanEvent;
import com.example.mierul.myapplication21.R;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mierul on 3/26/2017.
 */

public class LoginFragment extends BaseFragment implements View.OnClickListener{

    private static final String TAG = "LoginFragment";

    private FirebaseHelper helper;
    private EditText username,password;
    private TextView tv_error;
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

        tv_error = (TextView)view.findViewById(R.id.tv_login_error);

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
            case LOGIN_PAGE:
                buttonLogin.setText("Login");
                buttonRegister.setText("Sign Up");
                buttonLogin.setOnClickListener(this);
                buttonRegister.setOnClickListener(this);
                break;

            case SIGNUP_PAGE:
                buttonLogin.setOnClickListener(this);
                buttonLogin.setText("Create Account");
                buttonRegister.setVisibility(View.GONE);

                TextView loginPage = (TextView) view.findViewById(R.id.link_forgot_password);
                loginPage.setText("Already registered? Go to Login page.");
                loginPage.setOnClickListener(this);
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

                    if(tv_error.getVisibility() == View.VISIBLE){
                        tv_error.setVisibility(View.GONE);
                    }

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
        String message = "Press RESET to send a reset password to your email.\nChange your password immediately after you have retrieve the password.";

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
                //return previous fragment
                if(result.getResult()){
                    previousFragment();
                } else {
                    showErrorMessage(result.getMessage());
                }
                break;
            case FirebaseHelper.CREATEUSEREMAILPASSWORD:

                if(result.getResult()){
                    //display congratz message
                    showCongratzMessage();
                } else {
                    showErrorMessage(result.getMessage());
                }
                break;

            case FirebaseHelper.RESETPASSWORD:

                if(result.getResult()){
                    showLaunchEmailApp(result.getMessage());
                } else {
                    showErrorMessage(result.getMessage());
                }

                break;
        }

        hideProgressDialog();
    }

    private void showCongratzMessage() {

        DialogUtil.alertUserDialog(getContext(), "Congratulation",
                "you have successfully registered.",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                previousFragment();

            }
        });
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
                launchEmailApp();
            }
        });

    }

    //https://stackoverflow.com/questions/3489068/how-do-i-launch-the-email-client-directly-to-inbox-view
    private void launchEmailApp() {

        Intent emailIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"));

        PackageManager pm = getActivity().getPackageManager();

        List<ResolveInfo> resInfo = pm.queryIntentActivities(emailIntent, 0);

        if (resInfo.size() > 0) {

            ResolveInfo ri = resInfo.get(0);

            String notFallbackPackageName = ri.activityInfo.packageName;
            String fallback = "com.android.fallback";

            //check for fallback, the app must hv emaip app, otherwise return fallback and nullpointer exception
            if(!notFallbackPackageName.equals(fallback)){
                // First create an intent with only the package name of the first registered email app
                // and build a picked based on it
                Intent intentChooser = pm.getLaunchIntentForPackage(notFallbackPackageName);

                Log.e(TAG,"intent chooser :"+intentChooser);
                Intent openInChooser =
                        Intent.createChooser(intentChooser,
                                "Choose email app");

                // Then create a list of LabeledIntent for the rest of the registered email apps
                List<LabeledIntent> intentList = new ArrayList<>();

                for (int i = 1; i < resInfo.size(); i++) {
                    // Extract the label and repackage it in a LabeledIntent
                    ri = resInfo.get(i);
                    String packageName = ri.activityInfo.packageName;
                    Intent intent = pm.getLaunchIntentForPackage(packageName);
                    intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
                }

                LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);

                // Add the rest of the email apps to the picker selection
                openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
                startActivity(openInChooser);
            } else {
                showErrorMessage("You have no email app in your device.");
            }
        }
    }

    public void showErrorMessage(String errorMessage){
        //snackBarToToast(errorMessage);
        tv_error.setVisibility(View.VISIBLE);
        tv_error.setText(errorMessage);
    }
}
