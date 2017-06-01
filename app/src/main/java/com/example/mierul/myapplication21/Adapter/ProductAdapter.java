package com.example.mierul.myapplication21.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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

        //TODO give name
        TextView textView = holder.name_product;
        textView.setText("empty");
        ImageView image = holder.pict_product;

        final ProgressBar progressBar = holder.progressBar;

        //set default image
        //image.setImageResource(R.drawable.default_thumbnail);

        Glide.with(context)
                .load(model.image_1)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(image);
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name_product;
        ImageView pict_product;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);

            progressBar = (ProgressBar)itemView.findViewById(R.id.progress_pic);
            name_product = (TextView) itemView.findViewById(R.id.product_name);
            pict_product = (ImageView) itemView.findViewById(R.id.product_pic);
        }
    }
}
