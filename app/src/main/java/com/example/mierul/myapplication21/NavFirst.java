package com.example.mierul.myapplication21;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by mierul on 3/14/2017.
 */
public class NavFirst extends BaseFragment {
    private final String TAG ="NavFirst" ;
    int page = 1;
    String title = TAG;
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
                Log.v(TAG,"nav1st R.id.home");
        }
        return super.onOptionsItemSelected(item);
    }
}
