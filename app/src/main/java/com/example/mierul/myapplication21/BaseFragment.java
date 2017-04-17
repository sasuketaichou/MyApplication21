package com.example.mierul.myapplication21;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

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

    public void replaceFragment(Fragment fragment){

        try{
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            transaction.replace(R.id.root_main_frame,fragment);
            //replace the fragment with added to stack
            transaction.addToBackStack(null);
            transaction.commit();

        }catch (Exception e){
            Log.e(TAG,"replaceFragment",e);
        }
    }

    public void replaceFragmentHome(){
        try{
            FragmentManager fragmentManager = getFragmentManager();
            FirstFragment home = (FirstFragment) fragmentManager.findFragmentByTag(FirstFragment.class.getSimpleName());
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right)
                    .replace(R.id.root_main_frame,home)
                    .commit();

        }catch (Exception e){
            Log.e(TAG,"replaceFragmentHome",e);
        }

    }


    public void popFragment(){
        try{
            //to be use for up button-
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStack();

        }catch (Exception e){
            Log.e(TAG,"popFragment :",e);

        }

    }
}

