package com.example.mierul.myapplication21.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mierul.myapplication21.Model.ProductUrlPictureModel;
import com.example.mierul.myapplication21.R;

import java.util.List;

/**
 * Created by mierul on 3/16/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<ProductUrlPictureModel> item;
    private Context context;

    public ProductAdapter(Context context,List<ProductUrlPictureModel> item) {
        this.item = item;
        this.context = context;
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_view,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {

        ProductUrlPictureModel model = item.get(position);

        TextView textView = holder.name_product;
        textView.setText("empty");
        ImageView image = holder.pict_product;

        //set default image
        //image.setImageResource(R.drawable.default_thumbnail);

        Glide.with(context)
                .load(model.pushId1)
                .into(image);
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name_product;
        ImageView pict_product;

        public ViewHolder(View itemView) {
            super(itemView);

            name_product = (TextView) itemView.findViewById(R.id.product_name);
            pict_product = (ImageView) itemView.findViewById(R.id.product_pic);
        }
    }
}
