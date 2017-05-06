package com.doggo.doggydaycare.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.doggo.doggydaycare.R;
import com.doggo.doggydaycare.data.TodoItem;

import java.util.ArrayList;

public class MyStepsFragment extends Fragment
{
    public static final String TAG_MY_STEPS_FRAGMENT = "my_info_fragment";
    private ArrayList<TodoItem> todolist;
    private ListView doList;
    private TodoArrayAdapter todoListAdapter;

    public MyStepsFragment()
    {
        // Required empty public constructor
    }

    public static MyStepsFragment newInstance()
    {
        return new MyStepsFragment();
    }

    private class TodoArrayAdapter extends ArrayAdapter<TodoItem>
    {
        private final Context context;
        private final ArrayList<TodoItem> todo_list;
        private int id;

        public TodoArrayAdapter(Context context, int id , ArrayList input_list)
        {
            super(context, id, input_list);
            this.context = context;
            this.todo_list = input_list;
            this.id = id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.team_list_view_item, parent, false);

            TextView message = (TextView) rowView.findViewById(R.id.message);
            TextView subject = (TextView) rowView.findViewById(R.id.subject);

            subject.setText(todo_list.get(position).getSubject() + "");
            message.setText(todo_list.get(position).getMessage() + "");

            return rowView;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Log.d("doggo", "create");
        return inflater.inflate(R.layout.fragment_mysteps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        todolist = new ArrayList<TodoItem>();

        todolist.add(new TodoItem("Feed Daisy", "3 cups of tuna"));
        todolist.add(new TodoItem("Tonka and Daisy", "Walk around park"));
        todolist.add(new TodoItem("Pick up poo", "bring a doggy bag"));
        todolist.add(new TodoItem("Give Tonka medicine", "Give two pills"));
        todolist.add(new TodoItem("Massage Daisy", "Watch for injured hip!"));

        doList = (ListView) view.findViewById(R.id.todo_items);
        todoListAdapter = new MyStepsFragment.TodoArrayAdapter(getActivity(), R.layout.team_list_view_item, todolist);
        doList.setAdapter(todoListAdapter);
    }
}
