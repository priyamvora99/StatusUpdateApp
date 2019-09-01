package com.example.meetishah.status;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StatusUpdate extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences sp;
    EditText status;
    Button post;
    String TAG="StatusUpdate";
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_update);
        setTitle("Set Status");
        mAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("users");
        sp=getSharedPreferences("Login", Context.MODE_PRIVATE);
        status=(EditText)findViewById(R.id.statusUpdateEditText);
        post=(Button)findViewById(R.id.statusUpdateButton);
        post.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final String textStatus=status.getText().toString();
        if(TextUtils.isEmpty(textStatus)){
            status.setError("Please fill in a status to post");
        }else{

            // User is signed in
            SimpleDateFormat date= new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat time=new SimpleDateFormat("HH:mm");
            SimpleDateFormat dateandtime=new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date da=new Date();
            String statusTime=time.format(da);
            String statusDate=date.format(da);
            String statusDateAndTime=dateandtime.format(da);
            String id=databaseReference.push().getKey();
            String email=null;
            if(sp.contains("email")){
             email=sp.getString("email",null);
            }
            if(mAuth.getCurrentUser()!=null){
                FirebaseUser user;
                user=mAuth.getCurrentUser();
                email=user.getEmail();
            }
            Status s=new Status(email,id,textStatus,statusDate,statusTime,statusDateAndTime);

            databaseReference.child(id).setValue(s);

            Toast.makeText(getApplicationContext(),"Status posted", Toast.LENGTH_LONG).show();
            Intent i=new Intent(getApplicationContext(),HomePage.class);
            startActivity(i);

        }
    }

}

