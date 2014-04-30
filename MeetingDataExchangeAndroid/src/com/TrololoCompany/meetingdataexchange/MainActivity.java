package com.TrololoCompany.meetingdataexchange;

import dataBase.DataBaseHelper;
import dataBase.ServerEntity;
import serverCommunicator.Communication;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	private static final String log="MainActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		DataBaseHelper base = new DataBaseHelper(getApplicationContext());
		
		//Log.i("is read",base.getReadableDatabase()+"");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

	public void join_meeting(View v) 
	{
		//temporary string format IP:Port;MeetingId
		String temp="192.168.1.117:9000;MeetingId";
		Communication communication = new Communication(temp,getApplicationContext());
		communication.join2Meeting();
		
		
	}

}
