package asynctasks;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import serverCommunicator.CommunicationHelper;

import com.TrololoCompany.meetingdataexchange.AddServerActivity;
import com.TrololoCompany.meetingdataexchange.AddServerActivitySuccess;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class HttpGetServerName extends AsyncTask<String, Void, Void>
{
	private AddServerActivity activity;
	private String name;
	private static final String log="CheckIsServerExists";
	public HttpGetServerName(AddServerActivity activity)
	{
		this.activity=activity;
		name=null;
	}
	@Override
	protected Void doInBackground(String... address)
	{
		String response = null;
		
	    try {
	    	CommunicationHelper communication= new CommunicationHelper();
	    	response=communication.
	    			getHttpGetRequest("http://"+address[0]+"/api/general/getname");
			name=communication.parseJSONRespondGetServerName(response);
	    	Log.i(log,"response");
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
		return null;
	}

	@Override
	protected void onPostExecute(Void result) 
	{
		if(name==null)
		{
			activity.connection_failed();
		}
		else
		{
			activity.connection_succes(name);
		}
		
		
	}
	
		

}
