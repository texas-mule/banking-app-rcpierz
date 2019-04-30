package com.revature;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Driver {

	public static void main(String[] args) {
		// url = jdbc:subprotocol://url-ip:rdbmsport/database-name
		String url = "jdbc:postgresql://192.168.99.100:5432/bankingapp";
		String username = "bankingapp";
		String password = "p4ssw0rd";
		String sql;
		
		
		// Create/Open a connection to a postgres instance and take in Scanner input
		// from standard input

		try (Connection connection = DriverManager.getConnection(url, username, password)){
			Scanner scanner = new Scanner(System.in);
			
			while (true) {
				Statement statement = connection.createStatement();
				System.out.println("sql> ");
				sql = scanner.nextLine();
				if (sql.equalsIgnoreCase("quit"))
					break;
				
				boolean isResultSet = statement.execute(sql); // returns true or false if resultSet
				
				// statement.execute(sql) runs the statement input by user and 
				// returns true for a statement or false for a number
				// If true, return the data to the user
				// If false, return the # of rows affected
				if (isResultSet) {
					// Print Table
					ResultSet resultSet = statement.getResultSet();
					
					ResultSetMetaData rsmd = resultSet.getMetaData();
					// Print rows 
					while (resultSet.next()) {
//						Food currentRow = new Food();
						//Starts out on nothing, then first row (so start on .next)
						for (int i = 1; i <= rsmd.getColumnCount(); i++) {
							
							System.out.print(resultSet.getString(i)+ "\t");
//							currentRow.id = resultSet.getInt("id");
//							currentRow.name = resultSet.getString("name");
							}


//						System.out.print(currentRow);
						System.out.println();
					}
					resultSet.close();
					
				} else {
					// Print rows affected
					System.out.println(statement.getUpdateCount() + " rows affected.");
				} 
				
				// close statement, not connection
				statement.close();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
	}

}

//class Food {
//	int id;
//	String name;
//	
//	@Override
//	public String toString() {
//		return id + "\t" + name;
//	}
//}









