package dataBase;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper 
{
	private static final String log="BaseHelper";
	
	private static final String DEBUG_TAG = "SqLiteTodoManager";
	 
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "database.db";
    
    private static final String SERVER_TABLE_NAME="SERVER";
    private static final String MEETING_TABLE_NAME="MEETING";
    private static final String FILE_TABLE_NAME="FILE";
    private static final String COMMENT_TABLE_NAME="COMMENT";
    
    private static final String SERVER_ID="Id";
    private static final String SERVER_ADDRESS="address";
    private static final String SERVER_NAME="Servername";
    private static final String SERVER_LOGIN="login";
    private static final String SERVER_NICK="yourName";
    private static final String SERVER_MAIL="email";
    private static final String SERVER_PASSWORD="passwd";
    private static final String SERVER_SID="sid";
 
    
    private static final String MEETING_ID="Id";
    private static final String MEETING_SERVERID="serverID";
    private static final String MEETING_SERVER_MEETING_ID="serverMeetingId";
    private static final String MEETING_TITLE="title";
    private static final String MEETING_TOPIC="topic";
    private static final String MEETING_HOST_NAME="hostName";
    private static final String MEETING_START_TIME="startTime";
    private static final String MEETING_END_TIME="endTime";
    private static final String MEETING_CODE="code";
    private static final String MEETING_NUMBER_OF_MEMBERS="numberOfMembers";
    private static final String MEETING_PERMISSION="yourPermissions";
    
    private static final String FILE_ID="Id";
    private static final String FILE_METTING_ID="mettingId";
    private static final String FILE_SERVER_FILE_ID="serverFileId";
    private static final String FILE_FILE_NAME="fileName";
    private static final String FILE_AUTHOR_NAME="authorName";
    private static final String FILE_ADD_TIME="addTime";
    private static final String FILE_HASHMD5="hashMD5";
    
    private static final String COMMENT_ID="Id";
    private static final String COMMENT_FILE_ID="fileID";
    private static final String COMMENT_SERVER_COMMENT_ID="serverCommentId";
    private static final String COMMENT_AUTHOR_NAME="authorName";
    private static final String COMMENT_ADD_TIME="addTime";
    private static final String COMMENT_CONTENT="content";

    private SQLiteDatabase db;
    
    public DataBaseHelper(Context context) {
        super(context,"database.db",null,1);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db)
    {
    	String createServerTable=
    	"CREATE TABLE "+SERVER_TABLE_NAME+" ( "+
    	SERVER_ID+"INT AUTO_INCREMENT PRIMARY KEY , "+
    	SERVER_ADDRESS+" VARCHAR(255) , "+
    	SERVER_NAME+" VARCHAR(255) , "+
    	SERVER_LOGIN+" VARCHAR(255) , "+
    	SERVER_NICK+" VARCHAR(255) , "+
    	SERVER_MAIL+" VARCHAR(255) , "+
    	SERVER_PASSWORD+" VARCHAR(255) ,"+
    	SERVER_SID+" VARCHAR(255) "+
    	" );";
    	
    	String createMeetingTable=
    	"CREATE TABLE "+MEETING_TABLE_NAME+" ( "+
    	MEETING_ID+" INT AUTO_INCREMENT PRIMARY KEY, "+
    	MEETING_SERVERID+" INT ,"+
    	MEETING_SERVER_MEETING_ID+" INT ,"+
    	MEETING_TITLE+" VARCHAR(255) , "+
    	MEETING_TOPIC+" VARCHAR(255) , "+
    	MEETING_HOST_NAME+" VARCHAR(255) , "+
    	MEETING_START_TIME+" TIMESTAMP ,"+
    	MEETING_END_TIME+" TIMESTAMP ,"+
    	MEETING_CODE+" VARCHAR(255) , "+
    	MEETING_NUMBER_OF_MEMBERS+" INT ,"+
    	MEETING_PERMISSION+" INT ,"+
    	"FOREIGN KEY ("+MEETING_SERVERID+") REFERENCES "+
    	SERVER_TABLE_NAME+"("+SERVER_ID+")"+
    	" );";
    	
    	String createFileTable=
    	"CREATE TABLE "+FILE_TABLE_NAME+" ( "+
    	FILE_ID+" INT AUTO_INCREMENT PRIMARY KEY, "+
    	FILE_METTING_ID+" INT ,"+
    	FILE_SERVER_FILE_ID+" INT,"+
    	FILE_FILE_NAME+" VARCHAR(255) , "+
    	FILE_AUTHOR_NAME+" VARCHAR(255) , "+
    	FILE_ADD_TIME+" TIMESTAMP ,"+
    	FILE_HASHMD5+" VARCHAR(255) , "+
    	"FOREIGN KEY ("+FILE_METTING_ID+") REFERENCES "+
    	MEETING_TABLE_NAME+"("+MEETING_ID+")"+
    	" );";
    	
    	String createCommentTable=
    	"CREATE TABLE "+COMMENT_TABLE_NAME+" ( "+
    	COMMENT_ID+" INT AUTO_INCREMENT PRIMARY KEY, "+
    	COMMENT_FILE_ID+" INT ,"+
    	COMMENT_SERVER_COMMENT_ID+" INT ,"+
    	COMMENT_AUTHOR_NAME+" VARCHAR(255) , "+
    	COMMENT_ADD_TIME+" TIMESTAMP ,"+
    	COMMENT_CONTENT+" VARCHAR(255) , "+
    	"FOREIGN KEY ("+COMMENT_FILE_ID+") REFERENCES "+
    	FILE_TABLE_NAME+"("+FILE_ID+")"+
    	" );";
    	
    	
    			
    	Log.i(log, createServerTable);
    	try
    	{
    		Log.i(log, createServerTable);
    		db.execSQL(createServerTable);
    		Log.i(log, "done");
    		
    		Log.i(log, createMeetingTable);
    		db.execSQL(createMeetingTable);
    		Log.i(log, "done");
    		
    		Log.i(log, createFileTable);
    		db.execSQL(createFileTable);
    		Log.i(log, "done");
    		
    		Log.i(log, createCommentTable);
    		db.execSQL(createCommentTable);
    		Log.i(log, "done");
    	}
    	catch(SQLException e)
    	{
    		
    		e.printStackTrace();
    	}
        Log.i(log, "Database creating...");
        Log.i(log, "Table " + SERVER_TABLE_NAME + " ver." + DB_VERSION + " created");
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
    public ArrayList<ServerEntity> getServerEntities()
    {
    		SQLiteDatabase db =this.getReadableDatabase();
    		Cursor cursor= db.query(SERVER_TABLE_NAME,null,null,
    							null,null,null,null);
    	//db.close();
    	return makeServerEntityList(cursor);
    	
    }
    private ArrayList<ServerEntity> makeServerEntityList(Cursor cursor)
    {
    	ArrayList<ServerEntity> result=null;
    	if(cursor != null)
    	{
    		Log.i(log, "cursor size "+cursor.getCount());
    		result=new ArrayList<ServerEntity>();
    		while(cursor.moveToNext())
    		{
    			ServerEntity entity=new ServerEntity();
    			entity.setId(cursor.getLong(0));
    			Log.i(log, "loaded");
    			entity.setAddress(cursor.getString(1));
    			Log.i(log, "loaded");
    			entity.setServerName(cursor.getString(2));
    			Log.i(log, "loaded");
    			entity.setLogin(cursor.getString(3));
    			Log.i(log, "loaded");
    			entity.setYourName(cursor.getString(4));
    			Log.i(log, "loaded");
    			entity.setEmail(cursor.getString(5));
    			Log.i(log, "loaded");
    			entity.setPasswd(cursor.getString(6));
    			Log.i(log, "loaded");
    			entity.setSid(cursor.getString(7));
    			Log.i(log, "loaded");
    			result.add(entity);
    		}
    	}
    	return result;
	     
    }
    public void insertServerEntity(ServerEntity entity)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(SERVER_ADDRESS,entity.getAddress());
		values.put(SERVER_NAME,entity.getServerName());
		values.put(SERVER_LOGIN,entity.getLogin());
		values.put(SERVER_NICK,entity.getYourName());
		values.put(SERVER_MAIL,entity.getEmail());
		values.put(SERVER_PASSWORD,entity.getPasswd());
		values.put(SERVER_SID,entity.getSid());
		
		db.insert(SERVER_TABLE_NAME, null, values);
		Log.i(log, "ServerEntity added");
		//db.close();
	}
}
