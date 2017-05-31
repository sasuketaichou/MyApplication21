package com.example.mierul.myapplication21.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.mierul.myapplication21.Adapter.CheckoutAdapter;
import com.example.mierul.myapplication21.Base.BaseFragment;
import com.example.mierul.myapplication21.Constant;
import com.example.mierul.myapplication21.FirebaseHelper;
import com.example.mierul.myapplication21.Event.FirebaseListEvent;
import com.example.mierul.myapplication21.Model.CheckoutModel;
import com.example.mierul.myapplication21.Model.OrdersDetailsModel;
import com.example.mierul.myapplication21.Model.ProductUrlPictureModel;
import com.example.mierul.myapplication21.R;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hexa-Amierul.Japri on 17/4/2017.
 */

public class CheckoutFragment extends BaseFragment implements View.OnClickListener {
    private FirebaseHelper helper;
    private List<CheckoutModel> item;
    List<Map<String,String>> removeKey;
    List<Integer> removePosition;
    private CheckoutAdapter adapter;
    private int checkUpdate;

    private final String TAG = CheckoutFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        item = new ArrayList<>();
        if(removeKey == null){
            removeKey = new ArrayList<>();
        } else {
            removeKey.clear();
        }

        if(removePosition == null){
            removePosition = new ArrayList<>();
        } else {
            removePosition.clear();
        }
        helper = new FirebaseHelper();
        //trigger get order
        helper.getOrder();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout,container,false);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.checkOutRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CheckoutAdapter(getContext(),item);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),layoutManager.getOrientation()));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchBack());
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    private ItemTouchHelper.Callback simpleItemTouchBack() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    //show confirm dialog
                    confirmDel(position);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap bitmap;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                    Paint p = new Paint();

                    if(dX < 0){
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.delete);
                        bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        drawable.draw(canvas);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(bitmap,null,icon_dest,p);

                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        return simpleCallback;
    }

    private void confirmDel(int position) {

        Map<String,String> key = new HashMap<>();
        key.put(Constant.NODE_ORDKEY.getNode(),item.get(position).ordKey);
        key.put(Constant.NODE_USRORDKEY.getNode(),item.get(position).usrOrdKey);
        removeKey.add(key);
        removePosition.add(position);

        //show dialog here
        snackBarWithMessageAndListener("Confirm to delete " + removeKey.size() + " item?",
                "delete",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        helper.removeOrderByKey(removeKey);
                        for(Integer remove: removePosition){
                            adapter.removeItem(remove);
                        }
                        clearKey();
                    }
                });
    }

    private void clearKey() {
        //for every success transaction/click
        removeKey.clear();
        removePosition.clear();
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

        if(obj instanceof CheckoutModel) {
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
                    if(pModel.key.equals(model.picKey)){
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

        for(CheckoutModel model : (List<CheckoutModel>)event.getList()){
            item.add(model);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.checkoutfragment_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                for(Integer remove: removePosition){
                    adapter.notifyItemChanged(remove);
                }
                clearKey();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
