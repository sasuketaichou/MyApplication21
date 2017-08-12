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
    private List<CheckoutModel> item;
    private Context context;

    public CheckoutAdapter(Context context, List<CheckoutModel> item) {
        this.item = item;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.checkout_view,parent,false);
        CheckoutAdapter.ViewHolder viewHolder = new CheckoutAdapter.ViewHolder(view);

        //TODO make button place in bottom
        //add onItemLongclick listener to remove order

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
        address.setText(model.address);

        TextView order = holder.order;
        order.setText(model.numOrder);

        TextView title = holder.title;
        title.setText(model.productName);

        TextView total =holder.total;
        total.setText(model.total);
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public void removeItem(int position){
        if(position< getItemCount()){
            item.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, item.size());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView order;
        TextView address;
        TextView title;
        TextView total;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.checkOut_tv_title);
            imageView = (ImageView)itemView.findViewById(R.id.checkOut_iv);
            order = (TextView)itemView.findViewById(R.id.checkOut_tv_numOrder);
            address = (TextView)itemView.findViewById(R.id.checkOut_tv_user_address);
            total = (TextView)itemView.findViewById(R.id.checkOut_tv_total);
        }
    }
}
