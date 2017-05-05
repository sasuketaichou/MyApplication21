package com.example.mierul.myapplication21.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mierul.myapplication21.Model.ProductModel;
import com.example.mierul.myapplication21.R;

import java.util.List;

/**
 * Created by mierul on 3/16/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<ProductModel> item;

    public ProductAdapter(List<ProductModel> item) {
        this.item = item;
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

        ProductModel model = item.get(position);

        TextView textView = holder.name_product;
        textView.setText(model.getName());
        ImageView image = holder.pict_product;
        image.setImageBitmap(model.getPhoto());
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
