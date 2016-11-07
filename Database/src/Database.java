import java.sql.*;
     
public class Database {
	
		private static Connection con;
		
	public Database() {
		con = connect();
	}
	
	private static Connection connect() {
		String url = "jdbc:oracle:thin:@localhost:1522:ug";
		String user = "ora_k5o0b";
		String password = "a54223152";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch(java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}	
		
		try {
			con = DriverManager.getConnection(url, user, password);
			return con;
		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}	
		
		return null;
	}
	
	public String getPrimaryKeyName(String tableName) {
		String primaryKey = "";
		
		try {
			DatabaseMetaData meta = con.getMetaData();
			ResultSet rs =  meta.getPrimaryKeys(null, null, tableName);
	
			while(rs.next()) {
				primaryKey = rs.getString("COLUMN_NAME");
			}
		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}	
		
		return primaryKey;
	}
	
	public String[][] select(String[] entry, String tableName, String custom) throws SQLException {
		String[][] data = new String[64][entry.length];
		Statement stmt;
		String queryEntry = "";
		
		for (int i = 0; i < entry.length; i++) {
			entry[i] = entry[i].trim().toUpperCase();
			queryEntry += entry[i] + ", ";
		}
		queryEntry = queryEntry.substring(0, queryEntry.length() - 2);
		String query = "select " + queryEntry + " from " + tableName;	
		if (custom != null) {
			query += " where " + custom;
		}
		query += " ORDER BY " + entry[0] + " ASC";
		System.out.println(query);
		stmt = con.createStatement();							
	
		ResultSet rs = stmt.executeQuery(query);
		for (int i = 0; rs.next(); i++) {
			for (int j = 0; j < entry.length; j++) {
				data[i][j] = rs.getString(entry[j]);
			}
		}
			
		stmt.close();		
		return data;
	}
	
	public boolean update(String tableName, String columnName, Object updateValue, String primaryKey, String primaryKeyValue) throws SQLException {
		boolean isUpdated = false;
		Statement stmt;
		String update = "UPDATE " + tableName + " SET " + columnName + "='" + updateValue + "' " + "WHERE "
		+ primaryKey + "='" + primaryKeyValue + "'" ;
		System.out.println(update);
		
		stmt = con.createStatement();		

		if (stmt.executeUpdate(update) > 0) {
			isUpdated = true;
		}
		stmt.close();
		return isUpdated;
	}
}