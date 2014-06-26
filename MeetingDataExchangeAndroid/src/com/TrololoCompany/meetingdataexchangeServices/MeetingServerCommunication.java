package com.TrololoCompany.meetingdataexchangeServices;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import serverCommunicator.CommunicationHelper;
import serverCommunicator.FileHelper;
import serverCommunicator.MeetingHelper;

import com.TrololoCompany.meetingdataexchangedataBase.DataBaseHelper;
import com.TrololoCompany.meetingdataexchangedataBase.FileEntity;
import com.TrololoCompany.meetingdataexchangedataBase.MeetingEntity;
import com.TrololoCompany.meetingdataexchangedataBase.ServerEntity;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class MeetingServerCommunication extends IntentService 
{
	/**
	 * A constructor is required, and must call the super IntentService(String)
	 * constructor with a name for the worker thread.
	 */
	private MeetingEntity meeting;
	private ServerEntity server;
	private MeetingHelper meetinghelper;
	private FileHelper filehelper;
	public MeetingServerCommunication() 
	{
		
		super("MeetingServerCommunication");
		
	}

	/**
	 * The IntentService calls this method from the default worker thread with
	 * the intent that started the service. When this method returns,
	 * IntentService stops the service, as appropriate.
	 */
	@Override
	protected void onHandleIntent(Intent intent) 
	{
		meetinghelper= new MeetingHelper();
		filehelper=new FileHelper();
		this.server=(ServerEntity)
				intent.getSerializableExtra
				("com.TrololoCompany.meetingdataexchange.server");
		this.meeting=(MeetingEntity)
				intent.getSerializableExtra
				("com.TrololoCompany.meetingdataexchange.meeting");
		
		for(int i=0;i<10;i++)
		{
			try 
			{		meeting=meetinghelper.getMeetingDetails(server, meeting);
					new DataBaseHelper(getApplicationContext()).updateMeeting(meeting);
					meeting=new DataBaseHelper(getApplicationContext()).
							getMeetingServerId(meeting.getServerId());
					cleanAllFileAndCommentsAssociatedWithMeeting(meeting);
					
					
					Thread.sleep(2000);
					Log.i("service", "test");
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				Log.i("service", "error");
			}
		}
		

}

	
	private void cleanAllFileAndCommentsAssociatedWithMeeting(MeetingEntity entity)
	{
		
		ArrayList<FileEntity> files=new DataBaseHelper(getApplicationContext()).
									getFileAssociatedWithMeeting(meeting.getID());
		for(int i=0;i<files.size();i++)
		{
			new DataBaseHelper(getApplicationContext()).
							deleteAllCommentsAssociatedWithMeeting(files.get(i));
		}
		new DataBaseHelper(getApplicationContext()).
				deleteAllFileAssociatedWithMeeting(entity);
	}
}
