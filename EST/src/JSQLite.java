package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class JSQLite {
	Statement statement=null;
	Connection connection=null;
	
	public JSQLite(String filename){
		
		try {
			// Load the sqlite Database Engine JDBC driver
			// sqlite-jdbc-xxxx.jar should be in the class path or made part of the project
			Class.forName("org.sqlite.JDBC");
			// connect to the database. This will load the db files and start the
			// database if it is not already running.
			connection = DriverManager.getConnection("jdbc:sqlite:"+filename);
			statement = connection.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("ClassNotFoundException: org.sqlite.JDBC");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Cannot connect to the "+filename+" dataset.");
		}
		
	}
	
	public void close(){
		try {
			statement.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ResultSet executeQuery(String statementStr){ //the statement must return some ResultSet (select)
		ResultSet rs=null;
		try {
			rs = statement.executeQuery(statementStr);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("cannot executeQuery: "+statementStr);
		}
		return rs;
	}
	
	public void execute(String statementStr){ //the statement must not return ResultSet
		try {
			statement.execute(statementStr);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("cannot execute: "+statementStr);
		}
	}
	
}
