package fileMaintenance;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.TrololoCompany.meetingdataexchangedataBase.FileEntity;
import com.TrololoCompany.meetingdataexchangedataBase.MeetingEntity;
import com.TrololoCompany.meetingdataexchangedataBase.ServerEntity;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileMaintenance 
{
	private static String rootPath;
	public static File root;
	public void makeRootFile(Context contex)
	{
		File storageDir=Environment.getExternalStorageDirectory();
		rootPath=storageDir.getAbsolutePath();
		rootPath=rootPath+"/meeting_data_exchange";
		Log.i("fileMaintenance",rootPath);
		root=new File(rootPath);
		if(!root.exists())
		{
			root.mkdir();
		}

	}
	public File makeServerFile(ServerEntity entity)
	{
		File server=new File(rootPath+"/"+entity.getServerName());
		if(!server.exists())
		{
			Log.i("file_maintenance","server path "+server.getAbsolutePath());
			server.mkdir();
			Log.i("file_maintenance","server file created");
			return server;
		}
		else
		{
			Log.i("file_maintenance","server name collision");
			return null;
		}
	}
	public File makeMeetingFile(ServerEntity server,MeetingEntity meeting)
	{
		File serverFile= new File(rootPath+"/"+server.getServerName());
		if(serverFile.exists())
		{
			File meetingFile= new File
					(rootPath+"/"+server.getServerName()+"/"+meeting.getTitle());
			meetingFile.mkdir();
			Log.i("file_maintenance","meeting file created");
			return meetingFile;
		}
		else
		{
			Log.i("file_maintenance","meeting file failed");
			return null;
		}
		
	}
	public File getServer(ServerEntity entity)
	{
		File serverFile= new File(rootPath+"/"+entity.getServerName());
		if(serverFile.exists())
		{
			return serverFile;
		}
		else
		{
			return null;
		}
	}
	public File getMeeting(ServerEntity server,MeetingEntity meeting)
	{
		File meetingFile= new File
				(rootPath+"/"+server.getServerName()+"/"+meeting.getTitle());
		if(meetingFile.exists())
		{
			return meetingFile;
		}
		else
		{
			return null;
		}
	}
	public File makeFileForPhoto(ServerEntity server,MeetingEntity meeting)
	{
		// Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp;
	    File image = null;
	    File meeting_file= new File(rootPath+"/"+server.getServerName()+"/"+meeting.getTitle());
	    if(!meeting_file.exists())
	    {
	    	Log.i("file_maintenance","shit happens");
	    	return null;
	    }
	    else
	    {
	    	try {
	    			image = File.createTempFile(
				        imageFileName,  /* prefix */
				        ".jpg",         /* suffix */
				        meeting_file      /* directory */
				    );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	   return image;
	
	 
		
		
	}
}
