package asynctasks;

import java.util.ArrayList;

import meeting_options.MeetingProgress;
import meeting_options.RefreshMeetingProgressListView;
import android.os.AsyncTask;
import android.util.Log;

import com.TrololoCompany.meetingdataexchangeServices.ServiceHandlers;
import com.TrololoCompany.meetingdataexchangedataBase.DataBaseHelper;
import com.TrololoCompany.meetingdataexchangedataBase.FileEntity;
import com.TrololoCompany.meetingdataexchangedataBase.MeetingEntity;

public class RefreshMeetingProgress extends AsyncTask<Void , Void, Void> 
{
	private MeetingProgress frag;
	private ServiceHandlers handler;
	private MeetingEntity meeting;
	ArrayList<FileEntity> files;

	public RefreshMeetingProgress(MeetingProgress frag,MeetingEntity meeting)
	{
		this.frag=frag;
		this.meeting=meeting;
		handler=ServiceHandlers.getInstance();
	}
	@Override
	protected Void doInBackground(Void... arg0) 
	{
		while(handler.isRefreshMeetingProgress())
		{
			
			try {
				Thread.sleep(4000);
				Log.i("progress meeting refresh","reload");
				Log.i("progress meeting refresh meeting id ",meeting.getID()+"");
				files=new DataBaseHelper(frag.getActivity().getApplicationContext()).
						getFileAssociatedWithMeeting(meeting.getID());
				this.frag.setFiles(files);
				frag.getActivity().runOnUiThread(new RefreshMeetingProgressListView(frag));
				Log.i("progress meeting refresh files size ",files.size()+"");
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}

}
