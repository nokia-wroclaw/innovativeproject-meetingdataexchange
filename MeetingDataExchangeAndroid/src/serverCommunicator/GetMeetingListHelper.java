package serverCommunicator;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dataBase.MeetingEntity;

import android.util.Log;

public class GetMeetingListHelper 
{
	private static final String LOG="GetMeetingListHelper";
	public ArrayList<MeetingEntity> parseJSONRespondGetMeetings(String input,long serverId) throws Exception
	{
		ArrayList<MeetingEntity> result ;
		JSONObject json= new JSONObject(input);
		String status=json.getString("status");
		if(status.contains("failed"))throw new Exception(json.getString("reason"));
		Log.i(LOG,"status "+status);
		JSONArray jsonarr = json.getJSONArray("meetings");
		Log.i(LOG,"parsed array");
		result= new ArrayList<MeetingEntity>();
		Log.i(LOG," array size "+jsonarr.length());
		for (int i = 0; i < jsonarr.length(); i++) 
		{  
		     JSONObject childJSONObject = jsonarr.getJSONObject(i);
		     result.add(parseJson2MeetingEntity(childJSONObject,serverId));
		     Log.i(LOG,"parsed "+i);
		}
        
       
        
       
       
        return result;
	}
	private MeetingEntity parseJson2MeetingEntity(JSONObject json,long serverId) throws JSONException
	{
		MeetingEntity entity= new MeetingEntity();
			entity.setServerId(serverId);
			entity.setServerMeetingID(json.getLong("meetingid"));
			entity.setTitle(json.getString("title"));
			entity.setHostName(json.getString("hostname"));
			entity.setStartTime(json.getString("starttime"));
			entity.setEndTime(json.getString("endtime"));
			entity.setNumberOfMembers(json.getInt("members"));
			entity.setPermission(json.getString("permissions"));
			entity.setCode(json.getString("accessCode"));
			
		return entity;
		
	}
}
