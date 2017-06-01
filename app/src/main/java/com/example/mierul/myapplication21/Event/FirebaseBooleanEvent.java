package com.example.mierul.myapplication21.Event;

/**
 * Created by Hexa-Amierul.Japri on 26/4/2017.
 */

public class FirebaseBooleanEvent {
    private boolean result;
    private String message;

    public FirebaseBooleanEvent(boolean result){
        this.result = result;
    }

    public boolean getResult() {
        return result;
    }

    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return message;
    }



}
