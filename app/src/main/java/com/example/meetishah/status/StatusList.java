package com.example.meetishah.status;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class StatusList extends ArrayAdapter<Status> {
    Activity context;
    List<Status> statusList;

    public StatusList(Activity context, List<Status> statusList){
        super(context, R.layout.user_list,statusList);
        this.context=context;
        this.statusList=statusList;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View listViewItem=inflater.inflate(R.layout.user_list,null,true);
        TextView statusBy=(TextView)listViewItem.findViewById(R.id.postedBy);
        TextView status=(TextView)listViewItem.findViewById(R.id.textStatus);
        TextView date=(TextView)listViewItem.findViewById(R.id.statusDate);
        TextView time=(TextView)listViewItem.findViewById(R.id.statusTime);
        Status s=statusList.get(position);
        statusBy.setText("Status posted by: "+s.getEmail());
        status.setText("Status: "+ s.getStatus());
        date.setText("Status set on date: "+s.getDate());
        time.setText("Status set at: "+ s.getTime());
        return listViewItem;
        //return super.getView(position, convertView, parent);


    }
}