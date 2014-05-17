package serverCommunicator;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class NewMeetingHelper 
{
	private static final String LOG="NewMeetingHelper";
	public JSONObject makeJSON2LogIn
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
	public String[] parseJSONRespondRegistration(String input)
	{
		JSONObject json;
		String result[] = new String[2];
		try {
			json = new JSONObject(input);
			Log.i(LOG,"respond received");
			String status=json.getString("status");
			result[0]=status;
			Log.i(LOG,"status "+status);
			if(status.contains("failed"))
			{
				result[1]=json.getString("reason");
				Log.i(LOG,"status "+result[1]);
			}
			else if(status.contains("ok"))
			{
				result[1]=json.getString("meetingid");
				Log.i(LOG,"status "+result[1]);
			}
					
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return result;
	}
	
	

	
}
