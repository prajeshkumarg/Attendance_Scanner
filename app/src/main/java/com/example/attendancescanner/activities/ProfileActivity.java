package com.example.attendancescanner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendancescanner.R;
import com.example.attendancescanner.models.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

        Profile profile_details = new Profile();
        private List<String> profileclassList = new ArrayList<>();
        DatabaseReference databaseReference;
        ListView listView_classes;
        TextView profile_name,profile_email;
        ArrayAdapter arrayAdapter;
        Button button_logout,button_addclass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        listView_classes = findViewById(R.id.profile_classes);
        profile_name = findViewById(R.id.profile_name);
        profile_email = findViewById(R.id.profile_email);
        button_logout = findViewById(R.id.profile_logout);
        button_addclass = findViewById(R.id.add_new_classroom);
        intializedata_texts();
        initializedata_classes();

        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        button_addclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ClassroomAddActivity.class);
                startActivity(intent);
            }
        });

    }
    private void intializedata_texts() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail().replace('.', '_');
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userEmail);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    String name = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                    String email = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                    profile_name.setText(name);
                    profile_email.setText(email);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
        else {

        }
    }

    private void initializedata_classes() {
        profileclassList = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail().replace('.','_');
            Log.i("U EMAIL:",userEmail);
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userEmail).child("classes");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for ( DataSnapshot dataSnapshot : snapshot.getChildren()){
                        profileclassList.add(dataSnapshot.child("title").getValue().toString());
                    }
                    Log.i("LIST",profileclassList.toString());
                    arrayAdapter = new ArrayAdapter(getApplicationContext(),R.layout.list_item,R.id.list_text_view, profileclassList);

                    listView_classes.setAdapter(arrayAdapter);


                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        } else {
            Toast.makeText(getApplicationContext(),"User is not signed in",Toast.LENGTH_SHORT);
            // No user is signed in
        }
    }
}
