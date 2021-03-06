package com.doggo.doggydaycare.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.doggo.doggydaycare.MainActivity;
import com.doggo.doggydaycare.R;
import com.doggo.doggydaycare.constants.Constants;
import com.doggo.doggydaycare.interfaces.LogInScreenInteraction;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class UserLoginTask extends AsyncTask<String, Void, String>
{
    private final String username;
    private final String password;
    private Context context;
    private LogInScreenInteraction activity;

    public UserLoginTask(Context context, String username, String password)
    {
        this.username = username;
        this.password = password;
        this.context = context;
        this.activity = (LogInScreenInteraction)context;
    }

    @Override
    protected String doInBackground(String... params)
    {
        String result = Constants.STATUS_OFFLINE;
        try
        {
            // Send the entered username and password to the server and check for success
            publishProgress();
            Thread.sleep(500);
            result = attemptLogin();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onProgressUpdate(Void... params)
    {
        activity.NetworkingFlagUpdate(true);
        return;
    }

    @Override
    protected void onPostExecute(String result)
    {
        activity.NetworkingFlagUpdate(false);
        activity.LoginStatus(result);
    }

    @Override
    protected void onCancelled()
    {
        // Empty
    }

    private void saveInSharedPreferences(String result)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("sessionid", result);
        editor.putString("username", username);
        editor.putString("password", password);

        editor.apply();
    }

    private String attemptLogin() throws IOException
    {
        InputStream is = null;
        String cookie = "empty cookie";
        String result = "relogin";

        try
        {
            System.setProperty("http.keepAlive", "false");
            Log.d("doggo", "Creating connection to server for logging in.. url " + context.getString(R.string.aws) + " username:" + username + " password:" + password);
            HttpURLConnection conn = (HttpURLConnection) ((new URL(
                    context.getString(R.string.aws)).openConnection()));
            conn.setDoOutput(true);
            conn.setReadTimeout(MainActivity.READ_TIMEOUT_MS /* milliseconds */);
            conn.setConnectTimeout(MainActivity.CONNECT_TIMEOUT_MS /* milliseconds */);

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");
            conn.connect();

            JSONObject credentials = new JSONObject();
            credentials.put("action", "login");
            credentials.put("username", username);
            credentials.put("password", password);

            Log.d("doggo", "Sending login credentials to server... url" + context.getString(R.string.aws)+ " username:" + username + " password:" + password);
            Writer osw = new OutputStreamWriter(conn.getOutputStream());
            osw.write(credentials.toString());
            osw.flush();
            osw.close();

            final int HttpResultCode = conn.getResponseCode();
            is = HttpResultCode >= 400 ? conn.getErrorStream() : conn.getInputStream();

            Log.d("doggo", "Response is: " + HttpResultCode);
            if (HttpResultCode == HttpURLConnection.HTTP_OK)
            {
                Log.d("doggo", "Response is: " + conn.getContent());
                BufferedReader reader;
                String text = "";
                try
                {
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        // Append server response in string
                        sb.append(line + "\n");
                    }
                    text = sb.toString();
                    reader.close();
                }
                catch(Exception e)
                {

                }

                // Show response on activity
                Log.d("doggo", text);
                JSONObject jsonObj = new JSONObject(text);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("user_id", jsonObj.getString("userID"));
                editor.apply();

                Map<String, List<String>> headerFields = conn.getHeaderFields();
                List<String> cookiesHeader = headerFields.get(context.getString(R.string.cookies_header));
                cookie = cookiesHeader.get(0).substring(0, cookiesHeader.get(0).indexOf(";"));
                saveInSharedPreferences(cookie);
                result = Constants.STATUS_LOGGED_IN;
            }
            else if (HttpResultCode == 401)
            {
                Log.d("doggo", "Did not receive HTTP_OK from server!:"+401);
                result = Constants.STATUS_RELOGIN;
            }
            else
            {
                result = Constants.STATUS_OFFLINE;
                Log.d("doggo", "Did not receive HTTP_OK from server!--other");
            }
            conn.disconnect();
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

        return result;
    }
}
