package com.example.mierul.myapplication21;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Html;

/**
 * Created by Hexa-Amierul.Japri on 31/7/2017.
 */

public class DialogUtil {

    public static void alertUserDialog(Context context, String title, String message, DialogInterface.OnClickListener positiveListener,DialogInterface.OnClickListener negativeListener){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //https://stackoverflow.com/questions/9935692/how-to-set-part-of-text-to-bold-when-using-alertdialog-setmessage-in-android
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            builder.setTitle(Html.fromHtml("<b>"+title+"</b>",Html.FROM_HTML_MODE_LEGACY));
        } else {
            builder.setTitle(Html.fromHtml("<b>"+title+"</b>"));
        }

        builder.setMessage(message);

        builder.setPositiveButton(android.R.string.ok,positiveListener);
        builder.setNegativeButton(android.R.string.cancel,negativeListener);
        builder.setCancelable(false);

        builder.show();
    }

    public static void alertUserDialog(Context context, String title, String message, DialogInterface.OnClickListener positiveListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            builder.setTitle(Html.fromHtml("<b>"+title+"</b>",Html.FROM_HTML_MODE_LEGACY));
        } else {
            builder.setTitle(Html.fromHtml("<b>"+title+"</b>"));
        }

        builder.setMessage(message);

        builder.setPositiveButton(android.R.string.ok,positiveListener);
        builder.setCancelable(false);

        builder.show();
    }
}
