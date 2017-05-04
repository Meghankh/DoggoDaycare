package com.doggo.doggydaycare.database;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.doggo.doggydaycare.MainActivity;
import com.doggo.doggydaycare.R;
import com.doggo.doggydaycare.service.BackgroundService;

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
import java.util.ArrayList;

public class DBController
{
    private SQLiteDatabase db;
    private StepCountDB db_helper;
    private volatile Boolean processing = false;
    private BackgroundService background_service;
    private Context context;
    //private Socket mSocket;

    public DBController(Context context,
                        BackgroundService background_service,
                        Application application)
    {
        db_helper = new StepCountDB(context);
        this.background_service = background_service;
        this.context = context;

        //SocketIO app = (SocketIO)application;
        //mSocket = app.getSocket();
        //mSocket.connect();
    }

    // You need to call this open up the database
    public void OpenDB()
    {
        db = db_helper.getWritableDatabase();
    }

    // When the app is no longer using the database you must release the resources. This method will do that for you.
    public void CloseDB()
    {
        db.close();
        db = null;
    }

    // To avoid any complications that could happen due to simultaneous write/read operations you must make sure to check if the DB is not busy.
    private boolean IsFree()
    {
        return !processing;
    }

    // After the steps have been uploaded to the website the phone receives confirmation in the form
    // of timestamps for each of the step entries. Since we know that the confirmed steps are on the server we no longer need them in the
    // local database. In this method you need to remove all the rows that have been confirmed.
    public void RemoveSyncedSteps(final ArrayList<Long> confirmed)
    {
        Log.d("doggo", "remove steps called isFree " + IsFree() + " for " + confirmed.size());
        // TODO: here you perform the operation that will delete the confirmed steps as indicated by the 'epoch' timestamps inside 'confirmed'
        for (int i = 0; i < confirmed.size(); i++)
        {
            db.delete(DBConstants.TABLE_NAME, DBConstants.COLUMN_NAME_ENTRY_DATE_EPOCH + " = " + confirmed.get(i), null);
        }
        background_service.EraseConfirmedSteps();
    }

    public void InsertSteps(final ArrayList<ContentValues> tracked_steps)
    {
        Log.d("doggo", "insert steps called id db open? " + db.isOpen() + " IsFree to write? " + IsFree());

        for (int i = 0; i < tracked_steps.size(); i++)
        {
            db.insert(DBConstants.TABLE_NAME, null, tracked_steps.get(i));
            // Log.d("doggo", "insert " + tracked_steps.get(i));
        }
        UploadSteps();
        background_service.EraseBufferedSteps();
    }

    private void UploadSteps()
    {
        if (db.isOpen() && IsFree())
        {
            new AsyncTask<Void, Void, Void>()
            {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                Cursor cursor = null;
                Float total = new Float(0f);
                JSONObject data;

                @Override
                protected Void doInBackground(Void... params)
                {
                    String mCookie = prefs.getString("sessionid", "");
                    String user_id = prefs.getString("user_id", "");

                    //Log.d("doggo", "Cookie: " + mCookie);
                    //Log.d("doggo", "User ID: " + user_id);

                    //cursor = db.rawQuery("SELECT count(*) as test FROM " + DBConstants.TABLE_NAME, null);
                    //cursor.moveToFirst();
                    //Log.d("doggo", "TEST " + cursor.getString(cursor.getColumnIndex("test")));
                    //cursor.close();

                    //Log.d("doggo", "outside try.");
                    InputStream is = null;
                    try
                    {
                        //Log.d("doggo", "SELECT * FROM " + DBConstants.TABLE_NAME);
                        processing = true;

                        cursor = db.rawQuery("SELECT * FROM " + DBConstants.TABLE_NAME, null);

                        if (cursor.moveToFirst())
                        {
                            do
                            {
                                Float delta = cursor.getFloat(cursor.getColumnIndex(DBConstants.COLUMN_NAME_INCREMENT_VALUE));
                                total = cursor.getFloat(cursor.getColumnIndex(DBConstants.COLUMN_NAME_TOTAL_VALUE));
                                String date = cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_NAME_ENTRY_DATE));
                                Long epoch = cursor.getLong(cursor.getColumnIndex(DBConstants.COLUMN_NAME_ENTRY_DATE_EPOCH));

                                //Log.d("doggo", date + "");
                                //Log.d("doggo", total + "");

                                JSONObject credentials = new JSONObject();
                                try
                                {
                                    credentials.put("action", "steps");
                                    credentials.put("user_id", user_id + "");
                                    credentials.put("date", date + "");
                                    credentials.put("epoch", epoch + "");
                                    credentials.put("steps_total", total + "");
                                    credentials.put("steps_delta", delta + "");
                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }

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

                                //Log.d("doggo", "Sending user to server... url" + context.getString(R.string.aws)+ " .");
                                Writer osw = new OutputStreamWriter(conn.getOutputStream());
                                osw.write(credentials.toString());
                                osw.flush();
                                osw.close();

                                // handling the response
                                final int HttpResultCode = conn.getResponseCode();

                                is = conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream();
                                Reader reader = new InputStreamReader(is, "UTF-8");
                                char[] buffer = new char[2];
                                reader.read(buffer);
                                String response =  new String(buffer).substring(0, 1);
                                //Log.d("doggo","response inside logged in: " + HttpResultCode);
                                // TODO: get username and email and display in navigation drawer
                                // Makes sure that the InputStream is closed after the app is
                                // finished using it.
                            }
                            while (cursor.moveToNext());
                        }
                        cursor.close();
                    }
                    catch (Exception e)
                    {
                        //Log.d("doggo", "Except occurred");
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
        }
    }
}

