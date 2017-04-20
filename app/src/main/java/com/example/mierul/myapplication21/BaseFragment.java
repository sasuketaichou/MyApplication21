package com.example.mierul.myapplication21;

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

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

/**
 * Created by mierul on 4/15/2017.
 */

public abstract class BaseFragment extends Fragment {

    private static final String TAG = "RootFragment";
    private Toolbar toolbar = null;

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
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
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
            setLastfragment(fragment);
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                    .replace(R.id.root_main_frame,fragment)
                    .commit();

        }catch (Exception e){
            Log.e(TAG,"replaceFragment",e);
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
}

