package com.doggo.doggydaycare.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.doggo.doggydaycare.R;
import com.doggo.doggydaycare.data.TeamMember;

import java.util.ArrayList;

/**
 * Created by Meghan on 2/16/2017.
 */
public class TeamFragment extends Fragment
{
    public static final String TAG_TEAM_FRAGMENT = "team_fragment";
    private ArrayList<TeamMember> members;
    private ListView memberList;
    private TeamMemberArrayAdapter teamMemberListAdapter;

    public static TeamFragment newInstance()
    {
        TeamFragment fragment = new TeamFragment();
        return fragment;
    }

    public TeamFragment()
    {
        // Required empty public constructor
    }

    private class TeamMemberArrayAdapter extends ArrayAdapter<TeamMember>
    {
        private final Context context;
        private final ArrayList<TeamMember> members;
        private int id;

        public TeamMemberArrayAdapter(Context context, int id , ArrayList members)
        {
            super(context, id, members);
            this.context = context;
            this.members = members;
            this.id = id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.team_member_list_view_layout, parent, false);
            TextView name = (TextView) rowView.findViewById(R.id.name);
            TextView steps = (TextView) rowView.findViewById(R.id.steps);
            TextView goal = (TextView) rowView.findViewById(R.id.goal);

            ProgressBar progressBar = (ProgressBar)rowView.findViewById(R.id.progress_goal);

            name.setText(members.get(position).getName()+"'s steps");
            steps.setText(members.get(position).getSteps()+"");
            goal.setText(members.get(position).getGoal()+"");

            progressBar.setProgress(Math.round(((float)members.get(position).getSteps()/(float)members.get(position).getGoal())*100));

            return rowView;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        members = new ArrayList<TeamMember>();

        members.add(new TeamMember("A",10000,20000 ));
        members.add(new TeamMember("B",5000,15000 ));
        members.add(new TeamMember("C",6700,18000 ));
        members.add(new TeamMember("D",1000,3000 ));
        members.add(new TeamMember("E",10000,50000 ));
        members.add(new TeamMember("F",5000,13000 ));

        //int temp=0;
        // for(int i=0;i<6;i++){
        //     temp=0;
        //     try{
        //         temp = (int) prefs.getInt(i+"th",0);
        //
        //     }
        //     catch(Exception e){
        //         temp =0;
        //     }
        //     if(prefs.getString(i+"","0")!="0")
        //         members.add(new TeamMember(prefs.getString(i+"","0"),temp,10000 ));
        //
        // }
        memberList = (ListView)view.findViewById(R.id.team_members);
        teamMemberListAdapter= new TeamMemberArrayAdapter(getActivity(), R.layout.team_member_list_view_layout, members);
        memberList.setAdapter(teamMemberListAdapter);
    }
}

