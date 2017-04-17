package com.example.mierul.myapplication21;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Hexa-Amierul.Japri on 17/4/2017.
 */

public class CheckoutFragment extends BaseFragment {
    TextView textView;
    private int minteger = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout,container,false);
        initToolbar(view,CheckoutFragment.class.getSimpleName(),true);

        textView = (TextView) view.findViewById(R.id.tv);

        return view;
    }

    public void increaseInteger(View view) {
        minteger = minteger++;
        display(minteger);

    }public void decreaseInteger(View view) {
        minteger = minteger--;
        display(minteger);
    }

    private void display(int number) {
        textView.setText(String.valueOf(number));
    }

}
