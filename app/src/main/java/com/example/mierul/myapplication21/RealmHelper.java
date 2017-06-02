package com.example.mierul.myapplication21;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Hexa-Amierul.Japri on 22/5/2017.
 */

public class RealmHelper {
    private Context context;
    private Realm realm = null;
    private RealmConfiguration realmConfig;

    public RealmHelper(Context context) {
        this.context = context;
    }

    private RealmConfiguration getRealmConfig(){
        if (realmConfig == null)
            Realm.init(context);
            realmConfig = new RealmConfiguration.Builder()
                    .schemaVersion(0)
                    .deleteRealmIfMigrationNeeded()
                    .build();

        return realmConfig;
    }

    public Realm getRealm(){
        RealmConfiguration config = getRealmConfig();
        return realm == null ? Realm.getInstance(config):realm;
    }

    public void saveOrder(OrderForm orderForm){

        Realm realm = getRealm();
        realm.beginTransaction();

        realm.copyToRealmOrUpdate(orderForm);

        realm.commitTransaction();

    }

    public OrderForm getOrder(String id){

        Realm realm = getRealm();
        OrderForm resultForm = realm.where(OrderForm.class)
                .equalTo("id",id)
                .findFirst();

        OrderForm form = new OrderForm();

        if(resultForm != null){
            form.setId(resultForm.getId());
            form.setAddress(resultForm.getMapAddress());
            form.setNote(resultForm.getNote());
        } else {
            form.setId(id);
        }



        return form;
    }
}
