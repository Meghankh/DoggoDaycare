package com.doggo.doggydaycare.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doggo.doggydaycare.R;

/**
 * Created by Meghan on 3/2/2017.
 */
public class TeamsRankFragment extends Fragment
{
    public static final String TAG_TEAM_RANK_FRAGMENT = "dashboard_fragment";

    public TeamsRankFragment()
    {
        // Blank constructor
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    public static TeamsRankFragment newInstance(String param1, String param2)
    {
        return new TeamsRankFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team_rank, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }
}

