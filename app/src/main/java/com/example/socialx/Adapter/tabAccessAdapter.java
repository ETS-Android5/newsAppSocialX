package com.example.socialx.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.socialx.signInFragment;
import com.example.socialx.signUpFragment;

public class tabAccessAdapter extends FragmentPagerAdapter {

    public tabAccessAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                signInFragment signin= new  signInFragment();
                return signin;
            case 1:
                signUpFragment signup= new signUpFragment();
                return signup;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Login";
            case 1:
                return "SignUp";
            default:
                return null;
        }

    }
}
