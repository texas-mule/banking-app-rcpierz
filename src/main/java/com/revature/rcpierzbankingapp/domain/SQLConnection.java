package com.revature.rcpierzbankingapp.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLConnection{
	private static SQLConnection sqlconnection;
	String sql;
	public Connection connection;

	Boolean isResultSet;

	private SQLConnection() throws SQLException {
	}
	
	public static SQLConnection getInstance() {
		if (sqlconnection == null) {
			try {
				sqlconnection = new SQLConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sqlconnection;
	}
	
	public Connection establishConnection(String url, String username, String password) throws SQLException {
		return this.connection = DriverManager.getConnection(url, username, password);
	}
	
	public Connection getConnection() throws SQLException {
		return this.connection;
	}
	
	public void closeConnection() throws SQLException {
		this.connection.close();
	}

	

	public ResultSet pullAccountByAccId(String account_id_input) {
		try {
			Statement statement = this.connection.createStatement();

			Boolean isResultSet = statement.execute("SELECT * FROM accounts WHERE account_id = " + account_id_input);

			ResultSet resultSet = statement.getResultSet();

			if (!resultSet.next()) {
				System.out.println("No account found");
				return null;
			}
			return resultSet;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return  null;
	}

}
