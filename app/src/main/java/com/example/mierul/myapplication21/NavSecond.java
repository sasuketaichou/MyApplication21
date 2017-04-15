package com.example.mierul.myapplication21;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by mierul on 3/14/2017.
 */
public class NavSecond extends Fragment {
    int page = 2;
    String title = NavSecond.class.getSimpleName();
    TextView tv;

    public static Fragment newInstance(int page, String title) {

        NavSecond fragment = new NavSecond();
        Bundle args = new Bundle();
        args.putInt("number",page);
        args.putString("title",title);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nav_second,container,false);
        tv = (TextView)view.findViewById(R.id.tv);
        tv.setText(title);

        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(NavSecond.class.getSimpleName());
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        return view;
    }
}
