package serverCommunicator;

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

import com.TrololoCompany.meetingdataexchange.AddServerActivity;
import com.TrololoCompany.meetingdataexchange.AddServerActivitySuccess;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class CheckIsServerExists extends AsyncTask<String, Void, Void>
{
	private AddServerActivity activity;
	private String name;
	private static final String log="CheckIsServerExists";
	public CheckIsServerExists(AddServerActivity activity)
	{
		this.activity=activity;
		name=null;
	}
	@Override
	protected Void doInBackground(String... address)
	{

	    try {
	    	
		    HttpGet httpGet = new HttpGet("http://"+address[0]+"/api/general/getname");
		    HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
            HttpConnectionParams.setSoTimeout(httpParameters, 3000);
            HttpClient client = new DefaultHttpClient(httpParameters);
		    HttpResponse response = null;
	    	Log.i(log,"response before");
			response = client.execute(httpGet);
			Log.i(log,"response");
			String result = EntityUtils.toString(response.getEntity());
	        JSONObject obj= new JSONObject(result);
	        name=(String) obj.get("servername");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    catch (ConnectTimeoutException e) {
			name=null;
		}
	    catch (Exception e) {
			// TODO Auto-generated catch block
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
