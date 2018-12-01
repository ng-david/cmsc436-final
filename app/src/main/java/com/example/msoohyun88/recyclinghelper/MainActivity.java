package com.example.msoohyun88.recyclinghelper;

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


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    ProgressFragment progress = new ProgressFragment();
                    loadFragment(progress);
                    return true;
                case R.id.navigation_dashboard:
                    ScheduleFragment schedule = new ScheduleFragment();
                    loadFragment(schedule);
                    return true;
                case R.id.navigation_notifications:
                    ItemFragment list = new ItemFragment();
                    loadFragment(list);
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
    }

    @Override
    protected void onStart(){
        super.onStart();

        // NOTE: Example of initializing database access object
        ItemsDAO db = new ItemsDAO();
        // NOTE: Example of getting information from it
        ArrayList<Item> recycleList = db.getRecycleList();

    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
