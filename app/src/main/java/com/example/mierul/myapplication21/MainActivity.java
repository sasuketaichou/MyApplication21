package com.example.mierul.myapplication21;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set a container
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.root_main_frame, new RootFragment());
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        Log.v(TAG,"MainActivity : "+getSupportFragmentManager().getBackStackEntryCount());
        switch (id){
            case android.R.id.home:
                Log.v(TAG,"MainActivity home");
//
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FirstFragment home = (FirstFragment) fragmentManager.findFragmentByTag(FirstFragment.class.getSimpleName());
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right)
//                        .replace(R.id.root_main_frame,home)
//                        .commit();
                return false;
        }
        return super.onOptionsItemSelected(item);
    }
}
