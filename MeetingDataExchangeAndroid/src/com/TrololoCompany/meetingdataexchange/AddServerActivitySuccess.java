package com.TrololoCompany.meetingdataexchange;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddServerActivitySuccess extends Activity {

	private String name;
	private String address;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_server_activity_success);
		Intent intent = getIntent();
		this.name=intent.getStringExtra("name");
		this.address=intent.getStringExtra("address");
		TextView ed=(TextView) findViewById(R.id.add_new_server_succ_text3);
		ed.setText(ed.getText()+": "+name);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_server_activity_success, menu);
		return true;
	}
	public void log_in(View v)
	{
		Intent intent = new Intent(this, LogInActivity.class);
		
		intent.putExtra("name", name);
		intent.putExtra("address", address);
	    startActivity(intent);
	
	}
	public void sign_up(View v)
	{
		Intent intent = new Intent(this, SignUpActivity.class);
		intent.putExtra("name", name);
		intent.putExtra("address", address);
	    startActivity(intent);
	}
}
