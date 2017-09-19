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
    PRODUCT_ADDRESS,
    PRODUCT_NOTE,

    //child node
    NODE_NAME,
    NODE_ADDRESS,
    NODE_EMAIL,
    NODE_CONTACT,
    NODE_USRORDKEY,
    NODE_ORDKEY,

    //address map
    ADDRESS_ADDRESS,
    ADDRESS_CITY,
    ADDRESS_POSTCODE,
    ADDRESS_COUNTRY;

    public String getTitle(){
        switch (this){
            case PROFILE_NAME: return "NAME";
            case PROFILE_ADDRESS: return "ADDRESS";
            case PROFILE_CONTACT: return "CONTACT";
            case PROFILE_EMAIL: return "EMAIL";
            case PRODUCT_NOTE: return "NOTE";
            case PRODUCT_ADDRESS: return "ADDRESS";

            default: return "";
        }
    }

    public String getNode(){
        switch (this){
            case NODE_NAME: return "name";
            case NODE_ADDRESS: return "address";
            case NODE_CONTACT: return "contact";
            case NODE_EMAIL: return "email";
            case NODE_ORDKEY: return "ordKey";
            case NODE_USRORDKEY: return "usrOrdKey";

            default: return "";
        }
    }

    public String getString(){
        switch (this){
            case ADDRESS_ADDRESS: return "address";
            case ADDRESS_CITY: return "city";
            case ADDRESS_POSTCODE: return "postcode";
            case ADDRESS_COUNTRY: return "country";
            default:
                return "";
        }
    }
}
