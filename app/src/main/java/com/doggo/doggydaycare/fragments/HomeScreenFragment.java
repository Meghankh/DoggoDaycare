package com.doggo.doggydaycare.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import com.doggo.doggydaycare.MainActivity;
import com.doggo.doggydaycare.R;
import com.doggo.doggydaycare.interfaces.HomeScreenInteraction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

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

        new AsyncTask<Void, Void, Void>()
        {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            JSONObject data;

            @Override
            protected Void doInBackground(Void... params)
            {
                String mCookie = prefs.getString("sessionid", "");
                String user_id = prefs.getString("user_id", "");

                InputStream is = null;
                try
                {
                    JSONObject credentials = new JSONObject();
                    try
                    {
                        credentials.put("action", "findDogs");
                        credentials.put("user_id", user_id + "");
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    System.setProperty("http.keepAlive", "false");
                    HttpURLConnection conn = (HttpURLConnection) ((new URL(
                            getContext().getString(R.string.aws)).openConnection()));
                    conn.setReadTimeout(MainActivity.READ_TIMEOUT_MS /* milliseconds */);
                    conn.setConnectTimeout(MainActivity.CONNECT_TIMEOUT_MS /* milliseconds */);
                    conn.setRequestProperty("Cookie", mCookie);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestMethod("POST");
                    conn.connect();

                    Writer osw = new OutputStreamWriter(conn.getOutputStream());
                    osw.write(credentials.toString());
                    osw.flush();
                    osw.close();

                    // handling the response
                    final int HttpResultCode = conn.getResponseCode();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;


                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        // Append server response in string
                        sb.append(line + "\n");
                    }
                    String text = sb.toString();
                    reader.close();

                    JSONObject json = new JSONObject(text);

                    JSONObject dogs_json = json.getJSONObject("dogs");

                    SharedPreferences.Editor editor = prefs.edit();
                    int count = 0;
                    Iterator<?> keys = dogs_json.keys();
                    while(keys.hasNext())
                    {
                        String key = (String)keys.next();
                        if (dogs_json.get(key) instanceof JSONObject) {
                            JSONObject dog_json = dogs_json.getJSONObject(key);
                            count++;
                            String dogName = dog_json.getString("dogName");
                            String age = dog_json.getString("age");
                            String weight = dog_json.getString("weight");
                            String gender = dog_json.getString("gender");
                            editor.putString("dog" + count, dogName + ":" + age + ":" + weight + ":" + gender);
                        }
                    }
                    editor.putString("dogCount", count + "");
                    editor.apply();

                    //Log.d("doggo","response inside logged in: " + HttpResultCode);
                    // TODO: get username and email and display in navigation drawer
                    // Makes sure that the InputStream is closed after the app is
                    // finished using it.
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    if (is != null)
                    {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return null;
            }
        }.execute();

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

