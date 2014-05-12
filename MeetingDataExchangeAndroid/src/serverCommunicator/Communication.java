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

	String name="michal";//temporary
	String email="michal.blach92@gmail.com";//temporary
	String password="2345fgdsh434";//temporary
	final static String log="Communication";
	public Communication()
	{
	}
	
	private JSONObject makeJSON2Reg(String login,String nick,String email,String password)
	{	
		JSONObject json= new JSONObject();
		try {
			json.accumulate("logn", login);
			json.accumulate("name", name);
			json.accumulate("email", email);
			json.accumulate("password", password);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

/*
    private void register2Server()
    {
    	
    	JSONObject json=makeJSON2Reg();
		try {
			JSONObject jsonRes=new HttpPostRequest
			("http://"+address+"/api/account/register")
			.execute(json).get();
			String result=jsonRes.getString("status");
			Log.i(log,jsonRes.getString("status"));
			if(result.contains("ok"))
			{
				ServerEntity entity= new ServerEntity();//temporary
				entity.setName(name);
				entity.setAddress(address);
				entity.setLogin(jsonRes.getString("login"));
				entity.setNick("");
				entity.setEmail(email);
				entity.setPassword(password);
				new DataBaseHelper(context).insertServerEntity(entity);
				
			}
			else
			{
				
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
    	
    }
	*/
}
	


