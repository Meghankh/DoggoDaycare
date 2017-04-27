package com.doggo.doggydaycare;

import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.doggo.doggydaycare.constants.Constants;
import com.doggo.doggydaycare.interfaces.LogInScreenInteraction;
import com.doggo.doggydaycare.network.UserLoginTask;

public class LoginScreen extends Activity implements View.OnClickListener, LogInScreenInteraction
{
    private EditText password, username;
    private Button login;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    UserLoginTask logintask;
    Boolean busyNetworking=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = prefs.edit();

        login = (Button)findViewById(R.id.button);
        login.setOnClickListener(this);

        password = (EditText)findViewById(R.id.password);
        username = (EditText)findViewById(R.id.username);
    }

    @Override
    public void onClick(View v) {
        if(!busyNetworking){
            logintask = new UserLoginTask(this,username.getText().toString(),password.getText().toString());
            logintask.execute();
        }
    }

    @Override
    public void LoginStatus(String status)
    {
        //saving login status into persistence
        editor.putString("status",status).commit();

        if (status != Constants.STATUS_RELOGIN) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(status,true);
            this.startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(this, "Failed to login: wrong username or password", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void NetworkingFlagUpdate(Boolean busyNetworking) {
        this.busyNetworking=busyNetworking;
    }
}

