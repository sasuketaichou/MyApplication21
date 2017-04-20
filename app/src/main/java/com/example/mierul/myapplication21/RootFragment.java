package com.example.mierul.myapplication21;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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