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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.doggo.doggydaycare.R;
import com.doggo.doggydaycare.interfaces.HomeScreenInteraction;

/**
 * Created by Meghan on 2/16/2017.
 */
public class HomeScreenFragment extends Fragment implements View.OnClickListener
{
    public static final String TAG_HOME_FRAGMENT = "home_fragment";
    private TextView mysteps, teamsteps, myrank,teamrank;
    private ProgressBar mygoal, teamgoal;
    private HomeScreenInteraction activity;
    private ImageView teamFragment, myStepsFragment, teamsRankFragment;

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

        mysteps = (TextView)view.findViewById(R.id.mysteps);
        teamsteps = (TextView)view.findViewById(R.id.teamsteps);
        myrank = (TextView)view.findViewById(R.id.myrank);
        teamrank = (TextView)view.findViewById(R.id.teamrank);

        teamFragment = (ImageView)view.findViewById(R.id.teamFragment);
        myStepsFragment = (ImageView)view.findViewById(R.id.myStepsFragment);
        teamsRankFragment = (ImageView)view.findViewById(R.id.teamsRankFragment);

        mygoal = (ProgressBar)view.findViewById(R.id.mygoal);
        teamgoal = (ProgressBar)view.findViewById(R.id.teamgoal);

        teamFragment.setOnClickListener(this);
        myStepsFragment.setOnClickListener(this);
        teamsRankFragment.setOnClickListener(this);

        mygoal.setMax(100);
        mygoal.setProgress(35);
        teamgoal.setMax(100);
        teamgoal.setProgress(49);

        mysteps.setText(PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getFloat("steps",0)+" steps");
        teamsteps.setText("32,343 steps");
        myrank.setText("2/4");
        teamrank.setText("4/14");

        return view;
    }

    @Override
    public void onClick(View v)
    {
        Log.d("doggo", "view clicked " + v.getId());
        if (v.equals(teamFragment))
        {
            activity.changeFragment(TeamFragment.TAG_TEAM_FRAGMENT);
        }
        if (v.equals(myStepsFragment))
        {
            activity.changeFragment(MyStepsFragment.TAG_MY_STEPS_FRAGMENT);
        }
        if (v.equals(teamsRankFragment))
        {
            Log.d("doggo", "teamsrank");
            activity.changeFragment(TeamsRankFragment.TAG_TEAM_RANK_FRAGMENT);
        }
    }
}

