package serverCommunicator;

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

import android.os.AsyncTask;

public class HttpPostRequest extends AsyncTask<JSONObject, Void, JSONObject>
{
	private String address;
	public HttpPostRequest(String address)
	{
		this.address=address;
	}
	

	@Override
	protected JSONObject doInBackground(JSONObject ... arg) 
	{
		
	    DefaultHttpClient httpclient = new DefaultHttpClient();
	    HttpPost httpost = new HttpPost(address);

	    StringEntity se = null;
		try {
			se = new StringEntity(arg[0].toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    httpost.setEntity(se);

	    httpost.setHeader("Accept", "application/json");
	    httpost.setHeader("Content-type", "application/json");

	    ResponseHandler responseHandler = new BasicResponseHandler();
	    JSONObject result = null;
	     try {
			String content=httpclient.execute(httpost, responseHandler);
			 result=new JSONObject(content);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     return result;
	    
	}

}
