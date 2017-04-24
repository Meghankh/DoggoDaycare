package com.doggo.doggydaycare.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doggo.doggydaycare.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class dogProfileFragment extends Fragment {


    public dogProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dog_profile, container, false);
    }

}
