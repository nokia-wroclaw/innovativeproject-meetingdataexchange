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
import com.TrololoCompany.meetingdataexchangeAdapters.ServerAdapter;
import com.TrololoCompany.meetingdataexchangedataBase.DataBaseHelper;
import com.TrololoCompany.meetingdataexchangedataBase.ServerEntity;


public class ServerList extends Activity 
{
	private ArrayList<ServerEntity> arrayOfServers;
	private ServerAdapter adapter;
	private static final String LOG="ServerList";

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_list);
		this.arrayOfServers = new DataBaseHelper(getApplicationContext())
							.getServerEntities();
		// Create the adapter to convert the array to views
		this.adapter = new ServerAdapter(this, arrayOfServers);
		// Attach the adapter to a ListView
		ListView listView = (ListView) findViewById(R.id.serverList);
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
		    ServerEntity entity=arrayOfServers.get(position);
		    Log.i("server list choosen",entity.getServerName());
			Bundle bundle = new Bundle();
			bundle.putSerializable("com.TrololoCompany.meetingdataexchange.server", entity);
			intent.putExtras(bundle);
			setResult(RESULT_OK,intent);
			finish();

			
		}
		
	}
	
	

}
