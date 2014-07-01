package meeting_options;


import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.TrololoCompany.meetingdataexchange.MeetingDetails;
import com.TrololoCompany.meetingdataexchange.R;
import com.TrololoCompany.meetingdataexchangedataBase.MeetingEntity;
import com.TrololoCompany.meetingdataexchangedataBase.ServerEntity;

import fileMaintenance.FileMaintenance;
public class MeetingAddItems extends Fragment {
private MeetingDetails activity;
private ServerEntity server;
private MeetingEntity meeting;
static final int REQUEST_IMAGE_CAPTURE = 1;
static final int RESULT_OK = 2;
static final int REQUEST_TAKE_PHOTO = 3;
  @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
              Bundle savedInstanceState) 
  		{
          View android = inflater.inflate(R.layout.meeting_details_add_frag, container, false);
          activity=(MeetingDetails) getActivity();
          server=activity.getServer();
          meeting=activity.getMeeting();
          return android;
  		}

  
		   
		  	
}