package dataBase;

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
    
    private static final String SERVER_NAME="name ";
    private static final String SERVER_ADDRESS="address ";
    private static final String SERVER_LOGIN="login ";
    private static final String SERVER_NICK="nick ";
    private static final String SERVER_MAIL="mail ";
    private static final String SERVER_PASSWORD="password ";
    
    private static final String MEETING_ID=" id integer PRIMARY KEY auto_increment";
    private static final String MEETING_TITLE="integer auto_increment";
    private static final String MEETING_TOPIC="integer auto_increment";
    private static final String MEETING_START_TIME="start_time TIMESTAMP ";
    private static final String MEETING_END_TIME="end_time TIMESTAMP ";
    private static final String MEETING_OWNER="owner VARCHAR(255) ";
    private static final String MEETING_USER_QUANTITY="user_quantity integer ";
    private static final String MEETING_SERVER_ID="server_id VARCHAR(255)";
    

    private SQLiteDatabase db;
    
    public DataBaseHelper(Context context) {
        super(context,"database.db",null,1);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db)
    {
    	String createBase="CREATE TABLE "+SERVER_TABLE_NAME+"( "+
    	SERVER_NAME+" PRIMARY KEY , "+
    	SERVER_ADDRESS+" VARCHAR(255) , "+
    	SERVER_LOGIN+" VARCHAR(255) , "+
    	SERVER_NICK+" VARCHAR(255) , "+
    	SERVER_MAIL+" VARCHAR(255) , "+
    	SERVER_PASSWORD+" VARCHAR(255) "+
    	" );";
    	Log.i(log, createBase);
    	try{
    	db.execSQL(createBase);
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
    public ServerEntity getServerEntity(String name)
    {
    
    	
    		 SQLiteDatabase db =this.getReadableDatabase();

    			 String cond;

    			 cond=SERVER_NAME+" = '"+name+"'" ;

    				Cursor cursor= db.query(SERVER_TABLE_NAME,null,cond,
    							null,null,null,null);
    				
    	return null;
    }
}
