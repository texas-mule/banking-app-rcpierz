package com.revature.rcpierzbankingapp.domain;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AccountDAO {
	private static AccountDAO accDAO;
	public Connection connection;
	static String sql;
	static int maxID = 0;	// track highest Account ID

	private AccountDAO(Connection connection) {
		this.connection = connection;
	}

	public static AccountDAO getInstance() {
		return accDAO;
	}

	public static AccountDAO establish(Connection connection) {
		if (accDAO == null)
			accDAO = new AccountDAO(connection);
		return accDAO;
	}
	
	public void insertAccount(Account acc) {
		sql = "INSERT INTO accounts VALUES "+acc.toSqlString();
		try {
			Statement statement = connection.createStatement();
			statement.execute(sql);
		} catch (SQLException e ) {
			e.printStackTrace();
		}
	}
	
	public void updateAccount(Account acc) {
		sql = "UPDATE accounts SET balance = "+acc.getBalance()
				+ ", status = " + acc.status + " WHERE account_id = " +acc.getAccountId();
		try {
			Statement statement = connection.createStatement();
			statement.execute(sql);			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void addAccountToDatabase(Account acc) {
		sql = "INSERT INTO accounts VALUES ("+
				acc.id+", "+
				acc.owner_id+", "+
				(acc.joint_id == 0 ? "null" : acc.joint_id) +", \'"+
				acc.type+"\', \'"+
				acc.getBalance()+"\', "+
				acc.status+")";
		try {
			Statement statement = connection.createStatement();
			statement.execute(sql);
			
			if (statement.getUpdateCount() == 1) {
				System.out.println("Account successfully added to database");
				// TODO Add LogNewAccount
			} else System.out.println("Unable to add account to database.");
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
	public ArrayList<Account> getAll(){
		sql = "SELECT * FROM accounts";
		ArrayList<Account> accounts = new ArrayList<Account>();
		
		try {
			Statement statement = connection.createStatement();
			Boolean isResultSet = statement.execute(sql);
			if (!isResultSet) {
				System.out.println("No accounts found in database.");
				return null;
			}
			ResultSet rs = statement.getResultSet();
			while (rs.next()) {
				accounts.add(new Account(
						rs.getString("account_id"),
						rs.getString("owner_id"),
						rs.getString("joint_id"),
						rs.getString("account_type"),
						rs.getString("balance"),
						rs.getString("status")));
				if (Integer.parseInt(rs.getString("account_id")) > maxID) maxID = Integer.parseInt(rs.getString("account_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return accounts;
	}
	
	
	public Account fetchAccountByAccId(String accId) {
		sql = "SELECT * FROM accounts WHERE account_id = "+accId;
		Account currAcc;
		
		try {
			Statement statement = connection.createStatement();
			Boolean isResultSet = statement.execute(sql);
			
			if (isResultSet) {
				ResultSet rs = statement.getResultSet();
				rs.next();
				currAcc = new Account(
						rs.getString("account_id"),
						rs.getString("owner_id"),
						rs.getString("joint_id"),
						rs.getString("account_type"),
						rs.getString("balance"),
						rs.getString("status"));
				return currAcc;
			} else System.out.println("Account not found.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<Account> fetchAvailableCustomerAccounts(String custId){
		sql = "SELECT accounts.* FROM accounts INNER JOIN customers ON "
				+ "accounts.owner_id = customers.cust_id OR "
				+ "accounts.joint_id = customers.cust_id "
				+ "WHERE customers.cust_id = "+custId;
		ArrayList<Account> accData = new ArrayList<Account>();
		
		try {
			Statement statement = connection.createStatement();
			
			Boolean isResultSet = statement.execute(sql);
			
			if (isResultSet) {
				ResultSet rs = statement.getResultSet();
				ResultSetMetaData rsmd = rs.getMetaData();
				
				while (rs.next()) {
					accData.add(new Account(
						rs.getString("account_id"),
						rs.getString("owner_id"),
						rs.getString("joint_id"),
						rs.getString("account_type"),
						rs.getString("balance"),
						rs.getString("status")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return accData;
	}
}