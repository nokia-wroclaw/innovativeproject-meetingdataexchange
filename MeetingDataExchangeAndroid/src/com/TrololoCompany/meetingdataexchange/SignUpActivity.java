package com.TrololoCompany.meetingdataexchange;

import com.TrololoCompany.meetingdataexchangedataBase.ServerEntity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import asynctasks.HttpPostSignUp;

public class SignUpActivity extends Activity {

	private String name;
	private String address;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		Intent intent = getIntent();
		name=intent.getStringExtra("name");
		address=intent.getStringExtra("address");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}
	public void sign_up(View v)
	{
		EditText loginEd=(EditText) findViewById(R.id.sign_up_log_ed);
		EditText nickEd=(EditText) findViewById(R.id.sign_up_nick_ed);
		EditText emailEd=(EditText) findViewById(R.id.sign_up_email_ed);
		EditText passwordEd=(EditText) findViewById(R.id.sign_up_pass);
		EditText password_repEd=(EditText) findViewById(R.id.sign_up_pass_rep);
		String login=loginEd.getText().toString();
		String nick=nickEd.getText().toString();
		String email=emailEd.getText().toString();
		String password=passwordEd.getText().toString();
		String password_rep=password_repEd.getText().toString();
		Toast toast = Toast.makeText(getApplicationContext(), 
				"registration processing", Toast.LENGTH_LONG);
		toast.show();
		new HttpPostSignUp(this).execute(name,address,login,nick,email,password);
	}
	public void registrationFailed(String message)
	{
		Toast toast = Toast.makeText(getApplicationContext(), 
				"registration failed , reason:"+message, Toast.LENGTH_LONG);
		toast.show();
	}
	public void registrationSuccess()
	{
		Toast toast = Toast.makeText(getApplicationContext(), 
				"registration successful", Toast.LENGTH_LONG);
		toast.show();
		Intent intent = new Intent(this, ServerManageActivity.class);
		finish();
	    startActivity(intent);
		
	}
}
