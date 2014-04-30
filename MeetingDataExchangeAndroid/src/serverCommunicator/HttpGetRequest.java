package serverCommunicator;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class HttpGetRequest 
extends AsyncTask<String, Void, HttpResponse> 
{
	private HttpResponse response;
	private String result;
	private final String log="HttpsGetRequest";
	@Override
	protected HttpResponse doInBackground(String ...arg0) 
	{
		HttpClient client = new DefaultHttpClient();
	    HttpGet httpGet = new HttpGet(arg0[0]);
	    HttpResponse response = null;
	    try {
	    	Log.i(log,"response before");
			response = client.execute(httpGet);
			Log.i(log,"response");
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	

}

