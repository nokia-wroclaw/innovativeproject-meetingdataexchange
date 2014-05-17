package asynctasks;

import org.json.JSONObject;

import serverCommunicator.CommunicationHelper;
import serverCommunicator.LogInHelper;
import serverCommunicator.NewMeetingHelper;

import com.TrololoCompany.meetingdataexchange.AddNewMeetingActivity;

import dataBase.DataBaseHelper;
import dataBase.MeetingEntity;
import dataBase.ServerEntity;
import android.os.AsyncTask;
import android.util.Log;

public class HttpPostNewMeeting extends AsyncTask<String, Void, Void> 
{
	private ServerEntity server;
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
		NewMeetingHelper helper=new NewMeetingHelper();
		CommunicationHelper communicationHelper= new CommunicationHelper();
		
		JSONObject json=helper.makeJSON2LogIn
		(server.getLogin(), server.getSid(), title, topic, permission);
		Log.i(LOG,"http://"+server.getAddress()+"/api/meeting/new");
		String respond =communicationHelper.getPostHttpRequest
		("http://"+server.getAddress()+"/api/meeting/new", json);
		String result[]=helper.parseJSONRespondRegistration(respond);
		if(result[0].contains("ok"))
		{
			MeetingEntity entity= new MeetingEntity();
			entity.setServerId(server.getId());
			entity.setServerMeetingID(Long.parseLong(result[1]));
			new DataBaseHelper(activity.getApplicationContext()).insertMeetingEntity(entity)
		}
		
		
		return null;
	}

	

	
	

}
