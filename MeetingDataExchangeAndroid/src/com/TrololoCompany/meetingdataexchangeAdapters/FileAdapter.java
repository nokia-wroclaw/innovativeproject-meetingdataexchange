package com.TrololoCompany.meetingdataexchangeAdapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.TrololoCompany.meetingdataexchange.R;
import com.TrololoCompany.meetingdataexchangedataBase.FileEntity;


public class FileAdapter extends ArrayAdapter<FileEntity> 
{
	public FileAdapter(Context context, ArrayList<FileEntity> files) 
	{
	       super(context, R.layout.item_file, files);
	 }
	 @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	       // Get the data item for this position
		 FileEntity file = getItem(position);    
	       // Check if an existing view is being reused, otherwise inflate the view
	       if (convertView == null) {
	          convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_file, parent, false);
	       }
	       // Lookup view for data population
	       TextView fileName=(TextView) convertView.findViewById(R.id.fileName);
	       TextView fileLastActivity= (TextView) convertView.findViewById(R.id.fileLastActivity);
	      // TextView login= (TextView) convertView.findViewById(R.id.login);
	      
	       // Populate the data into the template view using the data object
	       fileName.setText(file.getFileName());
	       fileLastActivity.setText("dodano "+file.getAddTime());

	      // login.setText(server.getLogin());
	       // Return the completed view to render on screen
	       return convertView;
	   }
	
	
}
