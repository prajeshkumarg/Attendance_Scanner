package com.example.attendancescanner.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.attendancescanner.R;

public class WelcomeActivity extends AppCompatActivity {

    Button login,signup;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        context = getApplicationContext();
        login = findViewById(R.id.login_button_welcome);
        signup = findViewById(R.id.signup_button_welcome);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SignUpActivity.class);
                startActivity(intent);

            }
        });
    }
}