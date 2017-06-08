package com.example.mierul.myapplication21.Fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mierul.myapplication21.Base.BaseFragment;

/**
 * Created by Hexa-Amierul.Japri on 8/6/2017.
 */

public class CameraFragment extends BaseFragment {

    private String[] permissionType = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private int REQUEST_CAMERA = 1000;
    private int REQUEST_GALLERY= 1001;
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
            checkPermission();

        } else {
            //choose camera or gallery
            showOptionCameraGallery();
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

        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),permissionType[0])
                ||ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),permissionType[1])
                ||ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),permissionType[2])){

            //show message
            showRationale();
        } else {
            //go setting
        }
    }

    private void showRationale() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Permissions");
        builder.setMessage("This app need permissions.");
        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(getActivity(),permissionType,PERMISSION_GRANTED);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public boolean isPermissionGranted() {
        boolean permissionGranted;

        if(ActivityCompat.checkSelfPermission(getActivity(), permissionType[0]) == PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(getActivity(), permissionType[1]) == PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(getActivity(), permissionType[2]) == PackageManager.PERMISSION_GRANTED){

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
                break;
            }
        }

        if(finish){
            

        }
    }
}
