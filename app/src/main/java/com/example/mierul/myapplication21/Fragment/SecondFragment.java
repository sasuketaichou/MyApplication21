package com.example.mierul.myapplication21.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mierul.myapplication21.Adapter.ProductPicturePagerAdapter;
import com.example.mierul.myapplication21.Base.BaseFragment;
import com.example.mierul.myapplication21.R;

/**
 * Created by mierul on 3/16/2017.
 */

public class SecondFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "SecondFragment";
    private String[] url;
    private TextView numberOfOrder;
    private int mInteger =1;

    public static SecondFragment newInstance(String[] url) {

        Bundle args = new Bundle();
        args.putStringArray("url",url);
        SecondFragment fragment = new SecondFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getStringArray("url");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second,container,false);

        numberOfOrder = (TextView)view.findViewById(R.id.num_order);
        numberOfOrder.setText(String.valueOf(mInteger));

        view.findViewById(R.id.btn_addToCart).setOnClickListener(this);
        view.findViewById(R.id.btn_plus).setOnClickListener(this);
        view.findViewById(R.id.btn_minus).setOnClickListener(this);

        initToolbar(view,TAG,true);

        ViewPager viewPager = (ViewPager)view.findViewById(R.id.product_pic_viewPager);
        //TODO url string array
        viewPager.setAdapter(new ProductPicturePagerAdapter(getContext(),url));

        return view;
    }

    public void checkOut(){
        //TODO pass numOfOrder to checkout fragment
        //CheckoutFragment checkoutFragment = CheckoutFragment.newInstance(title,imagePath);
        //replaceFragment(checkoutFragment);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_addToCart:
                checkOut();
                break;
            case R.id.btn_minus:
                decreaseInteger();
                break;
            case R.id.btn_plus:
                increaseInteger();
                break;
        }
    }

    private void increaseInteger() {
        display(++mInteger);
    }

    private void decreaseInteger() {
        if(mInteger == 1){
            return;
        }
        display(--mInteger);
    }

    private void display(int number) {
        numberOfOrder.setText(String.valueOf(number));
    }


}
