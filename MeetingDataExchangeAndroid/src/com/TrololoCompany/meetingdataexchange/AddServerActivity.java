package com.TrololoCompany.meetingdataexchange;

import serverCommunicator.Communication;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;


public class AddServerActivity extends Activity {
	 private static final String  TAG="AddServerActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_server);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_server, menu);
		return true;
	}
	public void connect_2_server(View v)
	{
			Communication communication= new Communication();
			EditText ed=(EditText) findViewById(R.id.server_address_edit_text);
			Log.i(TAG,"log in procesing");
			String name=communication.getServerName(ed.getText().toString());
			if(name!=null)
				Log.i(TAG,"received name "+name);
			else
				Log.i(TAG,"connection failed");

	}
	
}
