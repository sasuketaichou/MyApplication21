package com.example.mierul.myapplication21.Base;

import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.mierul.myapplication21.Fragment.FirstFragment;
import com.example.mierul.myapplication21.Fragment.LoginFragment;
import com.example.mierul.myapplication21.FragmentStack;
import com.example.mierul.myapplication21.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;

import java.io.IOException;
import java.io.InputStream;


import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by mierul on 4/15/2017.
 */

public abstract class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";
    private Toolbar toolbar = null;
    public ProgressDialog mProgressDialog;
    private InputMethodManager inputMethodManager;
    private CoordinatorLayout coordinatorLayout;
    private String title = "";

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(title.isEmpty()){
            title = getClass().getSimpleName();
        }
        initToolbar(title);

        Class first = getClass();
        if(!first.isInstance(FirstFragment.class)){
            setDisplayHomeAsUpEnabled();
        }
    }

    public void initToolbar(String title){
        try{
            toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
            toolbar.setTitle(title);

        } catch (NullPointerException npe){
            Log.e(TAG,"initToolbar: ",npe);

        }
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setDisplayHomeAsUpEnabled(){
        AppCompatActivity ap = (AppCompatActivity)getActivity();
        ap.setSupportActionBar(toolbar);
        ap.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initCoordinatorLayout(int id){
        try{
            coordinatorLayout = (CoordinatorLayout)getView().findViewById(id);
        } catch (NullPointerException npe){
            Log.e(TAG,"initCoordinatorLayout: ",npe);
        }
    }

    public Toolbar getToolbar(){
        if(toolbar == null){
            Log.e(TAG,"toolbar is null");
            initToolbar(title);
        }
        return toolbar;
    }

    public InputStream getImage(String imagePath){
        AssetManager ass = getActivity().getAssets();
        InputStream is = null;
        try {
            is = ass.open("img/"+imagePath);
        } catch (IOException e) {
            Log.e(TAG,"getImage",e);
        }

        return is;
    }

    public void previousFragment(){
        try{
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction
                    .setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right)
                    .replace(R.id.root_main_frame,getLastFragment())
                    .commit();

        }catch (Exception e){
            Log.e(TAG,"previousFragment",e);
        }
    }

    public void replaceFragment(Fragment fragment){
        try{
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                    .replace(R.id.root_main_frame,fragment)
                    .commit();

            setLastfragment(fragment);

        }catch (Exception e){
            Log.e(TAG,"replaceFragment",e);
        }
    }

    public void switchFragment(Fragment fragment){
        try{
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                    .replace(R.id.root_main_frame,fragment)
                    .commit();

        }catch (Exception e){
            Log.e(TAG,"switchFragment",e);
        }
    }

    private void setLastfragment(Fragment fragment){
        try{
            FragmentStack.addStack(fragment);
        }catch (Exception e){
            Log.e(TAG,"setLastFragment",e);
        }
    }

    private Fragment getLastFragment(){
        Fragment fragment = null;
        try{
            fragment = FragmentStack.getPrevious();

        }catch (Exception e){
            Log.e(TAG,"getLastFragment",e);
        }
        return fragment;
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private InputMethodManager getInputMethodManager(){
        if(inputMethodManager == null){
            inputMethodManager =(InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        }
        return inputMethodManager;
    }

    public void hideSoftKeyboard(){
        if(getView()!= null){
            getInputMethodManager()
                    .hideSoftInputFromWindow(getView().getRootView().getWindowToken(), 0);
                    //.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    public void showSoftKeyboard(){
        if(getInputMethodManager().isActive()){
            getInputMethodManager()
                    //.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
                    .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        try{
            EventBus.getDefault().register(this);
        } catch (EventBusException ebEx){
            Log.e(TAG,"this class : "+getClass()+" do not have Subscriber");
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            EventBus.getDefault().unregister(this);
        } catch (EventBusException ebEx){
        }
    }

    public void snackBarToLogin(String message){
        if(coordinatorLayout != null){
            Snackbar.make(coordinatorLayout,
                    message,
                    Snackbar.LENGTH_LONG)
                    .setAction("Login",snackbarLoginListener())
                    .show();
        }
    }

    private View.OnClickListener snackbarLoginListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = 1;
                replaceFragment(LoginFragment.newInstance(type));
            }
        };
        return listener;
    }


}

