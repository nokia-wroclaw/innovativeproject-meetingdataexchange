package com.TrololoCompany.meetingdataexchangeAdapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.TrololoCompany.meetingdataexchange.R;
import com.TrololoCompany.meetingdataexchangedataBase.MeetingEntity;
import com.TrololoCompany.meetingdataexchangedataBase.ServerEntity;


public class MeetingAdapter extends ArrayAdapter<MeetingEntity> 
{
	public MeetingAdapter(Context context, ArrayList<MeetingEntity> users) 
	{
	       super(context, R.layout.item_meeting, users);
	 }
	 @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	       // Get the data item for this position
		 MeetingEntity meeting = getItem(position);    
	       // Check if an existing view is being reused, otherwise inflate the view
	       if (convertView == null) {
	          convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_meeting, parent, false);
	       }
	       // Lookup view for data population
	       TextView meetingTitle = (TextView) convertView.findViewById(R.id.meetingTitle);
	       TextView meetingTime = (TextView) convertView.findViewById(R.id.meetingTime);
	      // TextView login= (TextView) convertView.findViewById(R.id.login);
	      
	       // Populate the data into the template view using the data object
	       meetingTitle.setText(meeting.getTitle());
	       meetingTime.setText(meeting.getStartTime());
	      // login.setText(server.getLogin());
	       // Return the completed view to render on screen
	       return convertView;
	   }
	
	
}
