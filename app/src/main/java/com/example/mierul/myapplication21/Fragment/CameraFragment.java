package com.example.mierul.myapplication21.Fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mierul.myapplication21.Base.BaseFragment;

import java.util.Arrays;

/**
 * Created by Hexa-Amierul.Japri on 8/6/2017.
 */

public class CameraFragment extends BaseFragment {

    private String[] permissionType = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,};

    private int REQUEST_CAMERA = 1000;
    private int REQUEST_GALLERY= 1001;
    private int REQUEST_PERMISSION_SETTING = 1002;
    private int PERMISSION_GRANTED = 2000;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(isPermissionGranted()){
            //choose camera or gallery
            showOptionCameraGallery();
        } else {
            checkPermission();
        }

    }

    private void showOptionCameraGallery() {
        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
        ad.setTitle("Photo");
        CharSequence[] list = {"Camera", "Gallery"};
        ad.setSingleChoiceItems(list, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent;

                switch (i){
                    case 0:
                        intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        startActivityForResult(intent, REQUEST_CAMERA);
                        dialogInterface.dismiss();
                        break;
                    case 1:
                        intent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(
                                Intent.createChooser(intent, "Select Image"),
                                REQUEST_GALLERY);
                        dialogInterface.dismiss();
                        break;
                }
            }
        }).show();
    }

    private void checkPermission() {

        if(shouldShowRequestPermissionRationale(permissionType[0])
                ||shouldShowRequestPermissionRationale(permissionType[1])){

            showAskPermission();
        } else {
            //go setting
            goToSetting();

        }
    }

    private void showAskPermission() {
        alertUserDialog("Permission", "Need user permission to continue.", new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                requestPermissions(permissionType, PERMISSION_GRANTED);
            }
        });
    }

    public void goToSetting(){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);

    }

    public boolean isPermissionGranted() {
        boolean permissionGranted;

        if(ContextCompat.checkSelfPermission(getContext(), permissionType[0]) == PackageManager.PERMISSION_GRANTED
                &&ContextCompat.checkSelfPermission(getContext(), permissionType[1]) == PackageManager.PERMISSION_GRANTED){

            permissionGranted = true;
        } else {
            permissionGranted = false;
        }

        return permissionGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean finish = false;

        for (int result : grantResults ){
            if(result == PackageManager.PERMISSION_GRANTED){
                finish = true;
            } else {
                finish = false;
                break;
            }
        }

        if(finish){
            showOptionCameraGallery();
        } else {
            //show dialog warning
            //or back
            previousFragment();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PERMISSION_SETTING) {
            boolean finish = false;

            for(String permission : permissionType){
                if (ContextCompat.checkSelfPermission(getContext(),permission) == PackageManager.PERMISSION_GRANTED) {
                    //Got Permission
                    finish = true;
                } else {
                    //didnt get all permission
                    finish = false;
                    break;
                }
            }

            if(finish){
                showOptionCameraGallery();
            } else {
                //show ask permission
                goToSetting();
            }
        }
    }
}
