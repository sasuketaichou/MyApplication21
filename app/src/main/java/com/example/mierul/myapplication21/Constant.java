package com.example.mierul.myapplication21;

/**
 * Created by Hexa-Amierul.Japri on 18/5/2017.
 */

public enum Constant {

    //title
    PROFILE_NAME,
    PROFILE_ADDRESS,
    PROFILE_EMAIL,
    PROFILE_CONTACT,

    //child node
    NODE_NAME,
    NODE_ADDRESS,
    NODE_EMAIL,
    NODE_CONTACT;

    public String getTitle(){
        switch (this){
            case PROFILE_NAME: return "NAME";
            case PROFILE_ADDRESS: return "ADDRESS";
            case PROFILE_CONTACT: return "CONTACT";
            case PROFILE_EMAIL: return "EMAIL";

            default: return "empty";
        }
    }

    public String getNode(){
        switch (this){
            case NODE_NAME: return "name";
            case NODE_ADDRESS: return "address";
            case NODE_CONTACT: return "contact";
            case NODE_EMAIL: return "email";

            default: return "empty";
        }
    }
}
