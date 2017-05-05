package com.doggo.doggydaycare.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.doggo.doggydaycare.R;

public class MyStepsFragment extends Fragment
{
    public static final String TAG_MY_STEPS_FRAGMENT = "my_info_fragment";
    private CalendarView myCalender;

    public MyStepsFragment()
    {
        // Required empty public constructor
    }

    public static MyStepsFragment newInstance()
    {
        return new MyStepsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        Log.d("doggo:", "Messages Fragment ON_CREATE_VIEW()");
        View view = inflater.inflate(R.layout.fragment_mysteps, container, false);

        myCalender = (CalendarView) view.findViewById(R.id.calendarView);

        return view;
    }
}
