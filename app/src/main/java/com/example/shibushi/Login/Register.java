package com.example.shibushi.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.example.shibushi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener{
    Button bRegister;
    EditText etEmailAddress, etPassword, etUsername;
    ProgressBar progressBar;
    TextView pwStrength;
    View pwStrengthIndicator;
    private FirebaseAuth mAuth;
    private DatabaseReference dReference;
    private static final String TAG = "EmailPassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bRegister = findViewById(R.id.bRegister);
        bRegister.setOnClickListener(this);

        etEmailAddress = findViewById(R.id.etEmailAddress);
        etPassword = findViewById(R.id.etPassword);
        etUsername = findViewById(R.id.etUsername);
        progressBar = findViewById(R.id.progress_circular);

        //Firebase authentication object
        mAuth = FirebaseAuth.getInstance();
        dReference = FirebaseDatabase.getInstance("https://shibushi-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        //Password strength observer
        pwStrengthIndicator = findViewById(R.id.pwStrengthIndicator);
        pwStrength = findViewById(R.id.pwStrength);
        final PasswordStrengthCalculator[] passwordStrengthCalculator = {new PasswordStrengthCalculator()};
        etPassword.addTextChangedListener(passwordStrengthCalculator[0]);

        //Strength color
        passwordStrengthCalculator[0].strengthColor.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d(TAG, "Password change detected.");
                pwStrengthIndicator.setBackgroundColor(ContextCompat.getColor(Register.this, passwordStrengthCalculator[0].strengthColor.getValue()));
                pwStrength.setTextColor(ContextCompat.getColor(Register.this, passwordStrengthCalculator[0].strengthColor.getValue()));
            }
        });
        //Strength text
        passwordStrengthCalculator[0].strengthLevel.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                pwStrength.setText(passwordStrengthCalculator[0].strengthLevel.getValue());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

            //Switch statement used in case more buttons are required on the Register page.
            case R.id.bRegister:
                registerUser();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    // TODO: 3/15/2022 refactor into a new java file
    private void registerUser() {
        String email = etEmailAddress.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String username = etUsername.getText().toString().trim();

        //Email validation
        if(email.isEmpty()){
            etEmailAddress.setError("Email address is required!");
            etEmailAddress.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmailAddress.setError("Please enter a valid email address!");
            etEmailAddress.requestFocus();
            return;
        }
        //Username validation
        if (username.isEmpty()){
            etUsername.setError("Username is required!");
            etUsername.requestFocus();
            return;
        }
        //Password validation
        if (password.isEmpty()){
            etPassword.setError("Password is required!");
            etPassword.requestFocus();
            return;
        }
        if (password.length()<6){
            etPassword.setError("Min password length is 6 characters!");
        }

        progressBar.setVisibility(View.VISIBLE);
        //Todo Add username to user profile during registration.

        // Add the user to Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                            }
                                        }
                                    });
                            //update RTDB
                            dReference.child("users").child("idToken").setValue(username);
                            /*user.getIdToken(true)
                                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                                            if (task.isSuccessful()) {

                                                String idToken = task.getResult().getToken();
                                                dReference.child("users").child(idToken).setValue(username);
                                                Log.v("Logcat", "id: "+ idToken);
                                            } else {
                                                // Handle error -> task.getException();
                                            }
                                        }
                                    });*/
                            //Redirect to Login activity
                            startActivity(new Intent(Register.this, Login.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        progressBar.setVisibility(View.GONE);
    }

}
/*This is the Register page where new users can sign up.
* If they provide invalid details, they will stay on this page.
* If details are valid they will be registered and sent back to the Login page instantly.
* */

//Todo May change toasts to snackbar
