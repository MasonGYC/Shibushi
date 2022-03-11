package com.example.shibushi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button bLogout, bChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bLogout = findViewById(R.id.bLogout);
        bChangePassword = findViewById(R.id.bChangePassword);
        bLogout.setOnClickListener(this);
        bChangePassword.setOnClickListener(this);

    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.bLogout:
                startActivity(new Intent(this, Login.class));;
            case R.id.bChangePassword:
                break;
        }
    }

}




/*
* This is the Main screen that the user sees. It shows the
* */
// TODO: 3/10/2022 Change password