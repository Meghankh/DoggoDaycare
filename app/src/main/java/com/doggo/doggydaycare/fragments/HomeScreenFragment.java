package com.doggo.doggydaycare.fragments;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doggo.doggydaycare.R;
import com.doggo.doggydaycare.interfaces.HomeScreenInteraction;

/**
 * Created by Meghan on 2/16/2017.
 */
public class HomeScreenFragment extends Fragment implements View.OnClickListener
{
    public static final String TAG_HOME_FRAGMENT = "home_fragment";
    private TextView dogLabel, userLabel, calendarLabel;
    private HomeScreenInteraction activity;
    private ImageView dog, user, calendar;

    public static HomeScreenFragment newInstance()
    {
        HomeScreenFragment fragment = new HomeScreenFragment();
        return fragment;
    }

    public HomeScreenFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof HomeScreenInteraction)
        {
            activity = (HomeScreenInteraction) context;

        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement HomeScreenInteraction");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dogLabel = (TextView)view.findViewById(R.id.dogLabel);
        userLabel = (TextView)view.findViewById(R.id.userLabel);
        calendarLabel = (TextView)view.findViewById(R.id.calendarLabel);

        dog = (ImageView)view.findViewById(R.id.dog);
        user = (ImageView)view.findViewById(R.id.user);
        calendar = (ImageView)view.findViewById(R.id.calendar);

        dog.setOnClickListener(this);
        user.setOnClickListener(this);
        calendar.setOnClickListener(this);

        calendarLabel.setText(PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getFloat("steps",0)+" steps");
        dogLabel.setText("View Dogs");
        userLabel.setText("View Profile");

        return view;
    }

    @Override
    public void onClick(View v)
    {
        Log.d("doggo", "view clicked " + v.getId());
        if (v.equals(dog))
        {
            activity.changeFragment(TeamFragment.TAG_TEAM_FRAGMENT);
        }
        if (v.equals(calendar))
        {
            activity.changeFragment(MyStepsFragment.TAG_MY_STEPS_FRAGMENT);
        }
        if (v.equals(user))
        {
            activity.changeFragment(TeamsRankFragment.TAG_TEAM_RANK_FRAGMENT);
        }
    }
}

