package com.example.mierul.myapplication21.Fragment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mierul.myapplication21.Adapter.CheckoutAdapter;
import com.example.mierul.myapplication21.AnimationDecoratorHelper;
import com.example.mierul.myapplication21.Base.BaseFragment;
import com.example.mierul.myapplication21.FirebaseHelper;
import com.example.mierul.myapplication21.Event.FirebaseListEvent;
import com.example.mierul.myapplication21.Model.CheckoutModel;
import com.example.mierul.myapplication21.Model.OrdersDetailsModel;
import com.example.mierul.myapplication21.Model.ProductUrlPictureModel;
import com.example.mierul.myapplication21.R;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hexa-Amierul.Japri on 17/4/2017.
 */

public class CheckoutFragment extends BaseFragment implements View.OnClickListener {
    private FirebaseHelper helper;
    private List<CheckoutModel> item;
    private CheckoutAdapter adapter;
    private int checkUpdate;
    private  RecyclerView recyclerView;

    private final String TAG = CheckoutFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = new ArrayList<>();
        helper = new FirebaseHelper();
        //trigger get order
        helper.getOrder();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout,container,false);

        recyclerView = (RecyclerView)view.findViewById(R.id.checkOutRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CheckoutAdapter(getContext(),item);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),layoutManager.getOrientation()));

        //recyclerView.addItemDecoration(setUpAnimationDecoratorHelper());
        recyclerView.addItemDecoration(new AnimationDecoratorHelper());
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(setUpItemTouchHelper());
        mItemTouchHelper.attachToRecyclerView(recyclerView);


        return view;
    }

    private ItemTouchHelper.SimpleCallback setUpItemTouchHelper() {

        /**
         * This is the standard support library way of implementing "swipe to delete" feature. You can do custom drawing in onChildDraw method
         * but whatever you draw will disappear once the swipe is over, and while the items are animating to their new position the recycler view
         * background will be visible. That is rarely an desired effect.
         */

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(getContext(), R.drawable.ic_clear_24dp);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                CheckoutAdapter testAdapter = (CheckoutAdapter) recyclerView.getAdapter();
                if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                CheckoutAdapter adapter = (CheckoutAdapter) recyclerView.getAdapter();
                boolean undoOn = adapter.isUndoOn();
                if (undoOn) {
                    adapter.pendingRemoval(swipedPosition);
                } else {
                    adapter.remove(swipedPosition);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };

        return simpleItemTouchCallback;
    }

    public static CheckoutFragment newInstance(String address) {
        Bundle args = new Bundle();
        CheckoutFragment fragment = new CheckoutFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case android.R.id.home:
                break;
        }
    }

    @Subscribe
    public void FirebaseListener(FirebaseListEvent event){

        Object obj = event.getList().get(0);

        if(obj instanceof OrdersDetailsModel) {
            int needUpdate = event.getList().size();

            if (checkUpdate == 0 || needUpdate > checkUpdate) {
                checkUpdate = needUpdate;
                refreshData(event);
                //trigger get picture
                helper.getProductPicture();
            }
        } else if(obj instanceof ProductUrlPictureModel){
            List<ProductUrlPictureModel> list = (List<ProductUrlPictureModel>)event.getList();

            for(CheckoutModel model: item){
                for (ProductUrlPictureModel pModel : list){
                    if(pModel.key.equals(model.key)){
                        model.url = pModel.image_1;
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void refreshData(FirebaseListEvent event){
        //clear item to avoid duplication
        if(!item.isEmpty()){
            item.clear();
        }

        for(OrdersDetailsModel model : (List<OrdersDetailsModel>)event.getList()){

            String productName = model.productName;
            String numOrder = model.numOrder;
            String key = model.key;
            String address = model.productAddress;
            String note = model.productNote;
            String url = "";

            CheckoutModel checkoutModel = new CheckoutModel(productName,
                    numOrder,
                    key,
                    url,
                    address,
                    note);

            item.add(checkoutModel);
        }
    }
}
