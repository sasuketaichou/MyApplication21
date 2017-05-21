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
import com.example.mierul.myapplication21.FirebaseHelper;
import com.example.mierul.myapplication21.Model.OrdersDetailsModel;
import com.example.mierul.myapplication21.Model.ProductProfileModel;
import com.example.mierul.myapplication21.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by mierul on 3/16/2017.
 */

public class SecondFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "SecondFragment";
    private String[] url;
    private String key;
    private TextView numberOfOrder;
    private int mInteger =1;
    private FirebaseHelper helper;

    TextView productName;
    TextView productCost;
    TextView productType;
    TextView productPieces;

    public static SecondFragment newInstance(String[] url,String key) {

        Bundle args = new Bundle();
        args.putStringArray("url",url);
        args.putString("key",key);
        SecondFragment fragment = new SecondFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getStringArray("url");
        key = getArguments().getString("key");
        helper = new FirebaseHelper();

        //trigger getDetails
        helper.getProductProfile(key);

        //Todo get address from db and display
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
        viewPager.setAdapter(new ProductPicturePagerAdapter(getContext(),url));

        productName = (TextView)view.findViewById(R.id.tv_product_name);
        productCost = (TextView)view.findViewById(R.id.tv_product_cost);
        productType = (TextView)view.findViewById(R.id.tv_product_type);
        productPieces = (TextView)view.findViewById(R.id.tv_product_pieces);

        return view;
    }

    public void checkOut(){

        String order = numberOfOrder.getText().toString();
        String name = productName.getText().toString();

        OrdersDetailsModel model = new OrdersDetailsModel(name,order,key);
        helper.addOrder(model);

        replaceFragment(new CheckoutFragment());
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

    @Subscribe
    public void FirebaseHelperListener(ProductProfileModel event){

        if (event != null){
            String cost = "RM "+event.getCost();
            productName.setText(event.name);
            productCost.setText(cost);
            productPieces.setText(event.getPieces());
            productType.setText(event.type);
        }

    }


}
