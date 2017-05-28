package com.example.mierul.myapplication21;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Hexa-Amierul.Japri on 28/5/2017.
 */

public class ConfirmDialogFragment extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_confirm_dialog,container,false);
        return view;
    }

    public static ConfirmDialogFragment newInstance(){
        Bundle args = new Bundle();
        ConfirmDialogFragment fragment = new ConfirmDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        
    }
}
