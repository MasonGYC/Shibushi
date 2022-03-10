package com.example.shibushi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Login extends AppCompatActivity implements View.OnClickListener{
    Button bLogin;
    EditText etEmailAddress, etPassword;
    TextView tvRegisterLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bLogin = findViewById(R.id.bLogin);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etPassword = findViewById(R.id.etPassword);
        tvRegisterLink = findViewById(R.id.tvRegisterLink);

        bLogin.setOnClickListener(this);
        tvRegisterLink.setOnClickListener(this);

    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.bLogin:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.tvRegisterLink:
                startActivity(new Intent(this, Register.class));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}