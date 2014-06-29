package asynctasks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import serverCommunicator.CommunicationHelper;
import serverCommunicator.RegistrationHelper;

import com.TrololoCompany.meetingdataexchange.SignUpActivity;
import com.TrololoCompany.meetingdataexchangedataBase.DataBaseHelper;
import com.TrololoCompany.meetingdataexchangedataBase.ServerEntity;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class HttpPostSignUp extends AsyncTask<String, Void, Void>{

	private static final String LOG="HTTPPOSTSIGNUP";
	private String reason=null;
	private String status;
	private SignUpActivity activity;
	
	public HttpPostSignUp(SignUpActivity activity)
	{
		this.activity=activity;
	}
	@Override
	protected Void doInBackground(String... arg0) 
	{
		//arg0[0]-name
		//arg0[1]-address
		//arg0[2]-login
		//arg0[3]-nick
		//arg0[4]-email
		//arg0[5]-password
		try{
			CommunicationHelper communication= new CommunicationHelper();
			RegistrationHelper regHelper= new RegistrationHelper();
			JSONObject json=regHelper.makeJSON2Reg(arg0[2],arg0[3],arg0[4],arg0[5]);
			String respond=communication.getPostHttpRequest("http://"+arg0[1]+"/api/account/register", json);
			String result[]=regHelper.parseJSONRespondRegistration(respond);
			status=result[0];
			reason=result[1];
			if(status!=null&&status.contains("ok"))
			{
				ServerEntity entity=communication.
						makeServerEntity(arg0[1], arg0[0], arg0[2],
						arg0[3], arg0[4], arg0[5],null);
				new DataBaseHelper(activity.getApplicationContext()).
				insertServerEntity(entity);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
				
		return null;
	}	
	@Override
	protected void onPostExecute(Void result) 
	{
		
		if(status!=null&&status.contains("ok"))
		{
			activity.registrationSuccess();
		}
		else
		{
			if(reason==null)
				reason="connection failed";
			activity.registrationFailed(reason);
		}
	}

	

}
