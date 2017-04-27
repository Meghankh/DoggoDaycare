package com.doggo.doggydaycare.interfaces;

/**
 * Created by Meghan on 4/24/2017.
 */
public interface RetainedFragmentInteraction
{
    public String getActiveFragmentTag();
    public void setActiveFragmentTag(String s);
    public void checkIfLoggedIn();
    public void loginResult(String result);
    public void startBackgroundServiceNeeded();
}
