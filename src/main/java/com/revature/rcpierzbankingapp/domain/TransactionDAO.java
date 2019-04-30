package com.revature.rcpierzbankingapp.domain;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TransactionDAO {
	private static TransactionDAO transDAO;
	public static Connection connection;
	private static String sql;
	static int maxId = 0;
	
	private TransactionDAO(Connection connection) {
		this.connection = connection;
		
		try {
			Statement statement = connection.createStatement();
			sql = "SELECT COUNT(*) FROM transactions";
			statement.execute(sql);
			ResultSet rs = statement.getResultSet();
			rs.next();
			maxId = Integer.parseInt(rs.getString("count"));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public TransactionDAO getInstance() {
		return transDAO;
	}
	
	public static TransactionDAO establish(Connection connection) {
		if (transDAO == null) transDAO = new TransactionDAO(connection);
		return transDAO;
	}
	
	public static void logTransaction(String sql) {
		try {
			Statement statement = connection.createStatement();
			statement.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void logDeposit(Person currUser, Money depositAmount, Account currAcc) {
		sql = "INSERT INTO transactions VALUES ("+
				++maxId + ", CURRENT_TIMESTAMP, \'" +
				currUser.getUsername() + "\', \'User deposited "+depositAmount+
				" into Account #"+currAcc.getAccountId() + "\')";
		logTransaction(sql);
	}
	
	public static void logWithdraw(Person currUser, Money withdrawAmount, Account currAcc) {
		sql = "INSERT INTO transactions VALUES ("+
				++maxId + ", CURRENT_TIMESTAMP, \'" +
				currUser.getUsername() + "\', \'User withdrew "+withdrawAmount+
				" from Account #"+currAcc.getAccountId() + "\')";
		logTransaction(sql);
	}
	
	public static void logTransfer(Person currUser, Money transferAmount, Account firstAcc, Account secondAcc) {
		sql = "INSERT INTO transactions VALUES ("+
				++maxId + ", CURRENT_TIMESTAMP, \'" +
				currUser.getUsername() + "\', \'User transferred "+transferAmount+
				" from Account #"+firstAcc.getAccountId() + 
				" to Account #"+secondAcc.getAccountId() + "\')";
		logTransaction(sql);
	}

	public static void logAccountEdit(Person currUser, Account account, String target, String finalState) {
		sql = "INSERT INTO transactions VALUES ("
				+ ++maxId + ", CURRENT_TIMESTAMP, \'"
				+currUser.getUsername()+", Changed "
				+target+" of Account #"
				+account.getAccountId()+" to "
				+finalState + "\')";
	}
	
	public static void logNewUser(Customer newCust) {
		sql = "INSERT INTO customers VALUES ("+
				++maxId + ", CURRENT_TIMESTAMP, \'" + 
				newCust.getUsername() + "\', \'New User Created: "+newCust.toString() + "\')";
		logTransaction(sql);
	}
}
