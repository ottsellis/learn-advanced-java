package com.caveofprogramming.db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
	private static Database db = new Database();
	//private static String URL = "jdbc:mysql://localhost:3306/people?userSSL=false";
	private Connection conn;
	
	public static Database instance() {
		return db;
	}
	
	private Database() {
		 
	}
	
	public Connection getConnection() {
		return conn;
	}
	
	public void connect(Properties props) throws SQLException {
		String server = props.getProperty("server");
		String port = props.getProperty("port");
		String database = props.getProperty("database");
		String user = props.getProperty("user");
		String password = props.getProperty("password");
		
		String URL = String.format("jdbc:mysql://%s:%s/%s?userSSL=false", server, port, database);
		
		conn = DriverManager.getConnection(URL, user, password);
	}
	
	public void close() throws SQLException {
		conn.close();
	}
}
