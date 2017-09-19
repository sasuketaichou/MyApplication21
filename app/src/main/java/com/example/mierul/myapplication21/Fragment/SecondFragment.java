package com.example.mierul.myapplication21.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mierul.myapplication21.Adapter.ProductPicturePagerAdapter;
import com.example.mierul.myapplication21.AddressDialogFragment;
import com.example.mierul.myapplication21.Base.BaseFragment;
import com.example.mierul.myapplication21.ConfirmDialogFragment;
import com.example.mierul.myapplication21.Constant;
import com.example.mierul.myapplication21.FirebaseHelper;
import com.example.mierul.myapplication21.Model.OrdersDetailsModel;
import com.example.mierul.myapplication21.Model.ProductProfileModel;
import com.example.mierul.myapplication21.Model.ProfileDetailsModel;
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
    public static final int CONFIRM_REQUEST_CODE = 2001 ;
    public static final int ADDRESS_REQUEST_CODE = 2002 ;
    private String[] url;
    private String picKey;
    private int mInteger =1;

    private OrderForm form;
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

    private boolean isLogin;

    private Map<String, String> addressMap;

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

        if(helper == null){
            helper = new FirebaseHelper();
        }

        if(rHelper == null){
            rHelper = new RealmHelper(getContext());
        }

        //trigger getDetails
        helper.getProductProfile(picKey);

        //get address from server and update address view
        isLogin = helper.isLogin();
        if(isLogin){
            helper.getDetails();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second,container,false);

        LinearLayout ll_product_order = (LinearLayout)view.findViewById(R.id.ll_product_order);
        TextView tv_lbl_product_order = (TextView)ll_product_order.findViewById(R.id.tv_label);
        tv_lbl_product_order.setText("Order");

        numberOfOrder = (TextView)ll_product_order.findViewById(R.id.num_order);
        numberOfOrder.setText(String.valueOf(mInteger));

        Button checkOutButton =(Button) view.findViewById(R.id.btn_addToCart);
        checkOutButton.setOnClickListener(this);

        if(!isLogin){
            checkOutButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_hold));
        }

        view.findViewById(R.id.btn_plus).setOnClickListener(this);
        view.findViewById(R.id.btn_minus).setOnClickListener(this);

        ViewPager viewPager = (ViewPager)view.findViewById(R.id.product_pic_viewPager);
        viewPager.setAdapter(new ProductPicturePagerAdapter(getContext(),url));

        //TODO add pager indicator
        LinearLayout ll_product_name = (LinearLayout)view.findViewById(R.id.ll_product_name);
        LinearLayout ll_product_type = (LinearLayout)view.findViewById(R.id.ll_product_type);
        LinearLayout ll_product_cost = (LinearLayout)view.findViewById(R.id.ll_product_cost);
        LinearLayout ll_product_pieces = (LinearLayout)view.findViewById(R.id.ll_product_pieces);

        TextView tv_lbl_product_name = (TextView)ll_product_name.findViewById(R.id.tv_label);
        tv_lbl_product_name.setText("Name");
        TextView tv_lbl_product_type = (TextView)ll_product_type.findViewById(R.id.tv_label);
        tv_lbl_product_type.setText("Type");
        TextView tv_lbl_product_cost = (TextView)ll_product_cost.findViewById(R.id.tv_label);
        tv_lbl_product_cost.setText("Cost (RM)");
        TextView tv_lbl_product_pieces = (TextView)ll_product_pieces.findViewById(R.id.tv_label);
        tv_lbl_product_pieces.setText("Pieces");

        productName = (TextView)ll_product_name.findViewById(R.id.tv_value);
        productCost = (TextView)ll_product_cost.findViewById(R.id.tv_value);
        productType = (TextView)ll_product_type.findViewById(R.id.tv_value);
        productPieces = (TextView)ll_product_pieces.findViewById(R.id.tv_value);

        LinearLayout ll_product_address = (LinearLayout)view.findViewById(R.id.ll_product_address);
        ll_product_address.setOnClickListener(this);
        LinearLayout ll_product_note = (LinearLayout)view.findViewById(R.id.ll_product_note);
        ll_product_note.setOnClickListener(this);

        TextView tv_lbl_product_address = (TextView)ll_product_address.findViewById(R.id.tv_label);
        tv_lbl_product_address.setText("Address");
        TextView tv_lbl_product_note = (TextView)ll_product_note.findViewById(R.id.tv_label);
        tv_lbl_product_note.setText("Note");

        address_input = (TextView)ll_product_address.findViewById(R.id.tv_value);
        note_input = (TextView)ll_product_note.findViewById(R.id.tv_value);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void checkOut(){
        if(isLogin){
            proceedOrder();
        } else {
            holdOrder();
        }
    }

    private void proceedOrder() {
        String address = address_input.getText().toString();

        if(address.isEmpty()){
            snackBarToToast("Please fill in your address.");
            return;
        }
        String order = numberOfOrder.getText().toString();
        String name = productName.getText().toString();
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
        dialogFragment.setTargetFragment(this,CONFIRM_REQUEST_CODE);
        dialogFragment.show(getFragmentManager(),dialogFragment.getTag());

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
            case R.id.ll_product_address:
                holdEdit(4);
                break;
            case R.id.ll_product_note:
                holdEdit(5);
                break;
        }
    }

    private void holdEdit(int type) {
        if(isLogin){

            if(type == 4){
                showAddressDialog();
            } else {

                //show edit dialog for note only for now
                replaceFragment(EditProfileFragment.newInstance(type));
            }
        } else {
            snackBarToLogin("Please login before edit");
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
    public void FirebaseHelperListener(ProductProfileModel product){

        if (product != null){
            productName.setText(product.name);
            productCost.setText(product.getCost());
            productPieces.setText(product.getPieces());
            productType.setText(product.type);
        }

    }

    public String getTotal() {

        String cost = productCost.getText().toString();
        int integerCost = Integer.valueOf(cost);

        int integerTotal = integerCost * mInteger;

        return String.valueOf(integerTotal);
    }

    @Subscribe
    public void FirebaseHelperGetProfileListener(ProfileDetailsModel model){

        //new device
        if(model.address != null){

            String address = model.address.get("address");
            if(!address.isEmpty()){

                //set addressMap
                addressMap = model.address;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case CONFIRM_REQUEST_CODE:

                    if(data!=null){
                        boolean proceed = data.getBooleanExtra(ConfirmDialogFragment.RESULT,false);

                        if(proceed){
                            helper.addOrder(model);
                            switchFragment(new CheckoutFragment());
                        }
                    }

                    break;

                case ADDRESS_REQUEST_CODE:

                    if(data != null){

                        Map<String,String> address = (Map<String,String>)data.getSerializableExtra(AddressDialogFragment.KEY_ADDRESS_NEW);

                        if (!address.isEmpty()){
                            String address_string = simplifyAddress(address);
                            address_input.setText(address_string);
                        }
                    }
                    break;
            }
        }
    }

    private String simplifyAddress(Map<String, String> addressMap) {

        String address_value = addressMap.get(Constant.ADDRESS_ADDRESS.getString());
        String city_value = addressMap.get(Constant.ADDRESS_CITY.getString());
        String postcode_value = addressMap.get(Constant.ADDRESS_POSTCODE.getString());
        String country_value = addressMap.get(Constant.ADDRESS_COUNTRY.getString());

        String space = " ";
        String fullAddress = address_value+space+city_value+space+postcode_value+space+country_value;
        return fullAddress.trim().isEmpty()? "": fullAddress;
    }

    private void showAddressDialog(){

        AddressDialogFragment addressDialogFragment = AddressDialogFragment.newInstance(addressMap);
        addressDialogFragment.setTargetFragment(this,ADDRESS_REQUEST_CODE);
        addressDialogFragment.show(getFragmentManager(),addressDialogFragment.getTag());

    }
}
