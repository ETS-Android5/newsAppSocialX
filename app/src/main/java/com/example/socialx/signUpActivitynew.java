package com.example.socialx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.socialx.Adapter.tabAccessAdapter;
import com.google.android.material.tabs.TabLayout;

public class signUpActivitynew extends AppCompatActivity {
    private ViewPager sign_up_sign_in_viewpager;
    private tabAccessAdapter tabAccessAdapter;
    private TabLayout signin_signup_tablayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_activitynew);
        sign_up_sign_in_viewpager=(ViewPager) findViewById(R.id.sign_up_sign_in_viewpager);
        tabAccessAdapter= new tabAccessAdapter(getSupportFragmentManager());
        sign_up_sign_in_viewpager.setAdapter(tabAccessAdapter);
        signin_signup_tablayout = (TabLayout) findViewById(R.id.signin_signup_tablayout);
        signin_signup_tablayout.setupWithViewPager(sign_up_sign_in_viewpager);
    }
}