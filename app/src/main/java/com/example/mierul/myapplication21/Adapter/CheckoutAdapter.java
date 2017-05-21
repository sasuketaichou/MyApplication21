package com.example.mierul.myapplication21.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mierul.myapplication21.Model.CheckoutModel;
import com.example.mierul.myapplication21.Model.OrdersDetailsModel;
import com.example.mierul.myapplication21.R;

import java.util.List;

/**
 * Created by mierul on 5/21/2017.
 */

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {
    List<CheckoutModel> item;
    Context context;

    public CheckoutAdapter(Context context, List<CheckoutModel> item) {
        this.item = item;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.checkout_view,parent,false);
        CheckoutAdapter.ViewHolder viewHolder = new CheckoutAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CheckoutModel model = item.get(position);

        ImageView imageView = holder.imageView;

        Glide.with(context)
                .load(model.url)
                .into(imageView);

        TextView address = holder.address;
        //Todo edit model to get address
        address.setText(model.address);

        TextView order = holder.order;
        order.setText(model.numOrder);
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView order;
        TextView address;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.checkOut_iv);
            order = (TextView)itemView.findViewById(R.id.checkOut_tv_numOrder);
            address = (TextView)itemView.findViewById(R.id.checkOut_tv_user_address);
        }
    }
}
