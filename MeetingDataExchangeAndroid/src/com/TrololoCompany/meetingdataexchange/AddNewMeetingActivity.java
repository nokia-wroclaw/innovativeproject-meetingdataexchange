package com.TrololoCompany.meetingdataexchange;

import dataBase.ServerEntity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import asynctasks.HttpPostNewMeeting;

public class AddNewMeetingActivity extends Activity {
	private ServerEntity server;
	private String title;
	private String topic;
	private boolean permission;
	private static final String LOG="AddNewMeetingActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_meeting);
		Log.i(LOG,"created ");
		TextView serverName=(TextView) findViewById(R.id.add_new_meeting_textView_name);
		Log.i(LOG,"serverNameTextView capture ");
		Intent intent=getIntent();
		this.server=(ServerEntity) intent.getSerializableExtra("com.TrololoCompany.meetingdataexchange.ser");
		serverName.setText(server.getServerName());
		Log.i(LOG,"got server ");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_new_meeting, menu);
		return true;
	}
	private void getDataFromGUI()
	{
		EditText title=(EditText) findViewById(R.id.add_new_meeting_title_ed);
		EditText topic=(EditText) findViewById(R.id.add_new_meeting_thema);
		CheckBox permission=(CheckBox) findViewById(R.id.add_new_meeting_permission_check);
		this.title=title.getText().toString();
		this.topic=topic.getText().toString();
		this.permission=permission.hasSelection();
	}
	public void add_new_meeting(View view)
	{
		getDataFromGUI();
		new HttpPostNewMeeting(server,this).execute(title,topic,permission+"");
	}
}
