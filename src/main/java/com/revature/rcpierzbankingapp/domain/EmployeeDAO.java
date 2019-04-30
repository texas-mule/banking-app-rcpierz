package com.revature.rcpierzbankingapp.domain;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EmployeeDAO {
	private static EmployeeDAO empDAO;
	public Connection connection;
	private String sql;
	
	private EmployeeDAO(Connection connection) {
		this.connection = connection;
	}
	
	public EmployeeDAO getInstance() {
		return empDAO;
	}
	
	public static EmployeeDAO establish(Connection connection) {
		if (empDAO == null) empDAO = new EmployeeDAO(connection);
		return empDAO;
	}
	
	public ArrayList<Employee> getAll(){
		sql = "SELECT * FROM employees";
		ArrayList<Employee> employees = new ArrayList<Employee>();
		
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			
			while (rs.next()) {
				employees.add(new Employee(
						rs.getString("emp_id"),
						rs.getString("fName"),
						rs.getString("lName"),
						rs.getString("username"),
						rs.getString("password")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employees;
	}
	
	public Employee getEmpByID(String emp_id_input) {
		sql = "SELECT * FROM employees WHERE emp_id = " + emp_id_input;
		try {
			Statement statement = this.connection.createStatement();

			Boolean isResultSet = statement.execute(sql);
			ResultSet rs = statement.getResultSet();

			if (isResultSet){
				rs.next();
				return new Employee(
						rs.getString("emp_id"),
						rs.getString("fName"),
						rs.getString("lName"),
						rs.getString("username"),
						rs.getString("password"));
			}
			else 
				System.out.println("No employee found");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Customer> fetchOverseenCustomers(Employee currEmp){
		ArrayList<Customer> allOverseenCustomers = new ArrayList<Customer>();
		if (currEmp.getId().equals("1")) {
			sql = "SELECT * FROM customers";
		} else sql = "SELECT * FROM customers WHERE emp_id = "+currEmp.getId();
		
		try {
			Statement statement = connection.createStatement();
			Boolean isResultSet = statement.execute(sql);
			
			if (!isResultSet) {
				System.out.println("No customers found");
			} else {
				ResultSet rs = statement.getResultSet();
				while (rs.next()) {
					allOverseenCustomers.add(new Customer(
							rs.getString("cust_id"),
							rs.getString("fName"),
							rs.getString("lName"),
							rs.getString("gender"),
							rs.getString("dob"),
							rs.getString("emp_id"),
							rs.getString("username"),
							rs.getString("password")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return allOverseenCustomers;
		
	}
	
	public ArrayList<Account> fetchCustomerAccList(Employee currEmp){
		if (currEmp.getId().equals("1")) {
			sql = "SELECT * FROM accounts";
		} else sql = "SELECT distinct accounts.* FROM customers "
				+ "INNER JOIN employees on customers.emp_id = employees.emp_id  "
				+ "INNER JOIN accounts ON customers.cust_id = accounts.owner_id OR "
				+ "customers.cust_id = accounts.joint_id WHERE employees.emp_id = "+currEmp.getId();
		ArrayList<Account> allOverseenAcc = new ArrayList<Account>();
		
		try {
			Statement statement = connection.createStatement();
			Boolean isResultSet = statement.execute(sql);
			
			if (!isResultSet) {
				System.out.println("No accounts found");
			} else {
				ResultSet rs = statement.getResultSet();
				while (rs.next()) {
					allOverseenAcc.add(new Account(
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
		return allOverseenAcc;
	}

}

