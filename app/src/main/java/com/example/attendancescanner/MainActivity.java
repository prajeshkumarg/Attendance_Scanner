package com.example.attendancescanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayAdapter arrayAdapter;
    private ListView result;
    private Button button;
    private List<String> names=new ArrayList<>();
    public static final int REQ=100;
    public static final int REQC=200;
    private List<String > attCheck= new ArrayList<String >();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        button=findViewById(R.id.scan);
        result=findViewById(R.id.result);
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,names);
        result.setAdapter(arrayAdapter);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Attendance List");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    attCheck.add(dataSnapshot1.getValue().toString());
                    Log.i("DATA",attCheck.get(0));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQC);


        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ScanActivity.class);
                startActivityForResult(intent,REQ);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ && resultCode==RESULT_OK) {
            if (data != null) {
                Barcode barcode = data.getParcelableExtra("barcode");
                String t=barcode.displayValue;
                if(attCheck.contains(t)) {
                    names.add(barcode.displayValue);
                    arrayAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(),"Mareked Attendance",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Student not in Database",Toast.LENGTH_LONG).show();
                }

            }
            else{
                Toast.makeText(getApplicationContext(),"Failed to get data",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
