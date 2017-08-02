package com.example.mierul.myapplication21.Fragment;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mierul.myapplication21.Adapter.ProfileAdapter;
import com.example.mierul.myapplication21.Base.BaseFragment;
import com.example.mierul.myapplication21.FirebaseHelper;
import com.example.mierul.myapplication21.Model.ProfileDetailsModel;
import com.example.mierul.myapplication21.Model.ProfileFirebaseModel;
import com.example.mierul.myapplication21.R;

import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.ExecutionException;

/**
 * Created by Hexa-Amierul.Japri on 4/5/2017.
 */

public class ProfileFragment extends BaseFragment implements View.OnClickListener, ProfileAdapter.OnProfileAdapterListener {
    private FirebaseHelper helper;
    private String name;
    private String email;
    private String url;
    private ProfileAdapter adapter;

    private ImageView userPhoto;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        initView(view);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.profileRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProfileAdapter(new ProfileDetailsModel());
        adapter.setProfileAdapterListener(this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void initView(View view) {
        view.findViewById(R.id.btn_signout).setOnClickListener(this);

        //for future use
        TextView userDisplayName = (TextView) view.findViewById(R.id.user_profile_name);
        TextView userEmail = (TextView) view.findViewById(R.id.user_profile_email);

        userDisplayName.setText(name);
        userEmail.setText(email);

        userPhoto = (ImageView)view.findViewById(R.id.user_profile_photo);
        userPhoto.setOnClickListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(helper == null){
            helper = new FirebaseHelper();
        }

        if(helper.isLogin()){
            //trigger getDetails to get data
            helper.getDetails();

            //get display name and email
            ProfileFirebaseModel model =helper.getProfile();
            name = model.getDisplayName();
            email = model.getEmail();
        }
    }

    public static ProfileFragment newInstance(ProfileFirebaseModel model) {

        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("username",model.getDisplayName());
        args.putString("email",model.getEmail());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_signout:
                helper.sign_out();
                previousFragment();
                break;
            case R.id.user_profile_photo:
               //open camera
                if(url.isEmpty()){
                    replaceFragment(new CameraFragment());
                } else {
                    replaceFragment(CameraFragment.newInstance(url));
                }

                break;
        }
    }

    @Subscribe
    public void FirebaseHelperListener(ProfileDetailsModel model){
        //update ui after finish loading data

        //TODO diminish model
        adapter.refresh(model);
        url = model.url;

        if(!url.isEmpty()){
            new ImageLoader().execute(url);
        }

    }

    @Override
    public void onItemClick(int position) {
        replaceFragment(EditProfileFragment.newInstance(position));
    }

    private class ImageLoader extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return getBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            RoundedBitmapDrawable roundDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
            roundDrawable.setCircular(true);
            userPhoto.setImageDrawable(roundDrawable);
        }

        private Bitmap getBitmap(String url){
            Bitmap bitmap = null;

            try {
                bitmap = Glide.with(getActivity()).load(url).asBitmap().into(-1,-1).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            return bitmap;
        }
    }
}
