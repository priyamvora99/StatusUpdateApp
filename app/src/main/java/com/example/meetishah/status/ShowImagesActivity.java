package com.example.meetishah.status;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShowImagesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    String TAG="ShowImagesActivity";
    //adapter object
    private RecyclerView.Adapter adapter;

    //database reference
    private DatabaseReference mDatabase;

    //progress dialog
    private ProgressDialog progressDialog;

    //list to hold all the uploaded images
    private List<Status> uploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        progressDialog = new ProgressDialog(this);

        uploads = new ArrayList<>();

        //displaying progress dialog while fetching images
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        mDatabase = FirebaseDatabase.getInstance().getReference("users1");

        //adding an event listener to fetch values


    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //dismissing the progress dialog
                progressDialog.dismiss();
                //Iterable<DataSnapshot> children = snapshot.getChildren();

                /*for (DataSnapshot asnapshot: children){

                    String imagePath = asnapshot.child("imagePath").getValue().toString();
                    Log.e(TAG, "onDataChange: "+imagePath );
                    Status upload=asnapshot.getValue(Status.class);
                    uploads.add(upload);
                }*/
                //iterating through all the values in database
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Status upload = postSnapshot.getValue(Status.class);
                    uploads.add(upload);
                }
                Iterable<DataSnapshot> children = snapshot.getChildren();
                List<String> timeDateList=new ArrayList<String>();
                List<String> userId=new ArrayList<String>();

                timeDateList.clear();

                for (DataSnapshot asnapshot: children){

                    String timeAndDate = asnapshot.child("dat").getValue().toString();
                    String id=asnapshot.child("userid").getValue().toString();
                    Log.e(TAG, "onDataChange: "+id );
                    timeDateList.add(timeAndDate);
                    userId.add(id);

                }
                Log.e(TAG, "onDataChange:check "+timeDateList.size() );
                if(timeDateList.size()!=0) {
                    Log.e(TAG, "onDataChange:in if " );
                    Date timeToRemoveStatus=null;
                    for (int i = 0; i < timeDateList.size(); i++) {
                        Log.e(TAG, "onDataChange:in  for " );
                        Intent intent = new Intent(getApplicationContext(), DeleteFromFierbase.class);
                        intent.putExtra("Type",2);
                        intent.putExtra("Userid",userId.get(i));
                        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (i+1)+timeDateList.size(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        try {
                            Log.e(TAG, "onDataChange:in try " );
                            timeToRemoveStatus=sdf.parse(timeDateList.get(i));
                            Log.e(TAG, "onDataChange:dateandtime"+timeDateList.get(i) );
                            long priyam=timeToRemoveStatus.getTime();
                            Log.e(TAG, "onDataChange: priyam "+ String.valueOf(priyam));
                            alarm.set(AlarmManager.RTC_WAKEUP,priyam+86400000,pendingIntent);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                }
                //creating adapter
                adapter = new MyAdapter(getApplicationContext(), uploads);

                //adding adapter to recyclerview
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }
}
