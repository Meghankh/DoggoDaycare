package com.doggo.doggydaycare.Interfaces;

/**
 * Created by meghankh on 4/24/2017.
 */

public interface RetainedFragmentInteraction {

    public String getActiveFragmentTag();
    public void setActiveFragmentTag(String s);
    public void checkIfLoggedIn();
    public void loginResult(String result);
    public void startBackgroundServiceNeeded();

}
