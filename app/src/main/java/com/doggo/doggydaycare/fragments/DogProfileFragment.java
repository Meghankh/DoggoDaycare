package com.doggo.doggydaycare.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doggo.doggydaycare.R;

public class DogProfileFragment extends Fragment
{
    public static final String DOG_PROFILE_FRAGMENT= "Dog Profile Fragment";

    public DogProfileFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dog_profile, container, false);
    }
}
