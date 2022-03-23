package com.example.shibushi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.core.FirestoreClient;
import com.google.firestore.v1.WriteResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    Button bRegister;
    EditText etEmailAddress, etPassword, etUsername;
    ProgressBar progressBar;
    TextView txt_login;

    FirebaseAuth mAuth;
    DatabaseReference dReference;
    ProgressDialog pd;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bRegister = findViewById(R.id.bRegister);

        etEmailAddress = findViewById(R.id.etEmailAddress);
        etPassword = findViewById(R.id.etPassword);
        etUsername = findViewById(R.id.etUsername);
        progressBar = findViewById(R.id.progress_circular);
        txt_login = findViewById(R.id.txt_login);

        //
        mAuth = FirebaseAuth.getInstance();
        dReference = FirebaseDatabase.getInstance("https://shibushi-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Registering");
                pd.show();

                String email = etEmailAddress.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String username = etUsername.getText().toString().trim();
                // Check all fields...
                if (username.isEmpty()||email.isEmpty()||password.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Not all fields are filled...",Toast.LENGTH_SHORT).show();
                }
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
                //Todo check Password strength
                else {
                    //progressBar.setVisibility(View.VISIBLE);
                    register(username, email, password);
                   // progressBar.setVisibility(View.GONE);
                }


            }
        });

    }

    private void register(String username, String email, String password){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String userid = firebaseUser.getUid();
                    Log.i("Logcat", userid);

//                    Map<String, Object> city = new HashMap<>();
//                    city.put("name", "Los Angeles");
//                    city.put("state", "CA");
//                    city.put("country", "USA");
//
//                    db.collection("cities").document("LA")
//                            .set(city);


// RealTime Database
                    DatabaseReference add_reference = dReference.child("users").child(userid);

                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("username", username.toLowerCase());
                    hashMap.put("bio", "");
                    hashMap.put("imageurl","https://firebasestorage.googleapis.com/v0/b/shibushi.appspot.com/o/images%2Fayayaka.png?alt=media&token=56948b63-37c1-432a-83d6-53793e79e1ee" );

                    add_reference.setValue(hashMap);
                    // can check with addOnSuccessListener(...
                    pd.dismiss();
                    //progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else{
                    pd.dismiss();
                    Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}





















