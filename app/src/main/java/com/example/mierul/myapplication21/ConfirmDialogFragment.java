package com.example.mierul.myapplication21;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mierul.myapplication21.Model.OrdersDetailsModel;

/**
 * Created by Hexa-Amierul.Japri on 28/5/2017.
 */

public class ConfirmDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private OrdersDetailsModel model;
    private static String modelTag = "ordersdetailsmodel";
    public static String RESULT = "result";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = getArguments().getParcelable(modelTag);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_confirm_dialog,container,false);

        ((TextView)view.findViewById(R.id.tv_product_name)).setText(model.productName);
        ((TextView)view.findViewById(R.id.tv_product_address)).setText(model.productAddress);
        ((TextView)view.findViewById(R.id.tv_product_note)).setText(model.productNote);
        ((TextView)view.findViewById(R.id.tv_product_order)).setText(model.numOrder);
        ((TextView)view.findViewById(R.id.tv_product_total)).setText(model.total);

        view.findViewById(R.id.btn_ok).setOnClickListener(this);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);

        return view;
    }

    public static ConfirmDialogFragment newInstance(OrdersDetailsModel model){
        Bundle args = new Bundle();
        args.putParcelable(modelTag,model);
        ConfirmDialogFragment fragment = new ConfirmDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok:
                proceed();
                break;
            case R.id.btn_cancel:
                break;
        }

        dismiss();
    }

    private void proceed(){
        Intent intent = new Intent();
        intent.putExtra(RESULT,true);
        getActivity().setIntent(intent);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,getActivity().getIntent());
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
