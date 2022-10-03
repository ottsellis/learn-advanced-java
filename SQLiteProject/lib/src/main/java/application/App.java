package application;

import java.sql.DriverManager;
import java.sql.SQLException;

public class App {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		int[] ids = {0,1,2};
		String[] names = {"Sue","Bob","Charley"};
		
		Class.forName("org.sqlite.JDBC");
		
		String dbUrl = "jdbc:sqlite:people.db";
		
		var conn = DriverManager.getConnection(dbUrl);
		
		var stmt = conn.createStatement();
		conn.setAutoCommit(false);
		
		var sql = "CREATE TABLE IF NOT EXISTS user (id INTEGER PRIMARY KEY, name TEXT NOT NULL)";
		stmt.execute(sql);
		
		sql = "INSERT INTO user (id, name) VALUES (?,?)";
		var insertStmt = conn.prepareStatement(sql);
		
		for(int i=0; i<ids.length; i++) {
			insertStmt.setInt(1, ids[i]);
			insertStmt.setString(2, names[i]);
			
			insertStmt.executeUpdate();
		}
		
		conn.commit();
		
		insertStmt.close();
		
		sql = "SELECT id, name FROM user";
		var rs = stmt.executeQuery(sql);
		
		while(rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			
			System.out.println(id + ": " + name);
		}
		
		sql = "DROP TABLE user";
		stmt.execute(sql);
		
		stmt.close();
		
		conn.close();
	}

}
