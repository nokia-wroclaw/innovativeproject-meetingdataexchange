package meeting_options;


import com.TrololoCompany.meetingdataexchange.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
public class MeetingDescription extends Fragment {
   @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
              Bundle savedInstanceState) 
   	 {
          View ios = inflater.inflate(R.layout.meeting_details_desc_frag, container, false);
   
          return ios;
   	 }
}