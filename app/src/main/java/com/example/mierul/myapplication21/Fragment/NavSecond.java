package com.example.mierul.myapplication21.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mierul.myapplication21.Base.BaseFragment;
import com.example.mierul.myapplication21.R;

/**
 * Created by mierul on 3/14/2017.
 */
public class NavSecond extends BaseFragment {
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

        return view;
    }
}
