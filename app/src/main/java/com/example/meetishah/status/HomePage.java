package com.example.meetishah.status;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

public class HomePage extends AppCompatActivity {

    SharedPreferences sp;
    Intent i;
    ListView statusListView;
    List<Status> statusList;
    String TAG="HomePage";
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar myToolbar =  findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        sp=getSharedPreferences("Login", Context.MODE_PRIVATE);
        mAuth=FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Status");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        statusListView=findViewById(R.id.listView);
        statusList=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("users");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:

                SharedPreferences.Editor e=sp.edit();
                if(sp.contains("email")) {
                    e.remove("email");
                    e.apply();
                }
                if(mAuth.getCurrentUser()!=null){
                    mAuth.signOut();
                    mGoogleSignInClient.signOut().addOnCompleteListener(this,
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    i=new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(i);
                                }
                            });
                }

                i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_add_new:
                i=new Intent(getApplicationContext(),StatusUpdate.class);
                startActivity(i);

                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;
            case R.id.add_photo:
                i=new Intent(getApplicationContext(),PhotoUpdate.class);
                startActivity(i);
                return true;
            case R.id.timeline:
                i=new Intent(getApplicationContext(),ShowImagesActivity.class);
                startActivity(i);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                statusList.clear();
                for(DataSnapshot status:dataSnapshot.getChildren()){
                    // DatabaseReference time=databaseReference.child("users").child("time");
                    //String value=dataSnapshot.child("users").child("time").getValue(String.class);
                    //Log.e(TAG, "onDataChange: "+value );
                    Status s=status.getValue(Status.class);
                    statusList.add(s);

                }
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                List<String> timeDateList=new ArrayList<String>();
                List<String> userId=new ArrayList<String>();

                timeDateList.clear();

                for (DataSnapshot snapshot: children){

                    String timeAndDate = snapshot.child("dat").getValue().toString();
                    String id=snapshot.child("userid").getValue().toString();
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
                        intent.putExtra("Type",1);
                        intent.putExtra("Userid",userId.get(i));
                        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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


                StatusList adapter=new StatusList(HomePage.this,statusList);
                statusListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
