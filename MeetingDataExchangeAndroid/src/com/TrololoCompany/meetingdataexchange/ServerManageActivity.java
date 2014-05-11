package com.TrololoCompany.meetingdataexchange;

import java.util.ArrayList;

import com.TrololoCompany.meetingdataexchangeAdapters.ServerAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import dataBase.Server;

public class ServerManageActivity extends Activity 
{
	private ArrayList<Server> arrayOfServers;
	private ServerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_manage);
		// Construct the data source
		this.arrayOfServers = new ArrayList<Server>();
		// Create the adapter to convert the array to views
		this.adapter = new ServerAdapter(this, arrayOfServers);
		// Attach the adapter to a ListView
		ListView listView = (ListView) findViewById(R.id.serverList);
		listView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.server_manage, menu);
		return true;
	}
	public void add_new_server(View v)
	{
			Intent intent = new Intent(this, AddServerActivity.class);
		    startActivity(intent);
	}

}
