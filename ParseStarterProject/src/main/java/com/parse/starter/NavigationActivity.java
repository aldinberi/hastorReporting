package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import com.parse.ParseUser;



public class NavigationActivity extends AppCompatActivity {

    private ViewPager viewPager;


    public void onClickChange(View view){
        Intent intent = new Intent(getApplicationContext(), UpdateProfile.class);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ReportList());
        adapter.addFragment(new UserSettings());
        viewPager.setAdapter(adapter);

    }

    public void startAddReport(View view){
        Intent intent = new Intent(getApplicationContext(), AddingReport.class);
        startActivity(intent);
    }

    public void changePassword(View view){
        Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
        startActivity(intent);
    }

    public void logout(View view){
        ParseUser.logOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_reports:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_userSettings:
                    viewPager.setCurrentItem(1);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        viewPager = findViewById(R.id.viewPager);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setupViewPager(viewPager);

    }

}
