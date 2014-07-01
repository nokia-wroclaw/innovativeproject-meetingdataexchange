package serverCommunicator;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.TrololoCompany.meetingdataexchangedataBase.MeetingEntity;
import com.TrololoCompany.meetingdataexchangedataBase.ServerEntity;


import android.util.Log;

public class MeetingHelper 
{
	private static final String LOG="MeetingHelper";
	public JSONObject makeJSON2
	(String login,String sid,String title,String topic,String permission)
	{	
		JSONObject json= new JSONObject();
		try {
			json.accumulate("login", login);
			json.accumulate("sid", sid);
			json.accumulate("title", title);
			json.accumulate("topic", topic);
			json.accumulate("abilityToSendFiles", permission);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	public synchronized MeetingEntity getMeetingDetails(ServerEntity server,MeetingEntity meeting) throws ClientProtocolException, IOException
	{
		
		Log.i("meeting helper before request",meeting.getTitle()+" "+meeting.getID()+" "+meeting.getServerMeetingID());
		MeetingEntity result_meeting=null;
		CommunicationHelper communicationHelper= new CommunicationHelper();
		String address="http://"+server.getAddress()+"/api/meeting/details/"+
		meeting.getServerMeetingID()+"/"+server.getLogin()+"/"+server.getSid();
		String result=communicationHelper.getHttpGetRequest(address);
		String data[]=parseJSONRespond(result);
		if(data.length>2)
		{
			result_meeting=makeMeeting(data, server);
			
		}
		result_meeting.setID(meeting.getID());
		return result_meeting;

	}
	public MeetingEntity makeMeeting(String input[],ServerEntity server)
	{
		for(int i=0;i<input.length;i++)
		{
			Log.i(LOG,i+" "+input[i]);
		}
		MeetingEntity meeting= new MeetingEntity();
		meeting.setServerId(server.getId());
		meeting.setServerMeetingID(Long.parseLong(input[1]));
		meeting.setTitle(input[2]);
		meeting.setTopic(input[3]);
		meeting.setHostName(input[4]);
		meeting.setStartTime(input[5]);
		meeting.setEndTime(input[6]);
		meeting.setNumberOfMembers(Integer.parseInt(input[7]));
		meeting.setPermission(input[8]);
		meeting.setCode(input[9]);
		
		return meeting;
	}
	public String[] parseJSONRespond(String input)
	{
		JSONObject json;
		String result[] = null ;
		try {
			json = new JSONObject(input);
			Log.i(LOG,"respond received");
			String status=json.getString("status");
			
			Log.i(LOG,"status "+status);
			if(status.contains("failed"))
			{
				result=new String[2];
				result[0]=status;
				result[1]=json.getString("reason");
				Log.i(LOG,"reason "+result[1]);
			}
			else if(status.contains("ok"))
			{
				result=new String[10];
				result[0]=status;
				result[1]=json.getString("meetingid");
				result[2]=json.getString("title");
				result[3]=json.getString("topic");
				result[4]=json.getString("hostname");
				result[5]=json.getString("starttime");
				result[6]=json.getString("endtime");
				result[7]=json.getString("members");
				result[8]=json.getString("permissions");
				result[9]=json.getString("accessCode");

				Log.i(LOG,"ok");
			}
					
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return result;
	}
	
	

	
}
