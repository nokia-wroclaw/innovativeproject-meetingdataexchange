package meeting_options;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.TrololoCompany.meetingdataexchange.MeetingDetails;
import com.TrololoCompany.meetingdataexchange.R;
import com.TrololoCompany.meetingdataexchangedataBase.MeetingEntity;
import com.TrololoCompany.meetingdataexchangedataBase.ServerEntity;
public  class MeetingDescription extends Fragment 
{
	private TextView host;
	private TextView title;
	private TextView durationTime;
	private TextView members;
	private TextView permission;
	private TextView topic;
	private View description;
	private ServerEntity server;
	private MeetingEntity meeting;
	private MeetingDetails activity;


	
   @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
              Bundle savedInstanceState) 
   	 {
         this.description = inflater.inflate(R.layout.meeting_details_desc_frag, container, false);
          
         getTextViews( description);
         Log.i("host","getTextVIew");
         getInitData();
         Log.i("host","getInitData");
         refreshValues();
         Log.i("host","setStartingValues");
       
          return description;
          
   	 }
   private void getTextViews(View description)
   {
	   title=(TextView) description.findViewById(R.id.meeting_details_title);
	   host = (TextView) description.findViewById(R.id.meeting_details_server_host_name);
       durationTime=(TextView) description.findViewById(R.id.meeting_details_server_during_time);
       members=(TextView) description.findViewById(R.id.meeting_details_server_members);
       permission=(TextView) description.findViewById(R.id.meeting_details_server_permission);
       topic=(TextView) description.findViewById(R.id.meeting_details_server_topic);
   }
   

private void getInitData()
   {
	   activity=(MeetingDetails) getActivity();
       setServer(activity.getServer());
       meeting=activity.getMeeting();
   }
   public void refreshValues()
   {
	   try{

		  title.setText(meeting.getTitle());
		  topic.setText(meeting.getTopic());
		  host.setText(meeting.getHostName());
		  durationTime.setText(meeting.getStartTime()+"-");
		  members.setText(meeting.getNumberOfMembers()+"");
		  permission.setText(meeting.getPermission());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace();
	   }
   }
 
   

   
   
   
   
	public ServerEntity getServer() {
		return server;
	}
	public void setServer(ServerEntity server) {
		this.server = server;
	}
	   public MeetingEntity getMeeting() {
			return meeting;
		}
		public void setMeeting(MeetingEntity meeting) {
			this.meeting = meeting;
		}
    
   
}
