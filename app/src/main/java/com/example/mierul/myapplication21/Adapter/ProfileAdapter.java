package com.example.mierul.myapplication21.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mierul.myapplication21.Constant;
import com.example.mierul.myapplication21.Model.ProfileDetailsModel;
import com.example.mierul.myapplication21.Event.ProfileAdapterEvent;
import com.example.mierul.myapplication21.R;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by Hexa-Amierul.Japri on 5/5/2017.
 */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private ProfileDetailsModel item;

    public ProfileAdapter(ProfileDetailsModel profile){
        item = profile;
    }

    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.profile_details,parent,false);
        ProfileAdapter.ViewHolder viewHolder = new ProfileAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProfileAdapter.ViewHolder holder, int position) {

        TextView title = holder.title;
        TextView content = holder.content;
        //ImageView edit = holder.edit;

        Constant mTitle = null;
        String mContent="";
        switch(position){
            case 0:
                mTitle = Constant.PROFILE_NAME;
                mContent = item.name;
                break;
            case 1:
                mTitle = Constant.PROFILE_EMAIL;
                mContent = item.email;
                break;
            case 2:
                mTitle = Constant.PROFILE_ADDRESS;

                if(item.address != null){
                    String address = item.address.get("address");
                    String city =item.address.get("city");
                    String postcode = item.address.get("postcode");
                    String country =  item.address.get("country");
                    String space = " ";

                    mContent = address+space+city+space+postcode+space+country;
            }
                break;
            case 3:
                mTitle = Constant.PROFILE_CONTACT;
                mContent = item.contact;
                break;
        }
        title.setText(mTitle.getTitle());
        content.setText(mContent);

    }

    @Override
    public int getItemCount() {
        //4 view for now, will check by null?
        return 4;
    }

    public void refresh(ProfileDetailsModel model){
        //release previous item
        item = null;
        item = model;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView content;
        ImageView edit;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.tv_profile_title);
            content = (TextView)itemView.findViewById(R.id.tv_profile_content);
            edit = (ImageView)itemView.findViewById(R.id.iv_edit);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new ProfileAdapterEvent(getLayoutPosition()));
        }
    }
}
