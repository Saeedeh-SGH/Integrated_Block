package Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Database.JSQLite;

public abstract class IDatabase<T> {
	JSQLite database;
	String tableName=null;
	
	public IDatabase(String filename, String tableName){
		database=new JSQLite(filename);
		this.tableName=tableName;
	}
	
	public void close(){
		database.close();
	}
	
	public void setTable(String tableName){
		this.tableName=tableName;
	}
	
	public void add(T data){//string of data should not contain '
		database.execute("INSERT INTO "+tableName+" ("+colToString()+") VALUES("+objToString(data)+");");
	}
	
	public void addAll(List<T> dataAll){
		if(dataAll.size()==0){
			System.err.println("database addAll: data list is empty");
			return;
		}
		
		for(int i=0;i<dataAll.size();i=i+100){
			this.addAll(dataAll, i, 100);//every 100 data in one statement to increase the speed
			//there seems to a limit on the size of a statement, so we cannot put all at once
		}
	}
	private void addAll(List<T> dataAll, int startIndex, int length){//put some data to dataset
		if(dataAll.size()==0){
			return;
		}
		
		if(startIndex+length>dataAll.size()){
			length=dataAll.size()-startIndex;
		}
		StringBuilder strb=new StringBuilder();
		
		strb.append("INSERT INTO "+tableName+" ("+colToString()+") VALUES ");
		for(int i=startIndex;i<startIndex+length;i++){
			strb.append("("+objToString(dataAll.get(i))+"),");
		}
		
		strb.replace(strb.length()-1, strb.length(), ";");
		database.execute(strb.toString());
	}
	
	public List<T> get(){	
		return this.get("SELECT * FROM "+tableName+";");	
	}
	public List<T> get(int size){//get first n	
		return this.get("SELECT * FROM "+tableName+" LIMIT "+size+";");
	}
	public List<T> get(int size, int startIndex){//get n from startIndex, startIndex starts from 0
		return this.get("SELECT * FROM "+tableName+" WHERE ROWID > "+startIndex+ " AND ROWID <= "+(startIndex+size)+";");
		///return this.get("SELECT * FROM "+tableName+" WHERE ROWID < "+startIndex+";");
	
	}
	
	private List<T> get(String stat){
		ResultSet res=database.executeQuery(stat);
		List<T> set=new ArrayList<T>();
		
		try {
			while (res.next()) {
				set.add(resultsetToObject(res));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("IDatabase.get(String stat) error");
		}

		return set;
	}
	
	public void removeAll(){
		database.execute("DELETE FROM "+tableName);
	}
	//------------------------------------------------------------------
	// Abstract functions
	// sample:
	// class T has int var; String str;
	// colToString: "num, name"
	// objToString: "12, 'ali'"
	//------------------------------------------------------------------
	//for INSERT
	public abstract String objToString(T obj);
	//for SELECT and INSERT
	public abstract String colToString();
	//for SELECT
	public abstract T resultsetToObject(ResultSet res) throws SQLException;
}
