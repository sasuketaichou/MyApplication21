package com.example.mierul.myapplication21.Fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mierul.myapplication21.Base.BaseFragment;
import com.example.mierul.myapplication21.FirebaseHelper;
import com.example.mierul.myapplication21.Event.FirebaseListEvent;
import com.example.mierul.myapplication21.GridSpacingItemDecoration;
import com.example.mierul.myapplication21.ItemClickSupport;
import com.example.mierul.myapplication21.Model.ProductUrlPictureModel;
import com.example.mierul.myapplication21.Model.ProfileFirebaseModel;
import com.example.mierul.myapplication21.R;
import com.example.mierul.myapplication21.Adapter.ProductAdapter;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mierul on 3/14/2017.
 */
public class FirstFragment extends BaseFragment implements ItemClickSupport.OnItemClickListener {
    private DrawerLayout drawer;
    private List<ProductUrlPictureModel> item;
    private FirebaseHelper helper;
    private ProductAdapter adapter;
    private int checkUpdate;

    private static final String TAG = "FirstFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        item = new ArrayList<>();
        helper = new FirebaseHelper();
        //get picture Url
        helper.getProductPicture();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first,container,false);

        int numColumns = 2;

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.productRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),numColumns));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(numColumns, dpToPx(15), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ProductAdapter(getContext(),item);
        recyclerView.setAdapter(adapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(this);

        //TODO to add icon in drawer
        //change name, pic and email in drawer
        //change background drawer

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        drawer = (DrawerLayout)view.findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(),drawer,getToolbar(),
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)view.findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);

        View headerLayout = navigationView.getHeaderView(0);
        setupHeaderLayout(headerLayout);

    }

    //Converting dp to pixel
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void setupHeaderLayout(View headerLayout) {

        final boolean auth = helper.isLogin();
        final ProfileFirebaseModel model = helper.getProfile();

        if(auth){
            headerLayout.findViewById(R.id.navigation_header_iv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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

            ((TextView)headerLayout.findViewById(R.id.navigation_header_username_tv)).setText(model.getDisplayName());
            ((TextView)headerLayout.findViewById(R.id.navigation_header_email_tv)).setText(model.getEmail());
        }


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
                fragmentClass = NavFirst.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = NavSecond.class;
                break;
            case R.id.nav_checkout_fragment:
                fragmentClass = CheckoutFragment.class;
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

        ProductUrlPictureModel model = item.get(position);
        String[] url = model.toArray();
        String key = model.getKey();
        replaceFragment(SecondFragment.newInstance(url,key));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        int first_menu;

        if(helper.isLogin()){
            first_menu = R.menu.firstfragment_menu_logout;
        } else {
            first_menu = R.menu.firstfragment_menu_login;
        }

        inflater.inflate(first_menu,menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_login:
                int type = 1;
                replaceFragment(LoginFragment.newInstance(type));
                break;
            case R.id.action_logout:
                helper.sign_out();
                //refresh menu
                getActivity().invalidateOptionsMenu();
                snackBarToToast("Logout");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void closedDrawer(){
        drawer.closeDrawer(Gravity.START);
    }

    public boolean isDrawerOpen(){
        return drawer.isDrawerOpen(Gravity.START);
    }

    @Subscribe
    public void FirebaseListener(FirebaseListEvent event){

        int needUpdate = event.getList().size();

        if(checkUpdate ==0 || needUpdate>checkUpdate ){
            checkUpdate = needUpdate;
            refreshData(event);
        }
    }

    private void refreshData(FirebaseListEvent event){
        //clear item to avoid duplication
        if(!item.isEmpty()){
            item.clear();
        }

        for(ProductUrlPictureModel model : (List<ProductUrlPictureModel>)event.getList()){
            item.add(model);
        }
        adapter.notifyDataSetChanged();
    }
}
