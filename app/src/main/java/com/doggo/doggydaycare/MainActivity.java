package com.doggo.doggydaycare;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.doggo.doggydaycare.Fragments.CalendarFragment;
import com.doggo.doggydaycare.Fragments.DogProfileFragment;
import com.doggo.doggydaycare.Interfaces.HomeScreenInteraction;

public class MainActivity extends AppCompatActivity implements HomeScreenInteraction{

    private Fragment homeScreenFragment, taskFragment, calendarFragment, dogProfileFragment;

    private SharedPreferences prefs;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

    }

    @Override
    public void changeFragment(String fragment_name) {

        Fragment fragment;
        Class fragmentClass = null;
        if(fragment_name.equals(CalendarFragment.CALENDAR_FRAGMENT)){
            fragmentClass = CalendarFragment.class;

            Log.d("Group Project", "Calendar Fragment Selected");
        }
        else if(fragment_name.equals(DogProfileFragment.DOG_PROFILE_FRAGMENT)){
            fragmentClass = DogProfileFragment.class;

            Log.d("Group Project", "Dog Profile Fragment Selected");
        }


    }
}
