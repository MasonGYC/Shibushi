package com.example.shibushi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener{
    Button bLogin;
    EditText etEmailAddress, etPassword;
    TextView tvRegisterLink;

    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Firebase authentication
        mAuth = FirebaseAuth.getInstance();

        bLogin = findViewById(R.id.bLogin);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etPassword = findViewById(R.id.etPassword);
        tvRegisterLink = findViewById(R.id.tvRegisterLink);

        bLogin.setOnClickListener(this);
        tvRegisterLink.setOnClickListener(this);

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(this, MainActivity.class));
            //Should use FirebaseUser.reload method instead?
        }
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.bLogin:
                loginUser();
                break;
            case R.id.tvRegisterLink:
                startActivity(new Intent(this, Register.class));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    private void loginUser() {
        String email = etEmailAddress.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        //Email validation
        if(email.isEmpty()){
            etEmailAddress.setError("Email address is required!");
            etEmailAddress.requestFocus();
            return;
        }
        //Password validation
        if (password.isEmpty()){
            etPassword.setError("Password is required!");
            etPassword.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(Login.this, MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}
/* This is the first page a logged out user will see. The user can navigate to Register or
 * attempt to log in.
 * If there is a previous user logged in, they will directly be sent to the Main Activity page.
 *
 * If they are unsuccessful in logging in, they will stay on the log in page.
 * If they are successful they will go to the Main Activity page. */