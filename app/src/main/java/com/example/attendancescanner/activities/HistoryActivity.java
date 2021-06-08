package com.example.attendancescanner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.attendancescanner.models.AttendanceObject;
import com.example.attendancescanner.adapters.HistoryAdapter;
import com.example.attendancescanner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;

    List<AttendanceObject> attendanceObjects_list=new ArrayList<>();
    List<String> attendancecontent=new ArrayList<>();
    RecyclerView recyclerView_attendance;
    DatabaseReference databaseReference_attendance;
    HistoryAdapter attendanceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView_attendance=findViewById(R.id.recycler_view);
        recyclerView_attendance.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        attendanceObjects_list=new ArrayList<>();
        databaseReference_attendance= FirebaseDatabase.getInstance().getReference().child("Attendance");
        databaseReference_attendance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot1:snapshot.getChildren()) {
                    String title= Objects.requireNonNull(dataSnapshot1.child("Title").getValue()).toString();
                    attendancecontent= (List<String>) dataSnapshot1.child("Content").getValue();
                    attendanceObjects_list.add(new AttendanceObject(attendancecontent,title));
                }
                attendanceAdapter=new HistoryAdapter(attendanceObjects_list);
                recyclerView_attendance.setAdapter(attendanceAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}