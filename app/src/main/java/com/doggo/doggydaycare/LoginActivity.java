package com.doggo.doggydaycare;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by meghankh on 4/22/2017.
 */

public class LoginActivity extends Activity implements View.OnClickListener{

    private EditText password, username;
    private Button login;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(this);

        password = (EditText)findViewById(R.id.password);
        username = (EditText)findViewById(R.id.username);
    }

    @Override
    public void onClick(View v) {

    }
}
