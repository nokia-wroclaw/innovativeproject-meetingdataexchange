package serverCommunicator;



import org.json.JSONException;
import org.json.JSONObject;

import com.TrololoCompany.meetingdataexchangedataBase.ServerEntity;


import android.util.Log;

public class RegistrationHelper
{
	private static final String LOG ="RegistrationHelper";
	public JSONObject makeJSON2Reg(String login,String nick,String email,String password)
	{	
		JSONObject json= new JSONObject();
		try {
			json.accumulate("login", login);
			json.accumulate("name", nick);
			json.accumulate("email", email);
			json.accumulate("password", password);
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
					
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return result;
	}
	

}
