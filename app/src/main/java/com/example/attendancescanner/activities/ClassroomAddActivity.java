package com.example.attendancescanner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.attendancescanner.R;
import com.example.attendancescanner.models.AttendanceObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassroomAddActivity extends AppCompatActivity {

    List<AttendanceObject> attendanceObjects_list=new ArrayList<>();
    EditText add_title,editTextattd;
    Button confirmattd,doneattd;
    int attd_counter;
    int names_counter;
    DatabaseReference databaseReference;
    String uEmail;
    HashMap<Integer, Object> nameslist= new HashMap<>();
    HashMap<String, Object> post=new HashMap<>();
    HashMap<String , Object> post1=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_add);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uEmail = user.getEmail().replace('.', '_');
        initializedata();
        add_title = findViewById(R.id.add_title);
        editTextattd=findViewById(R.id.edittextattd);
        confirmattd=findViewById(R.id.confirmattd);
        doneattd=findViewById(R.id.cancelattd);

        confirmattd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String attdcontent=editTextattd.getText().toString();
                if(attdcontent.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter text", Toast.LENGTH_SHORT).show();
                }
                else {
                    nameslist.put(names_counter,attdcontent);
                    post.put(String.valueOf(names_counter),attdcontent);
                    names_counter=names_counter+1;
                    Toast.makeText(getApplicationContext(),"Success added:"+attdcontent,Toast.LENGTH_SHORT).show();
                }
                editTextattd.getText().clear();
            }
        });
        doneattd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = add_title.getText().toString();
                if(title!=null && !post.isEmpty()) {
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uEmail).child("classes");
                    post1.put("students", post);
                    post1.put("title", title);
                    databaseReference.child(String.valueOf(attd_counter)).setValue(post1);
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    attd_counter=attd_counter+1;
                    names_counter=0;
                    add_title.setText("");
                    initializedata();
                    Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),"all fields must be filled",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void initializedata() {
        attd_counter = 0;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uEmail).child("classes");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                attendanceObjects_list.clear();
                for(DataSnapshot dataSnapshot1:snapshot.getChildren()) {
                    if(dataSnapshot1!=null)
                        attd_counter=attd_counter+1;
                }
                Log.i("NUMBER",attd_counter+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}