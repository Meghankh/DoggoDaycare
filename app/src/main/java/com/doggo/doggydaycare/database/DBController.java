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

import com.doggo.doggydaycare.service.BackgroundService;
import com.doggo.doggydaycare.socketio.SocketIO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Socket;

public class DBController
{
    private SQLiteDatabase db;
    private StepCountDB db_helper;
    private volatile Boolean processing = false;
    private BackgroundService background_service;
    private Context context;
    private Socket mSocket;

    public DBController(Context context,
                        BackgroundService background_service,
                        Application application)
    {
        db_helper = new StepCountDB(context);
        this.background_service = background_service;
        this.context = context;
        SocketIO app = (SocketIO)application;
        mSocket = app.getSocket();
        mSocket.connect();
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
        Log.d("db", "remove steps called isFree " + IsFree() + " for " + confirmed.size());
        // TODO: here you perform the operation that will delete the confirmed steps as indicated by the 'epoch' timestamps inside 'confirmed'
        for (int i = 0; i < confirmed.size(); i++)
        {
            db.delete(DBConstants.TABLE_NAME, DBConstants.COLUMN_NAME_ENTRY_DATE_EPOCH + " = " + confirmed.get(i), null);
        }
        background_service.EraseConfirmedSteps();
    }

    public void InsertSteps(final ArrayList<ContentValues> tracked_steps)
    {
        Log.d("db", "insert steps called id db open? " + db.isOpen() + " IsFree to write? " + IsFree());

        for (int i = 0; i < tracked_steps.size(); i++)
        {
            db.insert(DBConstants.TABLE_NAME, null, tracked_steps.get(i));
        }
        // UploadSteps();
        background_service.EraseBufferedSteps();
    }

    private void UploadSteps()
    {
        Log.d("db", "Upload steps called" + " isFree " + IsFree());
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
                    if (!mSocket.connected())
                    {
                        mSocket.connect();
                    }

                    JSONObject data;
                    JSONObject data2 = new JSONObject();

                    processing = true;

                    cursor = db.rawQuery("SELECT * FROM " + DBConstants.TABLE_NAME, null);

                    if (cursor.moveToFirst())
                    {
                        do
                        {
                            Float delta = cursor.getFloat(cursor.getColumnIndex(DBConstants.COLUMN_NAME_INCREMENT_VALUE));
                            total = cursor.getFloat(cursor.getColumnIndex(DBConstants.COLUMN_NAME_TOTAL_VALUE));
                            String date = cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_NAME_ENTRY_DATE));
                            Integer _id = cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_NAME_ENTRY_ID));
                            Long epoch = cursor.getLong(cursor.getColumnIndex(DBConstants.COLUMN_NAME_ENTRY_DATE_EPOCH));

                            data = new JSONObject();
                            try
                            {
                                data.put("username", prefs.getString("username", ""));
                                data.put("delta", delta + "");
                                data.put("total", total + "");
                                data.put("date", date + "");
                                data.put("epoch", epoch);
                                data.put("id", _id + "");
                                data.put("type", "steps");
                                data2 = data;
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                            if (mSocket != null)
                            {
                                mSocket.emit("steps", data);
                            }

                        }
                        while (cursor.moveToNext());
                    }
                    cursor.close();

                    processing = false;
                    mSocket.emit("steps_upload_done", data2);

                    return null;
                }

                @Override
                protected void onPostExecute(Void result)
                {

                }
            }.execute();
        }
    }
}

