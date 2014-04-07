package meeting_options;


import com.TrololoCompany.meetingdataexchange.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class MeetingProgress extends Fragment
{
	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) 
	 {
		 View windows = inflater.inflate(com.TrololoCompany.meetingdataexchange.R.layout.meeting_details_progress_frag, container, false);
        
         return windows;
	 }


}
