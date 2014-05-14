package com.TrololoCompany.meetingdataexchange;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import asynctasks.HttpPostRequestLogIn;

public class LogInActivity extends Activity {

	private String name;
	private String address;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);
		Intent intent = getIntent();
		name=intent.getStringExtra("name");
		address=intent.getStringExtra("address");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.log_in, menu);
		return true;
	}
	public void logIn(View v)
	{
		EditText logEd=(EditText) findViewById(R.id.log_in_login_ed);
		EditText passwdEd=(EditText) findViewById(R.id.logIn_passwd_ed);
		String login=logEd.getText().toString();
		String passwd=passwdEd.getText().toString();
		/*should check here is that server already exists ??*/
		new HttpPostRequestLogIn(this).execute(address,name,login,passwd);
		
	}
	public void displayMessage(String message)
	{
		Toast toast = Toast.makeText(getApplicationContext(), 
				message, Toast.LENGTH_LONG);
		toast.show();
	}
	public void finishAndGoToList()
	{
		displayMessage("meetings downloaded");
		Intent intent = new Intent(this, ServerManageActivity.class);
		finish();
	    startActivity(intent);
	}
	

}
