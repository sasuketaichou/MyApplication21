package com.example.mierul.myapplication21.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mierul.myapplication21.Model.CheckoutModel;
import com.example.mierul.myapplication21.Model.OrdersDetailsModel;
import com.example.mierul.myapplication21.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mierul on 5/21/2017.
 */

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {
    private List<CheckoutModel> item;
    private List<CheckoutModel> itemsPendingRemoval;
    private Handler handler;
    private Map<CheckoutModel, Runnable> pendingRunnables;
    private Context context;

    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec
    private boolean undoOn;

    public CheckoutAdapter(Context context, List<CheckoutModel> item) {
        this.item = item;
        this.context = context;
        itemsPendingRemoval = new ArrayList<>();
        handler  = new Handler();
        pendingRunnables = new HashMap<>();
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
        ViewHolder viewHolder = holder;
        final CheckoutModel model = item.get(position);

        if(itemsPendingRemoval.contains(model)){
            // we need to show the "undo" state of the row
            viewHolder.itemView.setBackgroundColor(Color.RED);
            viewHolder.title.setVisibility(View.GONE);
            viewHolder.imageView.setVisibility(View.GONE);
            viewHolder.order.setVisibility(View.GONE);
            viewHolder.address.setVisibility(View.GONE);
            viewHolder.order_title.setVisibility(View.GONE);
            viewHolder.address_title.setVisibility(View.GONE);
            Button undoBut = viewHolder.undoButton;
            undoBut.setVisibility(View.VISIBLE);
            undoBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // user wants to undo the removal, let's cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnables.get(model);
                    pendingRunnables.remove(model);
                    if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable);
                    itemsPendingRemoval.remove(model);
                    // this will rebind the row in "normal" state
                    notifyItemChanged(item.indexOf(model));
                }
            });
        } else {
            viewHolder.itemView.setBackgroundColor(Color.WHITE);
            viewHolder.title.setVisibility(View.VISIBLE);
            viewHolder.imageView.setVisibility(View.VISIBLE);
            viewHolder.order.setVisibility(View.VISIBLE);
            viewHolder.address.setVisibility(View.VISIBLE);
            viewHolder.order_title.setVisibility(View.VISIBLE);
            viewHolder.address_title.setVisibility(View.VISIBLE);
            Button undoBut = viewHolder.undoButton;
            undoBut.setVisibility(View.GONE);
            undoBut.setOnClickListener(null);
        }

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
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public boolean isUndoOn() {
        return undoOn;
    }

    public void pendingRemoval(int position) {
        final CheckoutModel model = item.get(position);
        if (!itemsPendingRemoval.contains(model)) {
            itemsPendingRemoval.add(model);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(item.indexOf(model));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(model, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        CheckoutModel model = item.get(position);
        if (itemsPendingRemoval.contains(model)) {
            itemsPendingRemoval.remove(model);
        }
        if (item.contains(model)) {
            item.remove(position);
            notifyItemRemoved(position);
        }
    }

    public boolean isPendingRemoval(int position) {
        CheckoutModel model = item.get(position);
        return itemsPendingRemoval.contains(model);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView order;
        TextView address;
        TextView title;
        TextView address_title;
        TextView order_title;
        Button undoButton;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.checkOut_tv_title);
            imageView = (ImageView)itemView.findViewById(R.id.checkOut_iv);
            order = (TextView)itemView.findViewById(R.id.checkOut_tv_numOrder);
            address = (TextView)itemView.findViewById(R.id.checkOut_tv_user_address);
            undoButton = (Button)itemView.findViewById(R.id.btn_undo);

            address_title =(TextView)itemView.findViewById(R.id.address_title);
            order_title = (TextView)itemView.findViewById(R.id.order_title);
        }
    }
}
