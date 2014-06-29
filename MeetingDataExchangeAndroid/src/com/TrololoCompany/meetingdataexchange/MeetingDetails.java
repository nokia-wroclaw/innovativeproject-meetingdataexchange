package com.TrololoCompany.meetingdataexchange;

import com.TrololoCompany.meetingdataexchangeServices.MeetingServerCommunication;
import com.TrololoCompany.meetingdataexchangedataBase.MeetingEntity;
import com.TrololoCompany.meetingdataexchangedataBase.ServerEntity;

import meeting_options.MeetingDescription;
import meeting_options.MyOnPageChangeListener;
import meeting_options.MyTabListener;
import meeting_options.TabsPagerAdapter;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;


public class MeetingDetails extends FragmentActivity {
 
	private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private ServerEntity server;
    private MeetingEntity meeting;
    private String[] tabs = { "Opis", "Przebieg spotkania","Dodaj"};
   
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);
        Intent intent=getIntent();
        this.server=(ServerEntity) intent.getSerializableExtra("com.TrololoCompany.meetingdataexchange.server");
		this.meeting=(MeetingEntity)intent.getSerializableExtra("com.TrololoCompany.meetingdataexchange.meeting");
																
		// Initilization
        initGUI();
        
    }
    @Override
    protected void onResume() 
    {
    	try{
    	Intent intent = new Intent(this, MeetingServerCommunication.class);
    	Bundle bundle = new Bundle();
		bundle.putSerializable("com.TrololoCompany.meetingdataexchange.meeting", meeting);
		bundle.putSerializable("com.TrololoCompany.meetingdataexchange.server", server);
		
		intent.putExtras(bundle);
        startService(intent);
        super.onResume();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    };
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
