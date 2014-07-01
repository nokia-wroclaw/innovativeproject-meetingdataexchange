package com.TrololoCompany.meetingdataexchangeServices;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import serverCommunicator.CommunicationHelper;
import serverCommunicator.FileHelper;
import serverCommunicator.MeetingHelper;

import com.TrololoCompany.meetingdataexchange.MeetingDetails;
import com.TrololoCompany.meetingdataexchangedataBase.DataBaseHelper;
import com.TrololoCompany.meetingdataexchangedataBase.FileEntity;
import com.TrololoCompany.meetingdataexchangedataBase.MeetingEntity;
import com.TrololoCompany.meetingdataexchangedataBase.ServerEntity;

import android.app.ActivityManager;
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
	private MeetingDetails activity;
	private int flag;
	private ServiceHandlers handler;
	
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
		handler=ServiceHandlers.getInstance();
		meetinghelper= new MeetingHelper();
		filehelper=new FileHelper(getApplicationContext());
		this.server=(ServerEntity)
				intent.getSerializableExtra
				("com.TrololoCompany.meetingdataexchange.server");
		this.meeting=(MeetingEntity)
				intent.getSerializableExtra
				("com.TrololoCompany.meetingdataexchange.meeting");
		this.server=new DataBaseHelper(getApplicationContext()).
				getServer(server.getServerName(), server.getLogin());
		this.meeting=new DataBaseHelper(getApplicationContext()).
				getMeetingServerId(meeting.getServerMeetingID());
		while(handler.isCommunicationServerService())
		{
			refresh();
		}
		

}

	private void refresh()
	{	
		
		
		synchronized (this) 
		{
			
		try {
			meeting=meetinghelper.getMeetingDetails(server, meeting);
			
			new DataBaseHelper(getApplicationContext()).updateMeeting(meeting);
			cleanAllFileAndCommentsAssociatedWithMeeting(meeting);
			filehelper.getFileWithCommentsList(server, meeting);
			Thread.sleep(4000);
			Log.i("service", "test");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	private  void cleanAllFileAndCommentsAssociatedWithMeeting(MeetingEntity entity)
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
