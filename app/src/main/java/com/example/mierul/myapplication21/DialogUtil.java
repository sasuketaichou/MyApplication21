package com.example.mierul.myapplication21;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Hexa-Amierul.Japri on 31/7/2017.
 */

public class DialogUtil {

    public static void alertUserDialog(Context context, String title, String message, DialogInterface.OnClickListener positiveListener,DialogInterface.OnClickListener negativeListener){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton(android.R.string.ok,positiveListener);
        builder.setNegativeButton(android.R.string.cancel,negativeListener);
        builder.setCancelable(false);

        builder.show();
    }
}
