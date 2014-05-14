package asynctasks;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.TrololoCompany.meetingdataexchange.LogInActivity;
import com.TrololoCompany.meetingdataexchange.SignUpActivity;

import dataBase.DataBaseHelper;
import dataBase.ServerEntity;

import serverCommunicator.CommunicationHelper;
import serverCommunicator.GetPersonalDataHelper;

import android.os.AsyncTask;

public class HttpGetPersonalData extends AsyncTask<String, Void, Void>
{
	private LogInActivity activity;
	private String address;
	private String name;
	private String login;
	private String password;
	private String nick;
	private String email;
	private String sid;
	
	public HttpGetPersonalData(LogInActivity activity)
	{
		this.activity=activity;

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
		GetPersonalDataHelper helper= new GetPersonalDataHelper();
		try {
			String respond=communication.
			getHttpGetRequest("http://"+address+"/api/account/getdata/"
								+login+"/"+sid);
			String [] result=helper.parseJSONRespondGetServerName(respond);
			nick=result[0];
			email=result[1];
			ServerEntity entity=communication.
					makeServerEntity
					(address, name, login, nick, email, password, sid);
			new DataBaseHelper(activity.getApplicationContext()).
			insertServerEntity(entity);
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
		}
		return null;
	}
	@Override
	protected void onPostExecute(Void result) 
	{
		if(email!=null)
		{
			activity.
			displayMessage("Personal data downloaded ,downloading meetings");
		}
		else
		{
			activity.
			displayMessage("Something went wrong ");
		}
	}

}
