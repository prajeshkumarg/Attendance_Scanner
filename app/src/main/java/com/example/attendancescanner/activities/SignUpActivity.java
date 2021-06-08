package com.example.attendancescanner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.attendancescanner.R;
import com.example.attendancescanner.models.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText signup_name,signup_email,signup_regno,signup_pass,signup_confpass;
    Button signup_button;
    String name,email,regno,password,confpassword;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabaseRef;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        context = getApplicationContext();

        signup_button = findViewById(R.id.signup_button);
        signup_name = findViewById(R.id.signup_name);
        signup_email = findViewById(R.id.signup_email);
        signup_regno = findViewById(R.id.signup_regno);
        signup_pass = findViewById(R.id.signup_password);
        signup_confpass = findViewById(R.id.signup_conf_password);
        firebaseAuth= FirebaseAuth.getInstance();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("Users");

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = signup_name.getText().toString();
                email = signup_email.getText().toString();
                regno = signup_regno.getText().toString();
                password = signup_pass.getText().toString();
                confpassword = signup_confpass.getText().toString();

                if(TextUtils.isEmpty(name)){
                    signup_name.setError("Password is Required.");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    signup_email.setError("Email is Required.");
                    return;
                }
                if(TextUtils.isEmpty(regno)){
                    signup_regno.setError("Password is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    signup_pass.setError("Password is Required.");
                    return;
                }
                if(TextUtils.isEmpty(confpassword)){
                    signup_confpass.setError("Password is Required.");
                    return;
                }
                if(!password.equals(confpassword)){
                    signup_pass.setError("Passwords must be same");
                    signup_confpass.setError("Try again");
                    return;
                }

                if(password.length()<6){
                    signup_pass.setError("Password Must be >= 6 Characters");
                    return;
                }
                newprocess();

            }
        });
    }

    private void newprocess() {
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "SignUp Successful.", Toast.LENGTH_SHORT).show();
                    final String uEmail;
                    uEmail=email.replace('.','_');
                    Profile user = new Profile(name,email,regno);
                    String uploadID = uEmail;
                    mDatabaseRef.child(uploadID).setValue(user);

                    Intent intent = new Intent(context,SplashScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                    //uploadData();
                    //newActivity();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Unable to register user" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void newActivity(){
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }

    public void uploadData() {
        final String uEmail;
        uEmail=email.replace('.','_');
        Profile user = new Profile(name,email,regno);
        String uploadID = uEmail;
        mDatabaseRef.child(uploadID).setValue(user);
    }
}