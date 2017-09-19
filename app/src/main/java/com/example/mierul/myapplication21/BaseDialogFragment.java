package com.example.mierul.myapplication21;

import android.support.v7.app.AppCompatDialogFragment;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Hexa-Amierul.Japri on 19/9/2017.
 */

public abstract class BaseDialogFragment extends AppCompatDialogFragment {

    private InputMethodManager inputMethodManager;

    private InputMethodManager getInputMethodManager(){
        if(inputMethodManager == null){
            inputMethodManager =(InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        }
        return inputMethodManager;
    }

    public void hideSoftKeyboard(){
        if(getView()!= null){
            getInputMethodManager()
                    .hideSoftInputFromWindow(getView().getRootView().getWindowToken(), 0);
                    //.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    public void showSoftKeyboard(){
        if(getInputMethodManager().isActive()){
            getInputMethodManager()
                    //.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
                    .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }
    }
}
