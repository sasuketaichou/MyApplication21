package com.example.mierul.myapplication21;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mierul on 3/16/2017.
 */

public class SecondFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = SecondFragment.class.getSimpleName();
    TextView textView;
    String title;
    String imagePath;
    Bitmap bitmap;

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

        textView = (TextView)view.findViewById(R.id.tv);
        ImageView imageView = (ImageView)view.findViewById(R.id.iv);
        imageView.setImageBitmap(bitmap);
        Button button = (Button)view.findViewById(R.id.btn_addToCart);
        button.setOnClickListener(this);

        initToolbar(view,SecondFragment.class.getSimpleName(),true);


        return view;
    }



    public void checkOut(){
        CheckoutFragment checkoutFragment = CheckoutFragment.newInstance(title,imagePath);
        replaceFragment(checkoutFragment);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_addToCart:
                checkOut();
                break;
//            case android.R.id.home:
//                Log.v(TAG,"SecondFragment");
//                //popFragment();
//                break;

        }
    }
}
