package com.revature;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
	private String url;
	private String user;
	private String password;
	
	private static Connection conn;
	
	public static Connection getConnection(String url, String user, String password) throws SQLException {
		//Use to get instance by calling constructor
		if (conn == null) {
			//no connection, need to create
			conn = DriverManager.getConnection(url, user, password);
		}
		return conn;
	}
	
	//Put url, user, and password in resources folder
	
}
