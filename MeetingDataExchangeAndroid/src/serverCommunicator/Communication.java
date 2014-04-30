package serverCommunicator;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dataBase.DataBaseHelper;
import dataBase.ServerEntity;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;

public class Communication 
{
	private String input;
	private String address;
	private Context context;
	final static String log="Communication";
	public Communication(String input,Context contex)
	{
		this.input=input;
		this.context=contex;
		divideInput();
	}
	public void join2Meeting()
	{
		String name=getServerName();
		Log.i(log,name);
		ServerEntity ent=new DataBaseHelper(context)
		.getServerEntity(name);
		if(ent==null)
		{
			
		}
		else
		{
			
		}
		
	}
	private String getServerName()
	{
		HttpResponse response;
		String name = null;
		try {
			response = new HttpGetRequest().execute("http://"+address+"/api/general/getname").get();
			String result = EntityUtils.toString(response.getEntity());
	        JSONObject obj= new JSONObject(result);
	         name=(String) obj.get("servername");
	        Log.i("get login",name);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return name;

	}
	private void divideInput()
	{
		address=input.substring(0, input.indexOf(";"));
		Log.i(log,"address "+address);
	}
    private void register2Server()
    {
    	
    }
	
}
	


