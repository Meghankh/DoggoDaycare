package com.doggo.doggydaycare.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.doggo.doggydaycare.database.DBConstants;
import com.doggo.doggydaycare.database.DBController;
import com.doggo.doggydaycare.socketio.SocketIO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class BackgroundService extends Service implements SensorEventListener
{
    // This is needed for the step count detection
    private SensorManager mSensorManager;

    SharedPreferences prefs;
    private Socket mSocket;

    private int minute = 0;

    // These variables are used to detect the first step and to offset the old steps
    private float steps = 0f;
    private float baseline_steps = 0f;
    private int count = 0;

    //TODO: you need to create an ArrayList object that will hold the ContentValues of steps.
    ArrayList<ContentValues> contentValuesArrayList;
    //TODO: you need to create an ArrayList object that will hold confirmation timestamps.
    ArrayList<Long> timestampArraylist;

    private DBController database_controller;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    // happenswhen service starts
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);

        // we need this for extracting username when 'emitting' steps to the server.
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // We need to initialize an intent filter that will recognize 'ACTION_TIME_TICK'
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);

        // We need to register our local broadcast receiver
        registerReceiver(receiver, filter);

        Log.d("background_service", "BackgroundService Started!");

        //sensor manager allows us to get access to all of the sensors that your device is offering you.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // lets try and see if we can get the sensor that gives daily stepcount
        Sensor countSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        // if your device has this sensor then register the listener
        if (countSensor != null)
        {
            mSensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        //if not then pop a toast that will let you know
        else
        {
            Toast.makeText(this, "TYPE_STEP_COUNTER not available", Toast.LENGTH_LONG).show();
        }

        // Get the socket from the Application and then connect.
        SocketIO app = (SocketIO) getApplication();
        mSocket = app.getSocket();
        mSocket.connect();

        //TODO: save the confirmation inside the temporary ArrayList<Long> (you need to create one)
        contentValuesArrayList = new ArrayList<ContentValues>();
        timestampArraylist = new ArrayList<Long>();

        mSocket.on("step_confirmation", new Emitter.Listener()
        {
            @Override
            public void call(final Object... args)
            {
                try
                {
                    Log.d("socketio", ((JSONObject) args[0]).getString("epoch")
                            + " from user " + ((JSONObject) args[0]).getString("username"));
                    //TODO: what happens here?
                    timestampArraylist.add(Long.parseLong(((JSONObject)args[0]).getString("epoch")));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });

        // Lets initiate the database controller
        database_controller = new DBController(getApplicationContext(), this, getApplication());
        database_controller.OpenDB();
        // START_STICKY -- ? what does it mean? Research it.
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        Log.d("background_service", "BackgroundService Stopped!");

        //inside onDestroy you need to 'unregister' the broadcast receiver
        unregisterReceiver(receiver);
        database_controller.CloseDB();
        database_controller = null;

        //also disconnect the mSocket
        if (mSocket != null)
        {
            mSocket.disconnect();
        }
        super.onDestroy();
    }


    // this happens when steps are detected.
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        // event.values[0] contains the float value of the steps for the day.
        // to start counting from 0 you need to manually offset the initial value.

        if (count == 0)
        {
            baseline_steps = event.values[0];
            steps = 0;
        }
        count++;
        steps = event.values[0] - baseline_steps;

        editor = prefs.edit();
        editor.putFloat("steps", steps);
        editor.commit();
        Log.d("background_service", "Steps:" + steps);

        // TODO: here you populate the ContentValues object and save it in the ArrayList
        ContentValues values = new ContentValues();
        values.put(DBConstants.COLUMN_NAME_ENTRY_DATE, Calendar.getInstance().getTime() + "");
        values.put(DBConstants.COLUMN_NAME_ENTRY_DATE_EPOCH, Calendar.getInstance().getTimeInMillis());
        values.put(DBConstants.COLUMN_NAME_INCREMENT_VALUE, 0);
        values.put(DBConstants.COLUMN_NAME_TOTAL_VALUE, steps);
        values.put(DBConstants.COLUMN_NAME_STATUS, false);
        contentValuesArrayList.add(values);
    }

    //ignore this
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        // Empty
    }

    // BroadcastRecevier receiver
    private final BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            // extracing the string that the action is bringin in
            String action = intent.getAction();
            Log.d("broadcast_service", "action received:" + action.toString());
            // if our action contains "TIME_TICK" we upload to the server via socket
            if (action.contains("TIME_TICK"))
            {
                minute++;
                // TODO: on the i-th minmute you pass the steps to the database
                if (minute % 2 == 0)
                {
                    ArrayList<ContentValues> cloneList = (ArrayList<ContentValues>)contentValuesArrayList.clone();
                    database_controller.InsertSteps(cloneList);
                }
                else
                {
                    ArrayList<Long> cloneStampList = (ArrayList<Long>)timestampArraylist.clone();
                    database_controller.RemoveSyncedSteps(cloneStampList);
                }
                // TODO: on the (i+1)-st minute you remove the steps from the database that have been confirmed to be on the server
            }
        }
    };

    public void EraseConfirmedSteps()
    {
        Log.d("db", "confirmed cleared");
        timestampArraylist.clear();
    }

    public void EraseBufferedSteps()
    {
        Log.d("db", "steps buffer cleared");
        contentValuesArrayList.clear();
    }
}
