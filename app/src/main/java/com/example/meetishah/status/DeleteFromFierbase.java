package com.example.meetishah.status;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteFromFierbase extends BroadcastReceiver {
    DatabaseReference dr;

    @Override
    public void onReceive(Context context, Intent intent) {
        int intentData=intent.getIntExtra("Type",0);
        String userId=intent.getStringExtra("Userid");
        String temp;
        if(intentData==1){
            temp="users";
        }else{
            temp="users1";
        }
        dr= FirebaseDatabase.getInstance().getReference(temp).child(userId);
        dr.removeValue();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("Notification removed");
        mBuilder.setContentText("Since it has been 24 hours your notification is removed");
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder.setVibrate(new long[]{1000,1000});
// notificationID allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());

    }
}

