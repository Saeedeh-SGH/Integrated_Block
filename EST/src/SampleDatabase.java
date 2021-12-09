package Database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SampleDatabase extends IDatabase<SampleT> {

	public SampleDatabase(String filename, String tableName) {
		super(filename, tableName);
	}

	@Override
	public String objToString(SampleT obj) {
		return obj.num+", '"+obj.str+"'";
	}

	@Override
	public String colToString() {
		return "num, name";
	}

	@Override
	public SampleT resultsetToObject(ResultSet res) throws SQLException {
		int num=res.getInt("num");
		String str=res.getString("name");
		return new SampleT(num, str);
	}

}
