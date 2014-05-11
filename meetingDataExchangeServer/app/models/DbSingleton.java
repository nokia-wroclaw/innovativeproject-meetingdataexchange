package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class DbSingleton {
	private final static DbSingleton instance = new DbSingleton();
	
	private final String url = "jdbc:sqlite:data/mde.db";
	//private final String userName = "admin";
	//private final String password = "admin";
	
	private static Connection conn;
	private static DSLContext dsl;
	
	public static DbSingleton getInstance(){
		return instance;
	}
	
	private DbSingleton(){
		
	}
	
	public Connection getConnection(){
		checkConnect();
		return conn;
	}
	
	public DSLContext getDsl(){
		checkConnect();
		return dsl;
	}
	
    private void checkConnect(){
        try {
            if(conn == null || conn.isClosed())
                connect();
        } catch (SQLException ex) {
        	System.out.println(ex.getLocalizedMessage());
        }
    }
	
    private void connect(){
        try {
        	Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            dsl = DSL.using(conn, SQLDialect.SQLITE);
        } catch (ClassNotFoundException ex) {
        	
        } catch (SQLException ex) {
        	
        }
    }
}
