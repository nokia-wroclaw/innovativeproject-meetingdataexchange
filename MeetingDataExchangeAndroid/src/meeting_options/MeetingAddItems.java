package meeting_options;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.TrololoCompany.meetingdataexchange.R;
public class MeetingAddItems extends Fragment {
  @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
              Bundle savedInstanceState) 
  		{
          View android = inflater.inflate(R.layout.meeting_details_add_frag, container, false);
        
          return android;
  		}
}