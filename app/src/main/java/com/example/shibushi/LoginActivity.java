package com.example.shibushi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    Button bLogin;
    EditText etEmailAddress, etPassword;
    Button bRegister;

    FirebaseAuth mAuth;
    DatabaseReference dReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Firebase authentication
        mAuth = FirebaseAuth.getInstance();

        bLogin = findViewById(R.id.bLogin);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etPassword = findViewById(R.id.etPassword);
        bRegister = findViewById(R.id.bRegister);

        dReference = FirebaseDatabase.getInstance("https://shibushi-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                pd.setMessage("Logging In");
                pd.show();

                String email = etEmailAddress.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                // CHECK credentials
                if (TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Incorrect credentials",Toast.LENGTH_SHORT).show();
                }
//                if(email.isEmpty()){
//                    etEmailAddress.setError("Email address is required!");
//                    etEmailAddress.requestFocus();
//                    //return;
//                }
//                //Password validation
//                if (password.isEmpty()){
//                    etPassword.setError("Password is required!");
//                    etPassword.requestFocus();
//                   // return;
//                }
                else{
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                DatabaseReference get_ref = dReference.child("Users").child(mAuth.getCurrentUser().getUid());
                                get_ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        pd.dismiss();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        pd.dismiss();
                                    }
                                });
                            }
                            else {
                                pd.dismiss();
                                Toast.makeText(LoginActivity.this, "Wrong email or password. Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}