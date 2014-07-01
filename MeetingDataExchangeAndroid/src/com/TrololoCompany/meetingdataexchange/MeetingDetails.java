package com.TrololoCompany.meetingdataexchange;

import java.io.File;

import meeting_options.MeetingProgress;
import meeting_options.MyOnPageChangeListener;
import meeting_options.MyTabListener;
import meeting_options.TabsPagerAdapter;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import asynctasks.HttpPutSendFile;

import com.TrololoCompany.meetingdataexchangeServices.MeetingServerCommunication;
import com.TrololoCompany.meetingdataexchangeServices.ServiceHandlers;
import com.TrololoCompany.meetingdataexchangedataBase.MeetingEntity;
import com.TrololoCompany.meetingdataexchangedataBase.ServerEntity;

import fileMaintenance.FileMaintenance;


public class MeetingDetails extends FragmentActivity  {
 
	private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private ServerEntity server;
    private MeetingEntity meeting;
    private String[] tabs = { "Opis", "Przebieg spotkania","Dodaj"};
    private Intent service;
    private ServiceHandlers handler;
    private File photo_file;
    private static int  PHOTO_REQUEST=1;



   
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);
        Intent intent=getIntent();
        this.server=(ServerEntity) intent.getSerializableExtra("com.TrololoCompany.meetingdataexchange.server");
		this.meeting=(MeetingEntity)intent.getSerializableExtra("com.TrololoCompany.meetingdataexchange.meeting");
		initGUI();
		
		
		service = new Intent(this, MeetingServerCommunication.class);
    	Bundle bundle = new Bundle();
		bundle.putSerializable("com.TrololoCompany.meetingdataexchange.meeting", meeting);
		bundle.putSerializable("com.TrololoCompany.meetingdataexchange.server", server);
		service.putExtras(bundle);
		handler=ServiceHandlers.getInstance();
		Log.i("meeting details cur item",viewPager.getCurrentItem()+"");
		// Initilization
        
        
    }
    @Override
    protected void onResume() {
    	handler.setCommunicationServerService(true);
        startService(service);
    
    	super.onResume();
    }
    @Override
    protected void onPause() 
    {
    	handler.setCommunicationServerService(false);
    	stopService(service);
    	super.onPause();
    }
    public void  add_photo_Button(View v)
    {
  	  Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
  	  photo_file = new FileMaintenance().makeFileForPhoto(server, meeting);
  	        // Continue only if the File was successfully created
  	  takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
  	                    Uri.fromFile(photo_file));
  	Log.i("meeting details cur item",viewPager.getCurrentItem()+"");
  	  takePictureIntent.putExtra("com.TrololoCompany.meetingdataexchange.file",photo_file);
  	   startActivityForResult(takePictureIntent, PHOTO_REQUEST);
  	   
  	    
    }
    public void  addUser(View v)
    {
    	//
		Intent intent = new Intent(this,DisplayQR.class);
		intent.putExtra("com.TrololoCompany.meetingdataexchange.server",server);
		intent.putExtra("com.TrololoCompany.meetingdataexchange.meeting",meeting);
		startActivity(intent);
  	   
  	    
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {

	    if (requestCode == PHOTO_REQUEST) 
	    {
	    	Log.i("MeetingDetails","onActivityResult");
	        if(resultCode == RESULT_OK)
	        {
	        	Log.i("MeetingDetails","result ok");
	        	new HttpPutSendFile(photo_file, server, meeting).execute();
	       
	        }
	        
	    }
    }
	private void addTabsAndListeners()
    {
    	for(int i=0;i<3;i++)
        {
        	Tab tab=actionBar.newTab();
        	tab.setText(tabs[i]);
        	tab.setTabListener(new MyTabListener(viewPager));
        	actionBar.addTab(tab);
        }
    }
    private void initGUI()
    {
    	viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        
        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);  
        addTabsAndListeners();
        
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener(actionBar));
    }
    public ServerEntity getServer() {
		return server;
	}
	public void setServer(ServerEntity server) {
		this.server = server;
	}
	public MeetingEntity getMeeting() {
		return meeting;
	}
	public void setMeeting(MeetingEntity meeting) {
		this.meeting = meeting;
	}
	



 
}
