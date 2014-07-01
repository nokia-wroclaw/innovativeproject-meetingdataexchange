package asynctasks;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;

import com.TrololoCompany.meetingdataexchange.LogInActivity;
import com.TrololoCompany.meetingdataexchangedataBase.DataBaseHelper;
import com.TrololoCompany.meetingdataexchangedataBase.MeetingEntity;
import com.TrololoCompany.meetingdataexchangedataBase.ServerEntity;

import fileMaintenance.FileMaintenance;


import serverCommunicator.CommunicationHelper;
import serverCommunicator.GetMeetingListHelper;

import android.os.AsyncTask;
import android.util.Log;

public class HttpGetListMeetings extends AsyncTask<String, Void, Void>
{
	private static final String LOG="HttpGetListMeetings";
	private String address;
	private String name;
	private String login;
	private String password;
	private String sid;
	private LogInActivity activity;
	private ServerEntity server;
	
	public HttpGetListMeetings(LogInActivity activity,ServerEntity server)
	{
		this.activity=activity;
		this.server=server;
	}
	
	@Override
	protected Void doInBackground(String... arg0)
	{
		//arg0[0] -address
		//arg0[1] -name
		//arg0[2] -login
		//arg0[3] -password
		//arg0[4] -sid
		address=arg0[0];
		name=arg0[1];
		login=arg0[2];
		password=arg0[3];
		sid=arg0[4];
		CommunicationHelper communication = new CommunicationHelper();
		GetMeetingListHelper helper= new GetMeetingListHelper();
		try {
			Log.i(LOG,"http://"+address+"/api/meeting/list/"+login+"+/"+sid);
			String respond=communication.getHttpGetRequest("http://"+address+"/api/meeting/list/"+login+"/"+sid);
			long serverId = new DataBaseHelper(activity.getApplicationContext()).getServerId(name, login);
			Log.i(LOG,"found server with ID "+serverId);
			ArrayList<MeetingEntity> result=helper.parseJSONRespondGetMeetings(respond,serverId);
			Log.i(LOG,"array size "+result.size());
			for(int i=0;i<result.size();i++)
			{
				new DataBaseHelper(activity.getApplicationContext()).
				insertMeetingEntity(result.get(i));
				new FileMaintenance().makeMeetingFile(server, result.get(i));
				Log.i(LOG,"meeting  "+i+" added");
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onPostExecute(Void result) 
	{
		
		activity.finishAndGoToList();
		
	}

}
