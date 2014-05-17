package com.TrololoCompany.meetingdataexchangeAdapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.TrololoCompany.meetingdataexchange.R;

import dataBase.ServerEntity;

public class ServerAdapter extends ArrayAdapter<ServerEntity> 
{
	public ServerAdapter(Context context, ArrayList<ServerEntity> users) 
	{
	       super(context, R.layout.item_server, users);
	 }
	 @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	       // Get the data item for this position
		 ServerEntity server = getItem(position);    
	       // Check if an existing view is being reused, otherwise inflate the view
	       if (convertView == null) {
	          convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_server, parent, false);
	       }
	       // Lookup view for data population
	       TextView serverName = (TextView) convertView.findViewById(R.id.serverName);
	       TextView introduceName = (TextView) convertView.findViewById(R.id.introduceName);
	      // TextView login= (TextView) convertView.findViewById(R.id.login);
	      
	       // Populate the data into the template view using the data object
	       serverName.setText(server.getServerName());
	       introduceName.setText(server.getAddress());
	      // login.setText(server.getLogin());
	       // Return the completed view to render on screen
	       return convertView;
	   }
	
	
}
