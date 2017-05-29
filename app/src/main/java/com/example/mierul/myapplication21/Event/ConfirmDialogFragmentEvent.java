package com.example.mierul.myapplication21.Event;

/**
 * Created by Hexa-Amierul.Japri on 29/5/2017.
 */

public class ConfirmDialogFragmentEvent {
    private boolean result;

    public ConfirmDialogFragmentEvent(boolean result){
        this.result = result;
    }

    public boolean getResult() {
        return result;
    }
}
