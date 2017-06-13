package com.example.mierul.myapplication21.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mierul.myapplication21.Adapter.ProductPicturePagerAdapter;
import com.example.mierul.myapplication21.Base.BaseFragment;
import com.example.mierul.myapplication21.ConfirmDialogFragment;
import com.example.mierul.myapplication21.Event.ConfirmDialogFragmentEvent;
import com.example.mierul.myapplication21.FirebaseHelper;
import com.example.mierul.myapplication21.Model.OrdersDetailsModel;
import com.example.mierul.myapplication21.Model.ProductProfileModel;
import com.example.mierul.myapplication21.OrderForm;
import com.example.mierul.myapplication21.R;
import com.example.mierul.myapplication21.RealmHelper;

import org.greenrobot.eventbus.Subscribe;

import java.util.Map;

/**
 * Created by mierul on 3/16/2017.
 */

public class SecondFragment extends BaseFragment implements View.OnClickListener{
    private static final String TAG = "SecondFragment";
    private OrderForm form;
    private String[] url;
    private String picKey;
    private int mInteger =1;
    private FirebaseHelper helper;
    private RealmHelper rHelper;
    private OrdersDetailsModel model;

    private TextView numberOfOrder;
    private TextView productName;
    private TextView productCost;
    private TextView productType;
    private TextView productPieces;

    private TextView address_input;
    private TextView note_input;

    public static SecondFragment newInstance(String[] url,String key) {

        Bundle args = new Bundle();
        args.putStringArray("url",url);
        args.putString("picKey",key);
        SecondFragment fragment = new SecondFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getStringArray("url");
        picKey = getArguments().getString("picKey");
        helper = new FirebaseHelper();
        rHelper = new RealmHelper(getContext());

        //trigger getDetails
        helper.getProductProfile(picKey);

        //get address from db and display
        String id = helper.getUid();
        if(!id.isEmpty()){
            form = rHelper.getOrder(id);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second,container,false);

        numberOfOrder = (TextView)view.findViewById(R.id.num_order);
        numberOfOrder.setText(String.valueOf(mInteger));

        Button checkOutButton =(Button) view.findViewById(R.id.btn_addToCart);
        checkOutButton.setOnClickListener(this);

        if(!helper.isLogin()){
            checkOutButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_hold));
        }

        view.findViewById(R.id.btn_plus).setOnClickListener(this);
        view.findViewById(R.id.btn_minus).setOnClickListener(this);

        ViewPager viewPager = (ViewPager)view.findViewById(R.id.product_pic_viewPager);
        viewPager.setAdapter(new ProductPicturePagerAdapter(getContext(),url));

        //TODO add pager indicator

        productName = (TextView)view.findViewById(R.id.tv_product_name);
        productCost = (TextView)view.findViewById(R.id.tv_product_cost);
        productType = (TextView)view.findViewById(R.id.tv_product_type);
        productPieces = (TextView)view.findViewById(R.id.tv_product_pieces);

        address_input = (TextView)view.findViewById(R.id.tv_product_address_input);
        note_input = (TextView)view.findViewById(R.id.tv_product_note_input);

        view.findViewById(R.id.tv_product_address).setOnClickListener(this);
        address_input.setOnClickListener(this);
        view.findViewById(R.id.tv_product_note).setOnClickListener(this);
        note_input.setOnClickListener(this);

        if(form != null){

            address_input.setText(form.getAddress());
            note_input.setText(form.getNote());
        }

        //imageview pencil
        view.findViewById(R.id.iv_edit_address).setOnClickListener(this);
        view.findViewById(R.id.iv_edit_note).setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void checkOut(){
        if(helper.isLogin()){
            proceedOrder();
        } else {
            holdOrder();
        }
    }

    private void proceedOrder() {
        String order = numberOfOrder.getText().toString();
        String name = productName.getText().toString();

        String address = address_input.getText().toString();

        if(address.isEmpty()){
            snackBarToToast("Please fill in your address.");
            return;
        }
        String note = note_input.getText().toString();

        String total = getTotal();

        model = new OrdersDetailsModel();
        model.productName = name;
        model.numOrder = order;
        model.picKey = picKey;
        model.productAddress = address;
        model.productNote = note;
        model.total = total;

        ConfirmDialogFragment dialogFragment = ConfirmDialogFragment.newInstance(model);
        dialogFragment.show(getFragmentManager(),dialogFragment.getTag());

        //switchFragment(ConfirmDialogFragment.newInstance(model));
    }

    private void holdOrder(){
        //ask to login
        snackBarToLogin("Please login before order");
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
            case R.id.tv_product_address:
            case R.id.tv_product_address_input:
            case R.id.iv_edit_address:
                holdEdit(4);
                break;
            case R.id.tv_product_note:
            case R.id.tv_product_note_input:
            case R.id.iv_edit_note:
                holdEdit(5);
                break;
        }
    }

    private void holdEdit(int type) {
        if(helper.isLogin()){
            replaceFragment(EditProfileFragment.newInstance(type));
        } else {
            snackBarToLogin("Plese login before edit");
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
            productName.setText(event.name);
            productCost.setText(event.getCost());
            productPieces.setText(event.getPieces());
            productType.setText(event.type);
        }

    }

    public String getTotal() {

        String cost = productCost.getText().toString();
        int integerCost = Integer.valueOf(cost);

        int integerTotal = integerCost * mInteger;

        return String.valueOf(integerTotal);
    }

    @Subscribe
    public void onConfirmDialogFragmentListener(ConfirmDialogFragmentEvent result) {
        if(result.getResult()){
            helper.addOrder(model);
            switchFragment(new CheckoutFragment());
        }

    }
}
