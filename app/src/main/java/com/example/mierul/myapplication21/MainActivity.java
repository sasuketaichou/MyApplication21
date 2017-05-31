package com.example.mierul.myapplication21;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.mierul.myapplication21.Base.BaseActivity;
import com.example.mierul.myapplication21.Fragment.FirstFragment;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();
    }

    private void initFragment() {
        FirstFragment firstFragment = new FirstFragment();
        //add to custom stack
        FragmentStack.addStack(firstFragment);
        //set a container
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.root_main_frame, firstFragment);
        transaction
                //.addToBackStack(null)
                .commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                previousFragment();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void previousFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction
                .setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right)
                .replace(R.id.root_main_frame,FragmentStack.getPrevious())
                .commit();
    }

    @Override
    public void onBackPressed() {

        if (checkDrawer()){
            ((FirstFragment)(FragmentStack.getFirstFragment())).closedDrawer();
        }else if(!FragmentStack.isEmpty()) {
            previousFragment();
        }else{
            super.onBackPressed();
        }
    }

    private boolean checkDrawer() {
        return ((FirstFragment)(FragmentStack.getFirstFragment())).isDrawerOpen();
    }
}
