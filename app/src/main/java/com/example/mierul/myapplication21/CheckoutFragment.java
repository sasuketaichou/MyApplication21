package com.example.mierul.myapplication21;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Hexa-Amierul.Japri on 17/4/2017.
 */

public class CheckoutFragment extends BaseFragment implements View.OnClickListener {
    TextView textView;
    String title;
    String imagePath;
    Bitmap bitmap;
    private int minteger = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("title");
        imagePath = getArguments().getString("imgPath");
        bitmap = BitmapFactory.decodeStream(getImage(imagePath));
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout,container,false);
        initToolbar(view,CheckoutFragment.class.getSimpleName(),true);

        textView = (TextView) view.findViewById(R.id.tv);
        ImageView iv = (ImageView)view.findViewById(R.id.ivCh);
        iv.setImageBitmap(bitmap);

        return view;
    }

    public void increaseInteger(View view) {
        display(minteger++);

    }public void decreaseInteger(View view) {
        if(minteger == 0)
            return;

        display(minteger--);
    }

    private void display(int number) {
        textView.setText(String.valueOf(number));
    }

    public static CheckoutFragment newInstance(String title, String imagePath) {
        Bundle args = new Bundle();
        args.putString("title",title);
        args.putString("imgPath",imagePath);
        CheckoutFragment fragment = new CheckoutFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case android.R.id.home:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //previousFragment();
                break;
        }
        return true;
    }
}
