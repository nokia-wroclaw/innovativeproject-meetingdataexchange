package serverCommunicator;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.TrololoCompany.meetingdataexchangedataBase.MeetingEntity;
import com.TrololoCompany.meetingdataexchangedataBase.ServerEntity;

public class FileHelper 
{
	private String getStringWithFiles(ServerEntity server,MeetingEntity meeting) throws ClientProtocolException, IOException
	{
		CommunicationHelper communicationHelper= new CommunicationHelper();
		String address="http://"+server.getAddress()+"/api/files/list/"+
		meeting.getServerMeetingID()+"/"+server.getLogin()+"/"+server.getSid();
		String result=communicationHelper.getHttpGetRequest(address);
		return result;
		
	}
	public void getFileWithCommentsList(ServerEntity server,MeetingEntity meeting) throws Exception
	{

		String data=getStringWithFiles(server, meeting);
		String parseStrings[]=parseJSONRespond(data);
		if(parseStrings[0].equals("ok"))
		{
			JSONArray files=new JSONArray(parseStrings[1]);
			Log.i("files",files.length()+"");
		}
		else if(parseStrings[0].equals("failed"))
		{
			throw new Exception(parseStrings[1]);
		}
		else
		{
			throw new Exception("unknown status");
		}
		

	}
	public String[] parseJSONRespond(String input)
	{
		JSONObject json;
		String result[] = null ;
		try {
			json = new JSONObject(input);
			String status=json.getString("status");
			
			if(status.contains("failed"))
			{
				result=new String[2];
				result[0]=status;
				result[1]=json.getString("reason");
			
			}
			else if(status.contains("ok"))
			{
				result=new String[2];
				result[0]=status;
				result[1]=json.getString("files");

			}
					
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return result;
	}

}
