package lists;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.TrololoCompany.meetingdataexchange.R;
import com.TrololoCompany.meetingdataexchange.R.id;
import com.TrololoCompany.meetingdataexchange.R.layout;
import com.TrololoCompany.meetingdataexchange.R.menu;
import com.TrololoCompany.meetingdataexchangeAdapters.MeetingAdapter;
import com.TrololoCompany.meetingdataexchangeAdapters.ServerAdapter;
import com.TrololoCompany.meetingdataexchangedataBase.DataBaseHelper;
import com.TrololoCompany.meetingdataexchangedataBase.MeetingEntity;
import com.TrololoCompany.meetingdataexchangedataBase.ServerEntity;



public class MeetingList extends Activity 
{
	private ArrayList<MeetingEntity> arrayOfMeetings;
	private MeetingAdapter adapter;
	private ServerEntity server;
	private static final String LOG="MeetingList";

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		Intent intent=getIntent();
		this.server=(ServerEntity) intent.getSerializableExtra("com.TrololoCompany.meetingdataexchange.server");
		setContentView(R.layout.activity_meeting_list);
		this.arrayOfMeetings = new DataBaseHelper(getApplicationContext())
							.getAllMeetingServerId(server.getId());
		
		// Create the adapter to convert the array to views
		this.adapter = new MeetingAdapter(this, arrayOfMeetings);
		// Attach the adapter to a ListView
		ListView listView = (ListView) findViewById(R.id.meetingList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new MyOnItemClickListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.server_manage, menu);
		return true;
	}
	
	class MyOnItemClickListener implements OnItemClickListener 
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long id) {
			Intent intent = new Intent();
		    MeetingEntity entity=arrayOfMeetings.get(position);
			Bundle bundle = new Bundle();
			bundle.putSerializable("com.TrololoCompany.meetingdataexchange.meeting", entity);
			bundle.putSerializable("com.TrololoCompany.meetingdataexchange.server", server);
			Log.i("meeting list",entity.getID()+"");
			Log.i("meeting list",entity.getTopic());
			Log.i("meeting list",entity.getTitle());
			intent.putExtras(bundle);
			setResult(RESULT_OK,intent);
			finish();

			
		}
		
	}
	
	

}
