package com.example.msoohyun88.recyclinghelper;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.msoohyun88.recyclinghelper.database.Item;
import com.example.msoohyun88.recyclinghelper.database.ItemsDAO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ProgressFragment mProgressFragment;
    private ScheduleFragment mScheduleFragment;
    private SearchFragment mSearchFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:

                    if(mProgressFragment == null){
                        mProgressFragment = new ProgressFragment();
                    }

                    loadFragment(mProgressFragment);
                    return true;
                case R.id.navigation_dashboard:

                    if(mScheduleFragment == null){
                        mScheduleFragment = new ScheduleFragment();
                    }

                    loadFragment(mScheduleFragment);

                    return true;
              case R.id.navigation_notifications:

                  if(mSearchFragment == null){
                      mSearchFragment = new SearchFragment();
                  }

                  loadFragment(mSearchFragment);
                
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mProgressFragment = new ProgressFragment();
        loadFragment(mProgressFragment);
    }

    @Override
    protected void onStart(){
        super.onStart();

    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
