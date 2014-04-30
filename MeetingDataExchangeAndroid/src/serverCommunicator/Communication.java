package serverCommunicator;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Map;
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
			register2Server();
			
		}
		else
		{
			
		}
		
	}
	private JSONObject makeJSON2Reg()
	{
		
		String name="michal";
		String email="michal.blach92@gmail.com";
		String password="2345fgdsh434";
		JSONObject json= new JSONObject();
		try {
			json.accumulate("name", name);
			json.accumulate("email", email);
			json.accumulate("password", password);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
		
		
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
    	
    	JSONObject json=makeJSON2Reg();
		try {
			
			JSONObject jsonRes=new HttpPostRequest
			("http://192.168.1.117:9000/api/account/register")
			.execute(json).get();
			Log.i(log,jsonRes.getString("status"));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
	
}
	


