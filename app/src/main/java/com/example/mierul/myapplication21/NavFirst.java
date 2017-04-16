package com.example.mierul.myapplication21;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by mierul on 3/14/2017.
 */
public class NavFirst extends BaseFragment {
    int page = 1;
    String title = NavFirst.class.getSimpleName();
    TextView tv;

    public static Fragment newInstance(int page, String title) {

        NavFirst fragment = new NavFirst();
        Bundle args = new Bundle();
        args.putInt("number",page);
        args.putString("title",title);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nav_first,container,false);
        tv = (TextView)view.findViewById(R.id.tv);
        tv.setText(title);

        initToolbar(view,title,true);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =item.getItemId();

        switch (id){
            case android.R.id.home:
                FragmentManager fragmentManager = getFragmentManager();
                FirstFragment homeFragment = (FirstFragment) getFragmentManager().findFragmentByTag(FirstFragment.class.getSimpleName());
                fragmentManager.beginTransaction().replace(R.id.root_main_frame,homeFragment).commit();
        }
        return super.onOptionsItemSelected(item);
    }
}
