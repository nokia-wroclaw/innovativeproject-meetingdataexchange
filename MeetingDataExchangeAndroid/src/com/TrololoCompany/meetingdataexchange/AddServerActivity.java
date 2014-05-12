package com.TrololoCompany.meetingdataexchange;

import serverCommunicator.CheckIsServerExists;
import serverCommunicator.Communication;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


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
			EditText ed=(EditText) findViewById(R.id.server_address_edit_text);
			Log.i(TAG,"log in procesing");
			Toast toast = Toast.makeText(getApplicationContext(), 
					"log in procesing!", Toast.LENGTH_SHORT);
			toast.show();
			Log.i("CheckIsServerExists","connection failed");
			new CheckIsServerExists(this).
			execute(ed.getText().toString());
	}
	public void connection_succes(String name)
	{
		Intent intent = new Intent(this, AddServerActivitySuccess.class);
		intent.putExtra("name", name);
	    startActivity(intent);
		
	}
	public void connection_failed()
	{
		Toast toast = Toast.makeText(getApplicationContext(), 
				"connection failed!", Toast.LENGTH_SHORT);
		toast.show();
		Log.i("CheckIsServerExists","connection failed");
	}
	
}
