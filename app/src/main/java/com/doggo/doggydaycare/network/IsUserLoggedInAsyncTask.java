package com.doggo.doggydaycare.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.doggo.doggydaycare.MainActivity;
import com.doggo.doggydaycare.R;
import com.doggo.doggydaycare.constants.Constants;
import com.doggo.doggydaycare.fragments.TaskFragment;
import com.doggo.doggydaycare.interfaces.RetainedFragmentInteraction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class IsUserLoggedInAsyncTask extends AsyncTask<Void,Void,String>
{
    private RetainedFragmentInteraction taskFragment;
    SharedPreferences prefs;
    private Context context;

    public IsUserLoggedInAsyncTask(Context context, TaskFragment mTaskFragment)
    {
        this.context = context;
        this.taskFragment = (RetainedFragmentInteraction)mTaskFragment;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    protected String doInBackground(Void... params)
    {
        String mUsername = prefs.getString("username", "");
        if (mUsername.equals(""))
        {
            return Constants.STATUS_RELOGIN;
        }
        try
        {
            // Check if the user is logged into the server
            return isUserLoggedIn(mUsername);
        }
        catch (Exception e)
        {
            // Log.d(MainActivity.LOG_TAG, e.getMessage());
        }
        // Return OFFLINE by default
        return Constants.STATUS_OFFLINE;
    }

    @Override
    protected void onPostExecute(String status)
    {
        taskFragment.loginResult(status);
    }

    private String isUserLoggedIn(String user) throws IOException, JSONException
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String mCookie = prefs.getString("sessionid", "");
        if (mCookie.equals(""))
        {
            return Constants.STATUS_RELOGIN;
        }
        InputStream is = null;
        try
        {
            System.setProperty("http.keepAlive", "false");
            HttpURLConnection conn = (HttpURLConnection) ((new URL(
                    context.getString(R.string.aws)).openConnection()));
            conn.setReadTimeout(MainActivity.READ_TIMEOUT_MS /* milliseconds */);
            conn.setConnectTimeout(MainActivity.CONNECT_TIMEOUT_MS /* milliseconds */);
            conn.setRequestProperty("Cookie", mCookie);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");
            conn.connect();

            JSONObject credentials = new JSONObject();
            credentials.put("action", "check_login");
            credentials.put("username", user);

            Log.d("doggo", "Sending user to server... url" + context.getString(R.string.aws)+ " username:" + user);
            Writer osw = new OutputStreamWriter(conn.getOutputStream());
            osw.write(credentials.toString());
            osw.flush();
            osw.close();

            // handling the response
            final int HttpResultCode = conn.getResponseCode();

            is = conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream();
            String response = readInputStream(is, 2).substring(0, 1);
            Log.d("doggo","response inside logged in: " + HttpResultCode);
            // TODO: get username and email and display in navigation drawer

            if (!response.contains("0"))
            {
                conn.disconnect();
                return Constants.STATUS_LOGGED_IN;
            }
            else if (HttpResultCode > 200)
            {
                conn.disconnect();
                return Constants.STATUS_RELOGIN;
            }
            else
            {
                conn.disconnect();
                return Constants.STATUS_OFFLINE;
            }
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
                is.close();
            }
        }
        return Constants.STATUS_OFFLINE;
    }

    public String readInputStream(InputStream stream, int len) throws IOException
    {
        Reader reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
