package com.revature.rcpierzbankingapp.domain;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Customer extends Person {
	public String gender, emp_id, dob;
	static Customer currCust;

	// Constructor using passed SQL data in String[] form
	Customer(String[] customerData) {
		this.id = customerData[0];
		this.fName = customerData[1];
		this.lName = customerData[2];
		this.gender = customerData[3];
		this.dob = customerData[4];
		this.emp_id = customerData[5];
		this.username = customerData[6];
		this.password = customerData[7];
	}

	public Customer(String cust_id, String fName, String lName, String gender, String dob, String emp_id,
			String username, String password) {
		this.id = cust_id;
		this.fName = fName;
		this.lName = lName;
		this.gender = gender;
		this.dob = dob;
		this.emp_id = emp_id;
		this.username = username;
		this.password = password;
	}
	
	public String getEmp_id() {
		return emp_id;
	}

	public String getGender() {
		return gender;
	}

	public static Customer createNew(ArrayList<Customer> allCust, ArrayList<Employee> allEmp) {
		
		String fName, lName, gender, dob, username, password;
		String id;
		Random rand = new Random();
		// TODO Disallow "1" as an option
		String emp_id = Integer.toString(rand.nextInt(allEmp.size())+1);
		
		
		id = Integer.toString(++CustomerDAO.maxID);
		Scanner sc = new Scanner(System.in);
		
		// Get first name
		System.out.print("Please enter your first name: ");
		fName = sc.nextLine();
		
		// Get last name
		System.out.print("Please enter your last name: ");
		lName = sc.nextLine();
		
		// Get gender
		System.out.print("What is your gender? (M) for Male, (F) for Female, or (U) for Undisclosed: ");
		while (!(gender = sc.nextLine()).toUpperCase().matches("(M|F|U)")) {
			System.out.print("ERROR: Unrecognized input. Please enter 'M' for Male, \'F\' for Female, or \'U\' for Undisclosed: ");
		}
		
		// Get date of birth
		System.out.print("Please enter your date of birth (MMDDYYYY): ");
		while (!(dob = sc.nextLine()).matches("(0|1)\\d[0-3]\\d(19|20)\\d{2}")) {
			System.out.print("ERROR: Unrecognized input. Please enter your date of birth in the format MMDDYYYY: ");
		}
		
		// Create new username
		System.out.print("Please enter your desired username: ");
		while (Person.findUserInDatabase(username = sc.nextLine(),allCust) != null || (username.equalsIgnoreCase("guest"))) {
			System.out.println("ERROR: Username already exists. Please enter a new username: ");
		}
		
		// Create new password
		System.out.print("Please enter your desired password: ");
		password = sc.nextLine();
		Customer newCust = new Customer(id, fName,lName, gender, dob, emp_id, username, password);
		
		// Give account creation feedback 
		System.out.println("Account successfully created!");
		System.out.println();
		
		TransactionDAO.logNewUser(newCust);
		return newCust;
	}
	
	public static Customer customerLogin(ArrayList<Customer> allCust, ArrayList<Employee> allEmp, CustomerDAO custDAO) {
		Scanner scanIn = new Scanner(System.in);
		String userInput;
		int passLock = 0;
		
		// Log in as Customer
		System.out.print("Please enter your username (or enter \'guest\' to create a new account): ");
		userInput = scanIn.nextLine().toLowerCase();

		if (userInput.equals("guest")){
			currCust = Customer.createNew(allCust, allEmp);
			custDAO.addCustomer(currCust);
			return currCust;
		}
		
		// find user in database
		while ((currCust = (Customer)Customer.findUserInDatabase(userInput, allCust)) == null) {
			System.out.print("Username not found. Please re-enter your username: ");
			userInput = scanIn.nextLine();
		}

		// Authenticate password
		System.out.print("Please enter your password: ");
		userInput = scanIn.nextLine();

		// User is locked out after 5 failed attempts
		while (!(currCust.checkPassword(userInput)) && passLock < 5) {
			passLock++;
			if (passLock == 5) {
				System.out.println("Fatal Error: Account is locked. Exiting...");
				System.exit(0);
			}
			System.out.print("ERROR: Password did not match records. Please re-enter your password ("+
						(5-passLock) + (passLock==4 ? " try" : " tries") + " remaining): ");
			userInput = scanIn.nextLine();
		}
		return currCust;
	}
	
	public static void displayAccounts(ArrayList<Account> custAccList) {
		// Display accounts to the user
		System.out.println("You have access to the following accounts: ");
		System.out.println("Account\tStatus\tBalance");
		for (Account acc : custAccList) {
			System.out.println(
					acc.getAccountId()+ "\t" +
					acc.getStatus().toString() + "\t" +
					acc.getBalance());
		}
	}
	
	public static void displayCustomerInformation(ArrayList<Customer> customerList) {
		System.out.println("ID\tfName\t\tlName\t\tgender\tDate of Birth\tEmp ID\tUsername\tPassword");
		for (Customer cust : customerList) {
			System.out.println(cust.toString());
		}
	}
	
	public static ArrayList<Account> displayOpenAccounts(ArrayList<Account> custAccList) {
		// Display the user accounts that are open
		ArrayList<Account> custOpenAccList = new ArrayList<Account>();
		System.out.println("Account\tStatus\tBalance");
		System.out.println("You have access to the following OPEN accounts: ");
		for (Account acc : custAccList) {
			if (acc.isOpen()){
				custOpenAccList.add(acc);
				System.out.println(
						acc.getAccountId()+ "\t" +
						acc.getStatus().toString() + "\t" +
						acc.getBalance());
			}
		}
		return custOpenAccList;
	}
	
	public static Boolean isIdInList(String id, ArrayList<Customer> custList) {
		for (Customer cust : custList) {
			if (id.equals(cust.getId()))
				return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return id + "\t" + fName + "\t" + lName + "\t" + gender + "\t" + dob
				+ "\t" + emp_id + "\t" + username + "\t" + password;
	}

}
