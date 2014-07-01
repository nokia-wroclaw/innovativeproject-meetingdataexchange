package com.TrololoCompany.meetingdataexchange;

import lists.MeetingList;
import lists.ServerList;

import com.TrololoCompany.meetingdataexchangedataBase.DataBaseHelper;
import com.TrololoCompany.meetingdataexchangedataBase.MeetingEntity;
import com.TrololoCompany.meetingdataexchangedataBase.ServerEntity;

import fileMaintenance.FileMaintenance;

import serverCommunicator.CommunicationHelper;
import serverCommunicator.FileHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final String log="MainActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FileMaintenance file_maintenance= new FileMaintenance();
		file_maintenance.makeRootFile(getApplicationContext());
		Log.i(log,"onCreate");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		
		return true;
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	    if (requestCode == 1) 
	    {
	        if(resultCode == RESULT_OK)
	        {
	    		ServerEntity server=(ServerEntity)
	    				data.getSerializableExtra("com.TrololoCompany.meetingdataexchange.server");
	    		Intent intent = new Intent(this,AddNewMeetingActivity.class);
	    		intent.putExtra("com.TrololoCompany.meetingdataexchange.ser",server);
	    		startActivityForResult(intent, 2);
	       
	        }
	        
	    }
	    else if(requestCode==2)
	    {
	    	if(resultCode == RESULT_OK)
	        {
	    		ServerEntity server=(ServerEntity)
	    				data.getSerializableExtra("com.TrololoCompany.meetingdataexchange.server");
	    		MeetingEntity meeting=(MeetingEntity)
	    				data.getSerializableExtra("com.TrololoCompany.meetingdataexchange.meeting");
	    		Intent intent = new Intent(this,MeetingDetails.class);
	    		Bundle bundle = new Bundle();
				bundle.putSerializable("com.TrololoCompany.meetingdataexchange.server", server);
				bundle.putSerializable("com.TrololoCompany.meetingdataexchange.meeting", meeting);
				intent.putExtras(bundle);
				startActivity(intent);
	    		
	       
	        }
	    	
	    	
	    }
	    else if (requestCode == 3) 
	    {
	        if(resultCode == RESULT_OK)
	        {
	        	ServerEntity server=(ServerEntity)
	    				data.getSerializableExtra("com.TrololoCompany.meetingdataexchange.server");
	    		
	        	Intent intent = new Intent(this, MeetingList.class);
	    		Bundle bundle = new Bundle();
	    		bundle.putSerializable("com.TrololoCompany.meetingdataexchange.server", server);
	    		intent.putExtras(bundle);
	    		startActivityForResult(intent, 4);
	    	
	    		
	    		
	        }
	        
	    }
	    else if (requestCode == 4) 
	    {
	        if(resultCode == RESULT_OK)
	        {
	        	ServerEntity server=(ServerEntity)
	    				data.getSerializableExtra("com.TrololoCompany.meetingdataexchange.server");
	        	MeetingEntity meeting=(MeetingEntity)
	    				data.getSerializableExtra("com.TrololoCompany.meetingdataexchange.meeting");
	        	Intent intent = new Intent(this, MeetingDetails.class);
	    		Bundle bundle = new Bundle();
	    		bundle.putSerializable("com.TrololoCompany.meetingdataexchange.server", server);
	    		bundle.putSerializable("com.TrololoCompany.meetingdataexchange.meeting", meeting);
	    		intent.putExtras(bundle);
	    	    startActivity(intent);
	    		
	    		
	    		
	        }
	        
	    }
	    
	}
	public void create_new_meeting(View v) 
	{
		
		Intent intent = new Intent(this, ServerList.class);
	    startActivityForResult(intent, 1);
		
	}
	public void join_meeting(View v) 
	{
		
		
		
	}
	public void show_meetings(View v) 
	{
		
		Intent intent = new Intent(this, ServerList.class);
	    startActivityForResult(intent, 3);
		
	}
	public void manage_servers(View v)
	{
			Intent intent = new Intent(this, ServerManageActivity.class);
		    startActivity(intent);
		
		
	}

}
