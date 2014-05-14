package serverCommunicator;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class LogInHelper 
{
	private static final String LOG="LogInHelper";
	public JSONObject makeJSON2LogIn(String login,String password)
	{	
		JSONObject json= new JSONObject();
		try {
			json.accumulate("login", login);
			json.accumulate("password", password);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	public String[] parseJSONRespondLogIn(String input)
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
				Log.i(LOG,""+result[1]);
			}
			else if (status.contains("ok"))
			{
				result[1]=json.getString("sid");
				Log.i(LOG,""+result[1]);
			}
					
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return result;
	}

}
