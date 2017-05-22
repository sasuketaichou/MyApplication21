package com.example.mierul.myapplication21.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mierul.myapplication21.Adapter.CheckoutAdapter;
import com.example.mierul.myapplication21.Base.BaseFragment;
import com.example.mierul.myapplication21.FirebaseHelper;
import com.example.mierul.myapplication21.Event.FirebaseListEvent;
import com.example.mierul.myapplication21.Model.CheckoutModel;
import com.example.mierul.myapplication21.Model.OrdersDetailsModel;
import com.example.mierul.myapplication21.Model.ProductUrlPictureModel;
import com.example.mierul.myapplication21.R;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hexa-Amierul.Japri on 17/4/2017.
 */

public class CheckoutFragment extends BaseFragment implements View.OnClickListener {
    private FirebaseHelper helper;
    private List<CheckoutModel> item;
    private CheckoutAdapter adapter;
    private int checkUpdate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = new ArrayList<>();
        helper = new FirebaseHelper();
        //trigger get order
        helper.getOrder();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout,container,false);
        initToolbar(view,CheckoutFragment.class.getSimpleName(),true);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.checkOutRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CheckoutAdapter(getContext(),item);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public static CheckoutFragment newInstance(String address) {
        Bundle args = new Bundle();
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

    @Subscribe
    public void FirebaseListener(FirebaseListEvent event){

        Object obj = event.getList().get(0);

        if(obj instanceof OrdersDetailsModel) {
            int needUpdate = event.getList().size();

            if (checkUpdate == 0 || needUpdate > checkUpdate) {
                checkUpdate = needUpdate;
                refreshData(event);
                //trigger get picture
                helper.getProductPicture();
            }
        } else if(obj instanceof ProductUrlPictureModel){
            List<ProductUrlPictureModel> list = (List<ProductUrlPictureModel>)event.getList();

            for (CheckoutModel model : item){
                for (int i =0; i<list.size();i++){
                    if(list.get(i).key.equals(model.key)){
                        model.url = list.get(i).image_1;
                        break;
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void refreshData(FirebaseListEvent event){
        //clear item to avoid duplication
        if(!item.isEmpty()){
            item.clear();
        }

        for(OrdersDetailsModel model : (List<OrdersDetailsModel>)event.getList()){

            String productName = model.productName;
            String numOrder = model.numOrder;
            String key = model.key;
            String address = model.productAddress;
            String note = model.productNote;

            CheckoutModel checkoutModel = new CheckoutModel(productName,
                    numOrder,
                    key,
                    "",
                    address,
                    note);

            item.add(checkoutModel);
        }
    }
}
