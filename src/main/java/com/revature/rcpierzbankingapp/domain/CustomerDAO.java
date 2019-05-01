package com.revature.rcpierzbankingapp.domain;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CustomerDAO {
	private static CustomerDAO custDAO;
	public Connection connection;
	private String sql;
	static int maxID=0;
	
	private CustomerDAO(Connection connection) {
		this.connection = connection;
	}
	
	public static CustomerDAO getInstance() {
		return custDAO;
	}
	
	public static CustomerDAO establish(Connection connection) {
		if (custDAO == null) custDAO = new CustomerDAO(connection);
		return custDAO;
	}
	
	public Customer fetchCustomerByID(String input_id) {
		sql = "SELECT * FROM customers WHERE \'id\' = \'" + input_id + "\'";
		
		try {
			Statement statement = connection.createStatement();
			statement.execute(sql);

			ResultSet rs = statement.getResultSet();
			if (!(rs.next())) {
				System.out.println("Username could not be found");
			} else {
				return new Customer(
						rs.getString("cust_id"),
						rs.getString("fName"),
						rs.getString("lName"),
						rs.getString("gender"),
						rs.getString("dob"),
						rs.getString("emp_id"),
						rs.getString("username"),
						rs.getString("password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Customer fetchCustomerByUsername(String inputUsername) {
		sql = "SELECT * FROM customers WHERE username = \'" + inputUsername + "\'";
		try {
			Statement statement = connection.createStatement();
			Boolean isResultSet = statement.execute(sql);

			ResultSet rs = statement.getResultSet();

			if (rs.next()){
				return new Customer(
						rs.getString("cust_id"),
						rs.getString("fName"),
						rs.getString("lName"),
						rs.getString("gender"),
						rs.getString("dob"),
						rs.getString("emp_id"),
						rs.getString("username"),
						rs.getString("password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void addCustomer(Customer cust) {
		sql = "INSERT INTO customers VALUES ("+cust.id+", \'"+
				cust.fName+"\', \'"+
				cust.lName+"\', \'"+
				cust.gender+"\', \'"+
				cust.dob+"\', "+
				cust.emp_id+", \'"+
				cust.username+"\', \'"+
				cust.password+"\')";
		try {
			Statement statement = connection.createStatement();
			statement.execute(sql);
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
	public ArrayList<Customer> getAll(){
		sql = "SELECT * FROM customers";
		ArrayList<Customer> customers = new ArrayList<Customer>();
		
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			
			while (rs.next()) {
				customers.add(new Customer(
						rs.getString("cust_id"),
						rs.getString("fName"),
						rs.getString("lName"),
						rs.getString("gender"),
						rs.getString("dob"),
						rs.getString("emp_id"),
						rs.getString("username"),
						rs.getString("password")));
				if (Integer.parseInt(rs.getString("cust_id")) > maxID) maxID = Integer.parseInt(rs.getString("cust_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customers;	
	}
}
