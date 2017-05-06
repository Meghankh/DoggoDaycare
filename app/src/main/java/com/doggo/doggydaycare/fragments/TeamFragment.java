package com.doggo.doggydaycare.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.doggo.doggydaycare.R;
import com.doggo.doggydaycare.data.Dog;

import java.util.ArrayList;

/**
 * Created by Meghan on 2/16/2017.
 */
public class TeamFragment extends Fragment
{
    public static final String TAG_TEAM_FRAGMENT = "team_fragment";
    private ArrayList<Dog> members;
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

    private class TeamMemberArrayAdapter extends ArrayAdapter<Dog>
    {
        private final Context context;
        private final ArrayList<Dog> members;
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
            TextView age = (TextView) rowView.findViewById(R.id.age);
            TextView gender = (TextView) rowView.findViewById(R.id.gender);
            TextView weight = (TextView) rowView.findViewById(R.id.weight);

            name.setText(members.get(position).getName()+"");
            age.setText(members.get(position).getAge()+" years");
            gender.setText(members.get(position).getGender()+"");
            weight.setText(members.get(position).getWeight()+" lbs");

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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        int dogCount = Integer.parseInt(prefs.getString("dogCount", ""));

        members = new ArrayList<Dog>();

        for (int i = 1; i <= dogCount; i++)
        {
            String array[] = prefs.getString("dog" + i, "").split(":");
            members.add(new Dog(array[0], Integer.parseInt(array[1]), Double.parseDouble(array[2]), array[3]));
        }

        memberList = (ListView)view.findViewById(R.id.team_members);
        teamMemberListAdapter= new TeamMemberArrayAdapter(getActivity(), R.layout.team_member_list_view_layout, members);
        memberList.setAdapter(teamMemberListAdapter);
    }
}

