package meeting_options;



import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import asynctasks.RefreshMeetingProgress;

import com.TrololoCompany.meetingdataexchange.MeetingDetails;
import com.TrololoCompany.meetingdataexchange.R;
import com.TrololoCompany.meetingdataexchangeAdapters.FileAdapter;
import com.TrololoCompany.meetingdataexchangeServices.MeetingDetailsRefresh;
import com.TrololoCompany.meetingdataexchangeServices.ServiceHandlers;
import com.TrololoCompany.meetingdataexchangedataBase.DataBaseHelper;
import com.TrololoCompany.meetingdataexchangedataBase.FileEntity;
import com.TrololoCompany.meetingdataexchangedataBase.MeetingEntity;
import com.TrololoCompany.meetingdataexchangedataBase.ServerEntity;
public class MeetingProgress extends Fragment 
{
	private ServerEntity server;
	private MeetingEntity meeting;
	private FileAdapter adapter;
    private ArrayList<FileEntity> files;
    private MeetingDetails activity;
    private RefreshMeetingProgress task;
    private ListView listView;
    private ServiceHandlers handler;

	
  @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
              Bundle savedInstanceState) 
  		{
          View android = inflater.inflate(R.layout.meeting_details_progress_frag, container, false);
          getData();
          this.files = new DataBaseHelper(activity.getApplicationContext())
			.getFileAssociatedWithMeeting(activity.getMeeting().getID());
        handler= ServiceHandlers.getInstance();
          this.adapter = new FileAdapter(activity.getApplicationContext(), files);
          Log.i("meeting progress","file size "+this.files.size());
          listView = (ListView) android.findViewById(R.id.fileList);
          adapter.setNotifyOnChange(true);
        
          listView.setAdapter(adapter);
       
          handler=ServiceHandlers.getInstance();
         
          
          return android;
  		} 


@Override
public void onStart() 
  {
	 task=new RefreshMeetingProgress(this,meeting);
	 handler.setRefreshMeetingProgress(true);
	task.execute();
	super.onStart();
  }
@Override
public void onPause() {
	handler.setRefreshMeetingProgress(false);
	task.cancel(true);
	super.onPause();
}
  @Override
public void onStop() 
  {
	handler.setRefreshMeetingProgress(false);
	task.cancel(true);
	super.onStop();
}
  private void getData()
  {
	  activity=(MeetingDetails) getActivity();
	  server=activity.getServer();
	  meeting=activity.getMeeting();
	  Log.i("meeting id ",meeting.getID()+"");
  }


public ArrayList<FileEntity> getFiles() {
	return files;
}


public void setFiles(ArrayList<FileEntity> files) {
	this.files = files;
}
public void refreshList()
{
	Log.i("meeting progess","refreshing adapter");
	 this.adapter = new FileAdapter(activity.getApplicationContext(), files);
	 listView.setAdapter(adapter);
}




}