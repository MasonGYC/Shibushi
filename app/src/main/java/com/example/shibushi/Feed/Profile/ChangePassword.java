package com.example.shibushi.Feed.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shibushi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {
    TextView passwordOld;
    TextView password1;
    TextView password2;
    Button confirmResetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Change Password");
        setContentView(R.layout.activity_change_password);

        passwordOld = findViewById(R.id.oldPassword);
        password1 = findViewById(R.id.newPassword);
        password2 = findViewById(R.id.confirmNewPassword);
        confirmResetButton = findViewById(R.id.resetButton);

        confirmResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password1.getText().toString().length() < 6){
                    Toast.makeText(ChangePassword.this,"Fail: password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    Log.i("CHANGE PASSWORD", "Fail: password too short");
                    password1.requestFocus();
                    password1.setError("password must be at least 6 characters");
                }

                else if (!password1.getText().toString().equals(password2.getText().toString())){
                    Toast.makeText(ChangePassword.this,"Fail: password doesn't match", Toast.LENGTH_SHORT).show();
                    Log.i("CHANGE PASSWORD", "Fail: password doesn't match");
                    password2.requestFocus();
                    password2.setError("two new passwords must be the same!");
                }

                else{
                    // new password is valid
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    final String email = user.getEmail();
                    String newPassword = password1.getText().toString();
                    String oldPassword = passwordOld.getText().toString();
                    AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()){
                                            Log.i("CHANGE PASSWORD", "Fail: unknown failure");
                                            Toast.makeText(ChangePassword.this, "Fail to change password. Please try again later.", Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            Toast.makeText(ChangePassword.this, "Password updated successfully!", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(ChangePassword.this, EditProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        }
                                    }
                                });
                            }
                            else{
                                Log.i("CHANGE PASSWORD", "Fail: old password invalid");
                                Toast.makeText(ChangePassword.this, "Fail to change password. Please ensure that you input the correct old password", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }






}