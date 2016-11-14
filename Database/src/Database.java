import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
     
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
	
	public int getColumnCount(String tableName) {
		int col = 0;
		try {
			Statement stmt;
			stmt = con.createStatement();							
			ResultSet rs = stmt.executeQuery("SELECT * from " + tableName);
			ResultSetMetaData rsmd = rs.getMetaData();	
			col = rsmd.getColumnCount();

			stmt.close();		
		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}		
		
		return col;
	}
	
	public int getRowCount(String tableName) {
		int row = 0;
		try {
			Statement stmt;
			stmt = con.createStatement();							
			ResultSet rs = stmt.executeQuery("SELECT * from " + tableName);
			while (rs.next()) {
				++row;
			}

			stmt.close();		
		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}		
		
		return row;
	}
	
	
	public String[] getEntries(String tableName) {
		String[] entries = new String[32];
		
		try {
			Statement stmt;
			stmt = con.createStatement();							
			ResultSet rs = stmt.executeQuery("SELECT * from " + tableName);
			ResultSetMetaData rsmd = rs.getMetaData();	
			int columnCount = rsmd.getColumnCount();
			
			for (int i = 1; i <= columnCount; i++ ) {
				  entries[i - 1] = rsmd.getColumnName(i);
			}
			stmt.close();		
		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}		
		
		//remove trailing null values 
	    List<String> list = new ArrayList<String>(Arrays.asList(entries));
	    list.removeAll(Collections.singleton(null));
	    return list.toArray(new String[list.size()]);
	}
	
	public String getValue(String tableName, String primaryKeyName, String primaryKey, String valueName) {
		String value = "";
		
		try {
			Statement stmt;
			stmt = con.createStatement();							
			ResultSet rs = stmt.executeQuery("SELECT * from " + tableName);
			
			while(rs.next()) {
				if (rs.getString(primaryKeyName).equals(primaryKey)) {
					return rs.getString(valueName);
				}
			}
		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}	
		
		return value;
	}
	//gives the exact sql statement as query
	public String[][] exactQuery(String query, String[] entry) {
		String[][] data = new String[128][entry.length];
		System.out.println(query);
			
		try {
			Statement stmt;
			stmt = con.createStatement();	
			ResultSet rs = stmt.executeQuery(query);

			for (int i = 0; rs.next(); i++) {
				for (int j = 0; j < entry.length && entry[j] != null; j++) {
					data[i][j] = rs.getString(entry[j]);
				}
			}
		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}			
		return data;
	}
	
	public boolean deletion(String update) {
		try {
			Statement stmt;
			stmt = con.createStatement();	

			
			return (stmt.executeUpdate(update) != 0);

		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
			return false;
		}
	}
	
	public String[][] select(String[] entry, String tableName, String custom) throws SQLException {
		String[][] data = new String[128][entry.length];
		Statement stmt;
		String queryEntry = "";
		
		for (int i = 0; i < entry.length && entry[i] != null; i++) {
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
			for (int j = 0; j < entry.length && entry[j] != null; j++) {
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