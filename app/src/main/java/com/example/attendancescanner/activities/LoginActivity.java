package com.example.attendancescanner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.attendancescanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText login_email,login_password;
    Button login_button;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login_button = findViewById(R.id.login_button);
        firebaseAuth=FirebaseAuth.getInstance();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = login_email.getText().toString();
                String password = login_password.getText().toString();
                if(TextUtils.isEmpty(email)){
                    login_email.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    login_password.setError("Password is Required.");
                    return;
                }

                if(password.length()<6){
                    login_password.setError("Password Must be >= 6 Characters");
                    return;
                }

                login_button.setEnabled(false);
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Intent i= new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(i);
                            finish();
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        }
                        else
                        {
                            login_button.setEnabled(true);
                            Snackbar.make(findViewById(android.R.id.content),"Unable To Login!!!",Snackbar.LENGTH_SHORT).setTextColor(getColor(R.color.white)).setBackgroundTint(getColor(R.color.colorPrimary)).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        login_button.setEnabled(true);
                        Snackbar.make(findViewById(android.R.id.content),"Unable To Login!!!",Snackbar.LENGTH_SHORT).setTextColor(getColor(R.color.white)).setBackgroundTint(getColor(R.color.colorPrimary)).show();

                    }
                });
            }
        });
    }

}