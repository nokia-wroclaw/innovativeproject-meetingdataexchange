package serverCommunicator;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.TrololoCompany.meetingdataexchangedataBase.CommentEntity;
import com.TrololoCompany.meetingdataexchangedataBase.DataBaseHelper;
import com.TrololoCompany.meetingdataexchangedataBase.FileEntity;
import com.TrololoCompany.meetingdataexchangedataBase.MeetingEntity;
import com.TrololoCompany.meetingdataexchangedataBase.ServerEntity;

public class FileHelper 
{
	private Context context;
	public FileHelper (Context context)
	{
		this.context=context;
	}
	private String getStringWithFiles(ServerEntity server,MeetingEntity meeting) throws ClientProtocolException, IOException
	{
		
		CommunicationHelper communicationHelper= new CommunicationHelper();
		String address="http://"+server.getAddress()+"/api/files/list/"+
		meeting.getServerMeetingID()+"/"+server.getLogin()+"/"+server.getSid();
		String result=communicationHelper.getHttpGetRequest(address);
		return result;
		
	}
	public synchronized void getFileWithCommentsList(ServerEntity server,MeetingEntity meeting) throws Exception
	{

		String data=getStringWithFiles(server, meeting);
		Log.i("file helper",data);
		String parseStrings[]=parseJSONRespond(data);
		JSONObject json;
		if(parseStrings[0].equals("ok"))
		{
			json= new JSONObject(data);
			JSONArray files=(JSONArray) json.get("files");
			for(int i=0;i<files.length();i++)
			{
				Log.i("file helper","meeting id"+meeting.getID());
				JSONObject file=(JSONObject) files.get(i);
				
				insertFileWithComments(file,meeting);
				
			}
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
	private void insertFileWithComments(JSONObject json,MeetingEntity meeting) throws JSONException
	{
		CommentsHelper commentHelper = new CommentsHelper();
		ArrayList<CommentEntity> comments_result= new ArrayList<CommentEntity>();
		FileEntity file = new FileEntity();
		file.setServerFileId(json.getLong("fileid"));
		file.setFileName(json.getString("filename"));
		file.setAuthorName(json.getString("author"));
		file.setAddTime(json.getString("addtime"));
		file.setHashMD5(json.getString("hash"));
		file.setMeetingID(meeting.getID());
		new DataBaseHelper(context).insertFileEntity(file);
		file=new DataBaseHelper(context).getFileServerId(file.getServerFileId());
		JSONArray comments=(JSONArray) json.get("comments");
		for(int i=0;i<comments.length();i++)
		{	
			Log.i("fileID",file.getID()+"");
			CommentEntity comment= commentHelper.makeComment(json, file);
			new DataBaseHelper(context).insertCommentEntity(comment);
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


			}
					
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return result;
	}

}
