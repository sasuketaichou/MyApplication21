package com.example.mierul.myapplication21.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.example.mierul.myapplication21.Base.BaseFragment;
import com.example.mierul.myapplication21.R;

/**
 * Created by mierul on 3/14/2017.
 */
public class RootFragment extends BaseFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.root_main_frame,new FirstFragment())
                .commit();

        Log.v("naruto","Root fragment getbackstackEntry : "+getFragmentManager().getBackStackEntryCount());
    }
}