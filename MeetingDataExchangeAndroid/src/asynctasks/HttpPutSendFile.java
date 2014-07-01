package asynctasks;

import java.io.File;
import java.io.FileInputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.TrololoCompany.meetingdataexchange.MeetingDetails;
import com.TrololoCompany.meetingdataexchangedataBase.MeetingEntity;
import com.TrololoCompany.meetingdataexchangedataBase.ServerEntity;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class HttpPutSendFile extends AsyncTask<Void , Void, Void> 
{
	private File file;
	private ServerEntity server;
	private MeetingEntity meeting;
	public HttpPutSendFile(File file,ServerEntity server,MeetingEntity meeting)
	{
		this.file=file;
		this.server=server;
		this.meeting=meeting;
	}
	@Override
	protected Void doInBackground(Void... params) 
	{
	
		String url = "http://"+server.getAddress()+
					"/api/upload/"+server.getLogin()+"/"+server.getSid()+"/"+
					meeting.getServerMeetingID()+"/"+file.getName();
		Log.i("send_file",url);

		try {
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPut httppost = new HttpPut(url);
		    
		   
		    InputStreamEntity reqEntity = new InputStreamEntity(
		            new FileInputStream(file), -1);
		    
		    reqEntity.setContentType("binary/octet-stream");
		    reqEntity.setChunked(true); // Send in multiple parts if needed
		    httppost.setEntity(reqEntity);
		    HttpResponse response = httpclient.execute(httppost);
		    String result =EntityUtils.toString(response.getEntity());
		    Log.i("send_file",result );
		    //Do something with response...

		} catch (Exception e) {
		    // show error
		}
		return null;
	}

}
