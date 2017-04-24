package com.doggo.doggydaycare.Fragments;

import android.app.Fragment;

import com.doggo.doggydaycare.Interfaces.RetainedFragmentInteraction;

/**
 * Created by meghankh on 4/24/2017.
 */

public class TaskFragment extends Fragment implements RetainedFragmentInteraction{

    public static final String TAG_TASK_FRAGMENT = "task_fragment";

    @Override
    public String getActiveFragmentTag() {
        return null;
    }

    @Override
    public void setActiveFragmentTag(String s) {

    }

    @Override
    public void checkIfLoggedIn() {

    }

    @Override
    public void loginResult(String result) {

    }

    @Override
    public void startBackgroundServiceNeeded() {

    }
}
