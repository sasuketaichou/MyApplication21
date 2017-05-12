package com.example.mierul.myapplication21.Fragment;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mierul.myapplication21.Base.BaseFragment;
import com.example.mierul.myapplication21.FirebaseHelper;
import com.example.mierul.myapplication21.GridDividerItemDecoration;
import com.example.mierul.myapplication21.GridSpacingItemDecoration;
import com.example.mierul.myapplication21.Model.ProductModel;
import com.example.mierul.myapplication21.ItemClickSupport;
import com.example.mierul.myapplication21.Model.ProfileFirebaseModel;
import com.example.mierul.myapplication21.R;
import com.example.mierul.myapplication21.Adapter.ProductAdapter;
import com.example.mierul.myapplication21.SimpleDividerItemDecoration;
import com.example.mierul.myapplication21.SpacesItemDecoration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mierul on 3/14/2017.
 */
public class FirstFragment extends BaseFragment implements ItemClickSupport.OnItemClickListener {
    private DrawerLayout drawer;
    private List<ProductModel> item;
    private FirebaseHelper helper;

    private static final String TAG = "FirstFragment";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        setHasOptionsMenu(true);

        helper = new FirebaseHelper();
    }

    private void getData() {

        item = new ArrayList<>();
        AssetManager assetmanager = getActivity().getAssets();

        try {

            String[] imgPath = assetmanager.list("img");
            for(String path: imgPath){
                InputStream is = assetmanager.open("img/"+path);
                Bitmap bitmap = BitmapFactory.decodeStream(is);

                item.add(new ProductModel(path,bitmap));
            }

        } catch (Exception e){
            Log.e(TAG,"getData",e);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first,container,false);

        initToolbar(view,getClass().getSimpleName(),false);

        drawer = (DrawerLayout)view.findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(),drawer,getToolbar(),
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)view.findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);

        int numColumns = 2;
//        Drawable horizontalDivider = ContextCompat.getDrawable(getContext(), R.drawable.divider_horizontal);
//        Drawable verticalDivider = ContextCompat.getDrawable(getContext(), R.drawable.divider_vertical);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.productRecyclerView);
//        StaggeredGridLayoutManager gridLayoutManager =
//                new StaggeredGridLayoutManager(numColumns, StaggeredGridLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),numColumns));
        //recyclerView.addItemDecoration(new GridSpacingItemDecoration(numColumns, dpToPx(10), true));
        //recyclerView.addItemDecoration(new GridDividerItemDecoration(horizontalDivider, verticalDivider, numColumns));
        //recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        //recyclerView.addItemDecoration(new SpacesItemDecoration(numColumns));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ProductAdapter adapter = new ProductAdapter(item);
        recyclerView.setAdapter(adapter);


        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        setupHeaderLayout(headerLayout);

        return view;
    }

    private void setupHeaderLayout(View headerLayout) {

        View imageHeader =headerLayout.findViewById(R.id.navigation_header_imageView);

        imageHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean auth = helper.isLogin();
                ProfileFirebaseModel model = helper.getProfile();

                if(auth){
                    //profile fragment
                    replaceFragment(ProfileFragment.newInstance(model));
                } else {
                    int type = 1;
                    replaceFragment(LoginFragment.newInstance(type));
                }
                drawer.closeDrawer(Gravity.START);
            }
        });
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return false;
            }
        });
    }

    private void selectDrawerItem(MenuItem item) {

        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;

        int id = item.getItemId();

        switch (id){
            case R.id.nav_first_fragment:
                //transact fragment here
                fragmentClass = NavFirst.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = NavSecond.class;
                break;
            case R.id.nav_third_fragment:
                fragmentClass = NavThird.class;
                break;
        }

        try {
            fragment = (Fragment) (fragmentClass != null ? fragmentClass.newInstance() : null);
        } catch (Exception e) {
            Log.e(TAG,"selectDrawerItem",e);
        }

        replaceFragment(fragment);

        // Highlight the selected item has been done by NavigationView
        item.setChecked(false);
        // Set action bar title
        //toolbar.setTitle(item.getTitle());
        // Close the navigation drawer
        drawer.closeDrawer(Gravity.START);
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

        //this title is for picture path
        String title = item.get(position).getName();
        replaceFragment(SecondFragment.newInstance(title,title));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.firstfragment_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_logout:
                helper.sign_out();
                Toast.makeText(getContext(),"Logout",Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void closedDrawer(){
        drawer.closeDrawer(Gravity.START);
    }

    public boolean isDrawerOpen(){
        return drawer.isDrawerOpen(Gravity.START);
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
