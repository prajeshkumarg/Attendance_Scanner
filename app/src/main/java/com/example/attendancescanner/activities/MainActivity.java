package com.example.attendancescanner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.attendancescanner.adapters.AttendanceAdapter;
import com.example.attendancescanner.models.AttendanceModel;
import com.example.attendancescanner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AttendanceAdapter.OnAttendanceListener{

    static ArrayList<AttendanceModel> attendanceModels;
    static RecyclerView recyclerView;
    static ConstraintLayout constraintLayout;
    static SharedPreferences sharedPreferences;
    static AttendanceAdapter adapter;
    ImageView imageView_profile;
    Gson gson;
    Dictionary<String,List<String>> class_dict = new Hashtable<>();
    private List<String> profileclassList = new ArrayList<>();
    ArrayList<String> studentsname=new ArrayList<>();
    DatabaseReference databaseReference;

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
                        String title = dataSnapshot.child("title").getValue().toString();
                        studentsname = (ArrayList<String>) dataSnapshot.child("students").getValue();
                        class_dict.put(title,studentsname);
                    }

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


    public void nextActivity(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Title");
        alert.setMessage("Message");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                // Do something with value!
                if (profileclassList.contains(value)){
                    ArrayList<String> student_checklist = (ArrayList<String>)class_dict.get(value);
                    Intent intent = new Intent(getApplicationContext(),AttendanceActivity.class);
                    intent.putExtra("STUDENT_LIST",student_checklist);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }
                else{
                    Toast.makeText(getApplicationContext(),"You don't have that class, kindly add it if necessary",Toast.LENGTH_SHORT).show();
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (allPermissionsGranted()) {
            Log.i("permission", "granted");
        } else {
            getRuntimePermissions();
        }
        initializedata_classes();
        recyclerView = findViewById(R.id.my_recycler_view);
        constraintLayout = findViewById(R.id.constraintLayout);
        imageView_profile = findViewById(R.id.profile_button);
        sharedPreferences = this.getSharedPreferences("com.example.barcodeattendancetaker",MODE_PRIVATE);
        try {
            gson = new Gson();
            String response = sharedPreferences.getString("attendance","");
            if(response==""){
                attendanceModels = new ArrayList<>();
            }else{
                attendanceModels =(ArrayList<AttendanceModel>) gson.fromJson(response,new TypeToken<ArrayList<AttendanceModel>>(){}.getType());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        if(attendanceModels.size()==0){
            recyclerView.setVisibility(View.GONE);
            constraintLayout.setVisibility(View.VISIBLE);
        }
        else{
            recyclerView.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.GONE);
        }
        adapter = new AttendanceAdapter(attendanceModels,this);
        recyclerView.setAdapter(adapter);
//        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
//        itemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.divider));
//        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imageView_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onAttendanceClick(int position) {
        Log.i("position",position+"");
        Intent intent = new Intent(getApplicationContext(),AttendanceViewerActivity.class);
        intent.putExtra("position",position);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    this.getPackageManager()
                            .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (Exception e) {
            return new String[0];
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        Log.i("Status", "Permission granted!");
        if (allPermissionsGranted()) {
            Log.i("Status","Granted!");
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i("Status", "Permission granted: " + permission);
            return true;
        }
        Log.i("Status", "Permission NOT granted: " + permission);
        return false;
    }

}
