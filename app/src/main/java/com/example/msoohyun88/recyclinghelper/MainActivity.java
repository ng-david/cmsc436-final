package com.example.msoohyun88.recyclinghelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static boolean logged_in = false;
    private static final int LOG_IN = 0;

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

        //if the user hasn't logged in, start the log in activity.
        if (!logged_in) {
            Intent login = new Intent(this, LoginActivity.class);
            startActivityForResult(login, LOG_IN);

            logged_in = true;
            //TODO: get the bundle from activity result of logged_in activity (user info)
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onStart(){
        super.onStart();

        //login here?
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
