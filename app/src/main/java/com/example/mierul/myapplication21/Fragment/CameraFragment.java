package com.example.mierul.myapplication21.Fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mierul.myapplication21.Base.BaseFragment;
import com.example.mierul.myapplication21.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

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

    private ImageView profilePhoto;
    private Button saveButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.camera_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(isPermissionGranted()){
                    //choose camera or gallery
                    showOptionCameraGallery();
                } else {
                    checkPermission();
                }
                return false;
            }
        });

        profilePhoto = (ImageView) view.findViewById(R.id.profile_photo);
        saveButton = (Button)view.findViewById(R.id.btn_save);
        saveButton.setEnabled(false);
        saveButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_hold));
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void showOptionCameraGallery() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        CharSequence[] list = {"Camera", "Gallery"};
        builder.setItems(list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent;

                switch (which){
                    case 0:
                        intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        startActivityForResult(intent, REQUEST_CAMERA);
                        break;
                    case 1:
                        intent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(
                                Intent.createChooser(intent, "Select Image"),
                                REQUEST_GALLERY);
                        break;
                }
                dialog.dismiss();
            }
        });

        //Todo build adapter to attach icon with item
        //builder.setAdapter(new DialogImageAdapter(),);
        builder.show();
    }

    private void checkPermission() {

        if(shouldShowRequestPermissionRationale(permissionType[0])
                ||shouldShowRequestPermissionRationale(permissionType[1])){

            showAskPermission();
        } else {
            //go setting
           showAskToSetting();
        }
    }

    private void showAskPermission() {
        alertUserDialog("Permission", "Need user permission to continue.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(permissionType, PERMISSION_GRANTED);
            }
        });
    }

    private void showAskToSetting(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Permission");
        builder.setMessage("Need user permission to continue.");

        builder.setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                goToSetting();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);

        builder.show();
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
                showAskToSetting();
            }
        } else if(requestCode == REQUEST_CAMERA && data != null){
            if(resultCode == RESULT_OK){

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                profilePhoto.setImageBitmap(photo);

                saveButton.setEnabled(true);

            }
        } else if (requestCode == REQUEST_GALLERY && data != null){
            if(resultCode==RESULT_OK){

                Uri imageUri = data.getData();
                InputStream imageStream = null;
                try {
                    imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                profilePhoto.setImageBitmap(selectedImage);

                saveButton.setEnabled(true);
            }
        }
    }


}
