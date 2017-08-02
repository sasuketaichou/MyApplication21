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
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mierul.myapplication21.Base.BaseFragment;
import com.example.mierul.myapplication21.DialogUtil;
import com.example.mierul.myapplication21.Event.FirebaseBooleanEvent;
import com.example.mierul.myapplication21.FirebaseHelper;
import com.example.mierul.myapplication21.R;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Hexa-Amierul.Japri on 8/6/2017.
 */

public class CameraFragment extends BaseFragment {

    private String[] permissionType = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private String FILEPROVIDER = "com.example.mierul.myapplication21.fileprovider";
    private static String TAG = "CameraFragment";

    private int REQUEST_CAMERA = 1000;
    private int REQUEST_GALLERY= 1001;
    private int REQUEST_PERMISSION_SETTING = 1002;
    private int PERMISSION_GRANTED = 2000;

    private ImageView profilePhoto;
    private Button saveButton;

    private FirebaseHelper fHelper;

    private Uri takePhotoUri;
    private String url;

    public static CameraFragment newInstance(String url){
        CameraFragment fragment = new CameraFragment();
        Bundle bundle = new Bundle();
        bundle.putString("URL",url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(fHelper == null){
            fHelper = new FirebaseHelper(getContext());
        }

        if(getArguments() != null){
            url = getArguments().getString("URL");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera,container,false);
        profilePhoto = (ImageView) view.findViewById(R.id.profile_photo);

        if(!url.isEmpty()){
            Glide.with(getActivity()).load(url).into(profilePhoto);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.camera_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPermissionGranted()){
                    //choose camera or gallery
                    showOptionCameraGallery();
                } else {
                    checkPermission();
                }
            }
        });

        saveButton = (Button)view.findViewById(R.id.btn_save);
        disableButton();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(takePhotoUri != null){
                    fHelper.setUserProfileImage(takePhotoUri);
                    takePhotoUri = null;
                }
            }
        });
    }

    private void disableButton(){
        saveButton.setEnabled(false);
        saveButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_hold));
    }

    private void enableButton(){
        saveButton.setEnabled(true);
        saveButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_default));
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
                        takePhotoUri = getPhotoFileUri(photoFileName());
                        intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, takePhotoUri);
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

    private String photoFileName() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss", new Locale("ms","MY","MY")).format(new Date())+".jpg";
    }

    private void checkPermission() {

        if(shouldShowRequestPermissionRationale(permissionType[0])
                ||shouldShowRequestPermissionRationale(permissionType[1])
                ||shouldShowRequestPermissionRationale(permissionType[2])){

            showAskPermission();
        } else {
            //go setting
           showAskToSetting();
        }
    }

    private void showAskPermission() {
        DialogUtil.alertUserDialog(getContext(),"Permission",
                "Need user permission to continue.",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(permissionType, PERMISSION_GRANTED);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    private void showAskToSetting(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Permission");
        builder.setMessage("Need user permission to continue.");

        builder.setPositiveButton("SETTING", new DialogInterface.OnClickListener() {
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
        } else if(requestCode == REQUEST_CAMERA){
            if(resultCode == RESULT_OK){

                if(takePhotoUri != null){
                    profilePhoto.setImageURI(takePhotoUri);
                }
                enableButton();
            } else {
                disableButton();
            }
        } else if (requestCode == REQUEST_GALLERY && data != null){
            if(resultCode==RESULT_OK){

                takePhotoUri = data.getData();

                if(takePhotoUri != null ){
                    profilePhoto.setImageURI(takePhotoUri);
                }
                enableButton();
            } else {
                disableButton();
            }
        }
    }

    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {

            File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),TAG);

            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(TAG, "failed to create directory");
            }

            File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

            // wrap File object into a content provider
            // required for API >= 24
            // See https://guides.codepath.com/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
            return FileProvider.getUriForFile(getActivity(), FILEPROVIDER, file);
        }
        return null;
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Subscribe
    public void FirebaseListener(FirebaseBooleanEvent event){
        if(event.getResult()){
            previousFragment();
        }
    }
}
