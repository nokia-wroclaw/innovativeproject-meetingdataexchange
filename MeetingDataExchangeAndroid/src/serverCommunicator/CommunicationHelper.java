package serverCommunicator;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.TrololoCompany.meetingdataexchangedataBase.DataBaseHelper;
import com.TrololoCompany.meetingdataexchangedataBase.MeetingEntity;
import com.TrololoCompany.meetingdataexchangedataBase.ServerEntity;


import android.content.Context;
import android.nfc.Tag;
import android.util.Log;

public class CommunicationHelper 
{
	private static final String LOG="CommunicationHelper";
	final static String log="Communication";
	public CommunicationHelper()
	{
	}
	
	public String getPostHttpRequest(String address,JSONObject json)
	{
		Log.i(LOG,"request for respond");
		String result=null;
		try{
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httpost = new HttpPost(address);
			StringEntity se = new StringEntity(json.toString());
			httpost.setEntity(se);
			httpost.setHeader("Accept", "application/json");
			httpost.setHeader("Content-type", "application/json");
		    ResponseHandler responseHandler = new BasicResponseHandler();
		    result=httpclient.execute(httpost, responseHandler);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	public String getHttpGetRequest(String address) throws ClientProtocolException, IOException
	{
		
		HttpGet httpGet = new HttpGet(address);
	    HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
        HttpConnectionParams.setSoTimeout(httpParameters, 3000);
        HttpClient client = new DefaultHttpClient(httpParameters);
    	Log.i(log,"response before");
    	HttpResponse response  = client.execute(httpGet);
		String result =null;
		result =EntityUtils.toString(response.getEntity());
		return result;
	}
	public String parseJSONRespondGetServerName(String input) throws JSONException,NullPointerException
	{
		String name=null;
        JSONObject obj= new JSONObject(input);
        name=(String) obj.get("servername");
        if(name==null)throw new NullPointerException();
        return name;
	}
	public ServerEntity makeServerEntity
	(String address,String serverName,String login,
	String nick,String email,String password,String sid)
	{
		ServerEntity entity= new ServerEntity();
		entity.setAddress(address);
		entity.setServerName(serverName);
		entity.setLogin(login);
		entity.setYourName(nick);
		entity.setEmail(email);
		entity.setPasswd(password);
		entity.setSid(sid);
		return entity;

	}
	


}
	


