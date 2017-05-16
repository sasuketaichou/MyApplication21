package com.example.mierul.myapplication21.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mierul.myapplication21.Base.BaseFragment;
import com.example.mierul.myapplication21.R;

/**
 * Created by mierul on 3/16/2017.
 */

public class SecondFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = SecondFragment.class.getSimpleName();
    private String title;
    private String imagePath;
    private Bitmap bitmap;
    private TextView numberOfOrder;
    private int mInteger =1;

    public static SecondFragment newInstance(String title, String imagePath) {

        Bundle args = new Bundle();
        args.putString("title",title);
        args.putString("imgPath",imagePath);
        SecondFragment fragment = new SecondFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("title");
        imagePath = getArguments().getString("imgPath");
        bitmap = BitmapFactory.decodeStream(getImage(imagePath));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second,container,false);

        numberOfOrder = (TextView)view.findViewById(R.id.num_order);
        numberOfOrder.setText(String.valueOf(mInteger));
        ImageView imageView = (ImageView)view.findViewById(R.id.iv);
        imageView.setImageBitmap(bitmap);
        imageView.setOnClickListener(this);
        Button button = (Button)view.findViewById(R.id.btn_addToCart);
        button.setOnClickListener(this);

        view.findViewById(R.id.btn_plus).setOnClickListener(this);
        view.findViewById(R.id.btn_minus).setOnClickListener(this);

        initToolbar(view,TAG,true);

        return view;
    }

    public void checkOut(){
        //TODO pass numOfOrder to checkout fragment
        CheckoutFragment checkoutFragment = CheckoutFragment.newInstance(title,imagePath);
        replaceFragment(checkoutFragment);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_addToCart:
                checkOut();
                break;
            case R.id.iv:
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
            display(1);
            return;
        }
        display(--mInteger);
    }

    private void display(int number) {
        numberOfOrder.setText(String.valueOf(number));
    }


}
