package com.TrololoCompany.meetingdataexchangedataBase;

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
    private static final String FILE_METTING_ID="meetingId";
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
    	MEETING_PERMISSION+" VARCHAR(255) ,"+
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
    	ArrayList<ServerEntity> result=makeServerEntityList(cursor);
    	db.close();
    	return result;
    	
    }
    public long getServerId(String name,String login)
    {
    	long result=-1;
    		SQLiteDatabase db =this.getReadableDatabase();
    		String cond=SERVER_NAME+" = '"+name+"' AND "+SERVER_LOGIN+" = '"+login+"'";
   			Cursor cursor= db.query(SERVER_TABLE_NAME,null,cond,
   						null,null,null,null);
   			
   			if(cursor.moveToFirst())
   				result=cursor.getLong(0);
    		
    	db.close();
    	return result;
    	
    }
    public ServerEntity getServer(long id)
    {
    	long result=-1;
    		SQLiteDatabase db =this.getReadableDatabase();
    		String cond=SERVER_ID+" = '"+id+"'";
    		Cursor cursor= db.query(SERVER_TABLE_NAME,null,cond,
   						null,null,null,null);
    		
    		ServerEntity result_server=makeServerEntityList(cursor).get(0);
    		db.close();
   			return result_server;
    	
    }
    public MeetingEntity getMeeting(long id)
    {
    	
    		SQLiteDatabase db =this.getReadableDatabase();
    		String cond=MEETING_ID+" = '"+id+"'";
    		Cursor cursor= db.query(MEETING_TABLE_NAME,null,cond,
   						null,null,null,null);
    		db.close();
    		MeetingEntity result=makeMeetingEntityList(cursor).get(0);
   			return result;
    	
    }
    public MeetingEntity getMeetingServerId(long id)
    {
    	
    		SQLiteDatabase db =this.getReadableDatabase();
    		String cond=MEETING_SERVERID+" = '"+id+"'";
    		Cursor cursor= db.query(MEETING_TABLE_NAME,null,cond,
   						null,null,null,null);
    		db.close();
   			return makeMeetingEntityList(cursor).get(0);
    	
    }
    public ArrayList<FileEntity> getFileAssociatedWithMeeting(long id)
    {
    	
    	SQLiteDatabase db =this.getReadableDatabase();
			String cond=FILE_METTING_ID+" = '"+id+"'";
			Cursor cursor= db.query(FILE_TABLE_NAME,null,cond,
						null,null,null,null);
			db.close();
		return makeFileEntityList(cursor);
	
    	
    }
    private ArrayList<FileEntity> makeFileEntityList(Cursor cursor)
    {
    	
    	ArrayList<FileEntity> result=null;
    	if(cursor != null)
    	{
    		//Log.i(log, "cursor size "+cursor.getCount());
    		result=new ArrayList<FileEntity>();
    		while(cursor.moveToNext())
    		{
    			FileEntity entity=new FileEntity();
    			entity.setID(cursor.getLong(0));
    			entity.setMeetingID(cursor.getLong(1));
    			entity.setServerFileId(cursor.getLong(2));
    			entity.setFileName(cursor.getString(3));
    			entity.setAuthorName(cursor.getString(4));
    			entity.setAddTime(cursor.getString(5));
    			entity.setHashMD5(cursor.getString(6));
    			result.add(entity);
    		}
    	}
    	return result;
    	
    	
    }
    private ArrayList<MeetingEntity> makeMeetingEntityList(Cursor cursor)
    {
    	ArrayList<MeetingEntity> result=null;
    	if(cursor != null)
    	{
    		//Log.i(log, "cursor size "+cursor.getCount());
    		result=new ArrayList<MeetingEntity>();
    		while(cursor.moveToNext())
    		{
    			MeetingEntity entity=new MeetingEntity();
    			entity.setID(cursor.getLong(0));
    			entity.setServerId(cursor.getLong(1));
    			entity.setServerMeetingID(cursor.getLong(2));
    			entity.setTitle(cursor.getString(3));
    			entity.setTopic(cursor.getString(4));
    			entity.setHostName(cursor.getString(5));
    			entity.setStartTime(cursor.getString(6));
    			entity.setEndTime(cursor.getString(7));
    			entity.setCode(cursor.getString(8));
    			entity.setNumberOfMembers(cursor.getInt(9));
    			entity.setPermission(cursor.getString(10));
    			
    			result.add(entity);
    		}
    	}
    	return result;
    	
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
		db.close();
	}
    public int updateMeeting(MeetingEntity meeting)
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	ContentValues cv = new ContentValues();
    	cv.put(MEETING_TITLE, meeting.getTitle());
    	cv.put(MEETING_TOPIC,meeting.getTopic() );
    	cv.put(MEETING_HOST_NAME, meeting.getHostName());
    	cv.put(MEETING_START_TIME, meeting.getStartTime());
    	cv.put(MEETING_END_TIME, meeting.getEndTime());
    	cv.put(MEETING_CODE, meeting.getCode());
    	cv.put(MEETING_NUMBER_OF_MEMBERS, meeting.getNumberOfMembers());
    	cv.put(MEETING_PERMISSION, meeting.getPermission());


                     /* use COLUMN NAMES here */                     
        String where = MEETING_SERVER_MEETING_ID+"= ? ";
                     /* bind VALUES here */
        String[] whereArgs = { meeting.getServerMeetingID()+"" };
        int result=db.update(MEETING_TABLE_NAME, cv, where, whereArgs);
        db.close();
        return result;
    }
    
    public void insertMeetingEntity(MeetingEntity entity)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(MEETING_SERVERID,entity.getServerId());
		values.put(MEETING_SERVER_MEETING_ID,entity.getServerMeetingID());
		values.put(MEETING_TITLE,entity.getTitle());
		values.put(MEETING_TOPIC,entity.getTopic());
		values.put(MEETING_HOST_NAME,entity.getHostName());
		values.put(MEETING_START_TIME,entity.getStartTime());
		values.put(MEETING_END_TIME,entity.getEndTime());
		values.put(MEETING_CODE,entity.getCode());
		values.put(MEETING_NUMBER_OF_MEMBERS,entity.getNumberOfMembers());
		values.put(MEETING_PERMISSION,entity.getPermission());
		
		db.insert(MEETING_TABLE_NAME, null, values);
		
		Log.i(log, "Meeting added");
		db.close();
	}
    public void insertFileEntity(MeetingEntity entity)
   	{
   		SQLiteDatabase db = this.getWritableDatabase();
   		ContentValues values = new ContentValues();
   		values.put(MEETING_SERVERID,entity.getServerId());
   		values.put(MEETING_SERVER_MEETING_ID,entity.getServerMeetingID());
   		values.put(MEETING_TITLE,entity.getTitle());
   		values.put(MEETING_TOPIC,entity.getTopic());
   		values.put(MEETING_HOST_NAME,entity.getHostName());
   		values.put(MEETING_START_TIME,entity.getStartTime());
   		values.put(MEETING_END_TIME,entity.getEndTime());
   		values.put(MEETING_CODE,entity.getCode());
   		values.put(MEETING_NUMBER_OF_MEMBERS,entity.getNumberOfMembers());
   		values.put(MEETING_PERMISSION,entity.getPermission());
   		
   		db.insert(MEETING_TABLE_NAME, null, values);
   		Log.i(log, "Meeting added");
   		db.close();
   	}
    public void deleteAllFileAssociatedWithMeeting(MeetingEntity entity)
   	{
   		SQLiteDatabase db = this.getWritableDatabase();
   	    String where = FILE_METTING_ID+"= ? ";
   	    /* bind VALUES here */
   	    String[] whereArgs = { entity.getID()+"" };
   		db.delete(FILE_TABLE_NAME, where, whereArgs);
   	
   		Log.i(log, "files deleted");
   		db.close();
   	}
    public void deleteAllCommentsAssociatedWithMeeting(FileEntity entity)
   	{
   		SQLiteDatabase db = this.getWritableDatabase();
   	    String where = COMMENT_FILE_ID+"= ? ";
   	    /* bind VALUES here */
   	    String[] whereArgs = { entity.getID()+"" };
   		db.delete(COMMENT_TABLE_NAME, where, whereArgs);
   	
   		Log.i(log, "comments deleted");
   		db.close();
   	}
    
}
