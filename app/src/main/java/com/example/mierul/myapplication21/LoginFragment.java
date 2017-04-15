package com.example.mierul.myapplication21;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by mierul on 3/26/2017.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText username,password;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(LoginFragment.class.getSimpleName(), "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(LoginFragment.class.getSimpleName(), "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login,container,false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(LoginFragment.class.getSimpleName());
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Button buttonLogin = (Button)view.findViewById(R.id.btn_login);
        buttonLogin.setOnClickListener(this);
        Button buttonRegister = (Button)view.findViewById(R.id.btn_register);
        buttonRegister.setOnClickListener(this);

        username = (EditText)view.findViewById(R.id.input_username);
        password = (EditText)view.findViewById(R.id.input_password);
        username.setText("");
        password.setText("");

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_login:

                String user = username.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if(!user.isEmpty() && !pass.isEmpty()) {

                    firebaseAuth.signInWithEmailAndPassword(user, pass)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        FragmentManager fragmentManager = getFragmentManager();
                                        FirstFragment home = (FirstFragment) fragmentManager.findFragmentByTag(FirstFragment.class.getSimpleName());
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right)
                                                .replace(R.id.root_main_frame,home)
                                                .commit();

                                        Toast.makeText(getActivity(), "Welcome "+task.getResult().getUser().getDisplayName(), Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Login failed :" + task, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                break;
            case R.id.btn_register:
                break;
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }
}
