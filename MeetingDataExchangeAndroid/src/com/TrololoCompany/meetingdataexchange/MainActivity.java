package com.TrololoCompany.meetingdataexchange;

import serverCommunicator.CommunicationHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import dataBase.DataBaseHelper;

public class MainActivity extends Activity {

	private static final String log="MainActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	public void create_new_meeting(View v) 
	{
		
		Intent intent = new Intent(this, ServerList.class);
	    startActivity(intent);
		
	}
	public void join_meeting(View v) 
	{
		
		
		
	}
	public void show_meetings(View v) 
	{
		
		
		
	}
	public void manage_servers(View v)
	{
			Intent intent = new Intent(this, ServerManageActivity.class);
		    startActivity(intent);
		
		
	}

}
