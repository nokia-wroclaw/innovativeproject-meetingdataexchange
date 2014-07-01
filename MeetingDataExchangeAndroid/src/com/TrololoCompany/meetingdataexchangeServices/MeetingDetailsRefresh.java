package com.TrololoCompany.meetingdataexchangeServices;

import com.TrololoCompany.meetingdataexchange.MeetingDetails;

import android.app.IntentService;
import android.content.Intent;



public class MeetingDetailsRefresh extends IntentService 
{
	/**
	 * A constructor is required, and must call the super IntentService(String)
	 * constructor with a name for the worker thread.
	 */
	
	private ServiceHandlers handler;
	private MeetingDetails act;
	public MeetingDetailsRefresh() 
	{
		
		super("MeetingDetailsRefresh");
		
	}

	/**
	 * The IntentService calls this method from the default worker thread with
	 * the intent that started the service. When this method returns,
	 * IntentService stops the service, as appropriate.
	 */
	@Override
	protected void onHandleIntent(Intent intent) 
	{
		handler=ServiceHandlers.getInstance();
		
		while(handler.isRefreshMeetingDetails())
		{
			refresh();
		}
		

}

	private void refresh()
	{	
		
	}
	
}
