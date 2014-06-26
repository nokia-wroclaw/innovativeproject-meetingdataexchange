package asynctasks;

import org.json.JSONObject;

import serverCommunicator.CommunicationHelper;
import serverCommunicator.LogInHelper;
import serverCommunicator.MeetingHelper;

import com.TrololoCompany.meetingdataexchange.AddNewMeetingActivity;
import com.TrololoCompany.meetingdataexchangedataBase.DataBaseHelper;
import com.TrololoCompany.meetingdataexchangedataBase.MeetingEntity;
import com.TrololoCompany.meetingdataexchangedataBase.ServerEntity;

import android.os.AsyncTask;
import android.util.Log;

public class HttpPostNewMeeting extends AsyncTask<String, Void, Void> 
{
	private ServerEntity server;
	private MeetingEntity meeting;
	private AddNewMeetingActivity activity;
	private static final String LOG="HttpPostNewMeeting";
	public HttpPostNewMeeting(ServerEntity server,AddNewMeetingActivity activity)
	{
		this.server=server;
		this.activity=activity;
	}
	@Override
	protected Void doInBackground(String... params)
	{
		String title=params[0];
		String topic=params[1];
		String permission=params[2];
		MeetingHelper helper=new MeetingHelper();
		CommunicationHelper communicationHelper= new CommunicationHelper();
		
		JSONObject json=helper.makeJSON2
		(server.getLogin(), server.getSid(), title, topic, permission);
		
		Log.i(LOG,"http://"+server.getAddress()+"/api/meeting/new");
		
		String respond =communicationHelper.getPostHttpRequest
		("http://"+server.getAddress()+"/api/meeting/new", json);
		Log.i(LOG,"respond received");
		String result[]=helper.parseJSONRespond(respond);
		Log.i(LOG,"json parsed");
		if(result[0].contains("ok"))
		{
			Log.i(LOG,"start making meeting");
			this.meeting =helper.makeMeeting(result, server);
			Log.i(LOG,"meeting done");
			new DataBaseHelper
			(activity.getApplicationContext()).
			insertMeetingEntity(meeting);
			Log.i(LOG,"Meeting added");
		}
	
		
		return null;
	}
	@Override
	protected void onPostExecute(Void result) 
	{
		Log.i(LOG,"pred");
		activity.meeting_added(meeting,server);
		Log.i(LOG,"post");
	}


	
	

}
