package com.example.mierul.myapplication21;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mierul on 3/14/2017.
 */
public class RootFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_root,container,false);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.root_main_frame,new FirstFragment(),FirstFragment.class.getSimpleName())
                .addToBackStack(null)
                .commit();

        return view;
    }
}