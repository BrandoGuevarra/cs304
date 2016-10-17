import java.sql.*;
     
public class Connect {

	public static void main(String args[]) {
		  
		String url = "jdbc:oracle:thin:@localhost:1522:ug";
		String user = "ora_k5o0b";
		String password = "a54223152";
		Connection con;
		
		Statement stmt;
		String query = "select USERNAME from PLAYER where PLAYERLEVEL > 5";
	
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
	
		} catch(java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}	
	
		try {
			con = DriverManager.getConnection(url, user, password);
			stmt = con.createStatement();							

			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				String s = rs.getString("USERNAME");
				System.out.println(s);
			}
			
			stmt.close();
			con.close();
	
		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
	}
}