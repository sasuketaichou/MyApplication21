package com.example.mierul.myapplication21.Base;

import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.mierul.myapplication21.FragmentStack;
import com.example.mierul.myapplication21.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

/**
 * Created by mierul on 4/15/2017.
 */

public abstract class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";
    private Toolbar toolbar = null;
    public ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initToolbar(View view,String title,Boolean displayUp){
        try{
            toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            toolbar.setTitle(title);
            AppCompatActivity ap = (AppCompatActivity)getActivity();
            ap.setSupportActionBar(toolbar);
            ap.getSupportActionBar().setDisplayHomeAsUpEnabled(displayUp);

        } catch (Exception e){
            Log.e(TAG,"initToolbar: ",e);

        }
    }

    public Toolbar getToolbar(){
        if(toolbar == null){
            Log.e(TAG,"toolbar is null");
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
}

