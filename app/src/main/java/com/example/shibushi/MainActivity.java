package com.example.shibushi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button bLogout, bChangePassword;
    TextView tvWelcome;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bLogout = findViewById(R.id.bLogout);
        bChangePassword = findViewById(R.id.bChangePassword);
        bLogout.setOnClickListener(this);
        bChangePassword.setOnClickListener(this);

        //Firebase authentication
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //Welcome message
        //Obviously we don't have to include this but its an option to display the username somewhere
        //somehow
        String welcome = "Welcome, " + currentUser.getDisplayName();
        tvWelcome = findViewById(R.id.tvWelcome);
        tvWelcome.setText(welcome);
    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.bLogout:
                logOut();
                break;
            // Not high priority, may remove
            case R.id.bChangePassword:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    private void logOut() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            mAuth.signOut();
            Toast.makeText(this, user.getEmail()+ "is logged out!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Login.class));;
        }else{
            Toast.makeText(this, "You aren't logged in yet!", Toast.LENGTH_SHORT).show();
        }
    }

}


/*
* This is the Main screen that the user sees. From here, users can navigate to other activities.
* */
