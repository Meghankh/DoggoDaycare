package com.doggo.doggydaycare.fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.doggo.doggydaycare.constants.Constants;
import com.doggo.doggydaycare.interfaces.ActivityInteraction;
import com.doggo.doggydaycare.interfaces.RetainedFragmentInteraction;
import com.doggo.doggydaycare.network.IsUserLoggedInAsyncTask;
import com.doggo.doggydaycare.service.BackgroundService;

/**
 * Created by Meghan on 2/16/2017.
 */
public class TaskFragment extends Fragment implements RetainedFragmentInteraction
{
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        activity = (ActivityInteraction)context;
        checkIfLoggedIn();
    }

    public static final String TAG_TASK_FRAGMENT = "task_fragment";
    private String mActiveFragmentTag;
    private IsUserLoggedInAsyncTask logincheck;
    private ActivityInteraction activity;

    public static TaskFragment newInstance()
    {
        TaskFragment fragment = new TaskFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public TaskFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    public String getActiveFragmentTag()
    {
        return mActiveFragmentTag;
    }

    public void setActiveFragmentTag(String s)
    {
        mActiveFragmentTag = s;
    }

    @Override
    public void checkIfLoggedIn()
    {
        logincheck = new IsUserLoggedInAsyncTask(getActivity(), this);
        logincheck.execute();
    }

    @Override
    public void loginResult(String result)
    {
        Log.d("doggo", "login check:" + result);
        if (result.equals(Constants.STATUS_RELOGIN))
        {
            activity.InitiateLoginActivity();
        }
    }

    // checks if the background service is running
    public boolean isBackgroundServiceRunning() {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            Log.d("doggo", "Checking");
            if (BackgroundService.class.getName().equals(service.service.getClassName()))
            {
                Log.d("doggo", "BackgroundService is already running!");
                return true;
            }
        }
        return false;
    }

    @Override
    public void startBackgroundServiceNeeded()
    {
        // check if the background service is running, if not then start it
        if (!isBackgroundServiceRunning())
        {
            Intent intent = new Intent(getActivity(), BackgroundService.class);
            getActivity().startService(intent);
            Log.d("doggo", "BackgroundService TOLD TO START!");
        }

    }
}
