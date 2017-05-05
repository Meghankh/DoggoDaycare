package com.doggo.doggydaycare.fragments;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doggo.doggydaycare.MainActivity;
import com.doggo.doggydaycare.R;

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

/**
 * Created by Meghan on 3/2/2017.
 */
public class TeamsRankFragment extends Fragment
{
    public static final String TAG_TEAM_RANK_FRAGMENT = "dashboard_fragment";
    private TextView lastName;
    private TextView firstName;
    private TextView stepsLabel;

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

        View rowView = inflater.inflate(R.layout.fragment_team_rank, container, false);

        firstName = (TextView) rowView.findViewById(R.id.firstname);
        lastName = (TextView) rowView.findViewById(R.id.lastname);
        stepsLabel = (TextView) rowView.findViewById(R.id.steps);

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
                        credentials.put("action", "findUser");
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

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("lastName", json.getString("lastName"));
                    editor.putString("firstName", json.getString("firstName"));
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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String lastNameStr = prefs.getString("lastName", "");
        String firstNameStr = prefs.getString("firstName", "");

        firstName.setText(firstNameStr);
        lastName.setText(lastNameStr);
        stepsLabel.setText(prefs.getFloat("steps",0)+" steps");

        return rowView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }
}

