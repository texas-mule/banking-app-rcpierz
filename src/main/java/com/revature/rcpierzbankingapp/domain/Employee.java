package com.revature.rcpierzbankingapp.domain;

import java.util.ArrayList;
import java.util.Scanner;

public class Employee extends Person {
	static Employee currEmp;
	
	public Employee(String id, String fName, String lName, String username, String password) {
		this.id = id;
		this.fName = fName;
		this.lName = lName;
		this.username = username;
		this.password = password;
	}
	
	public String getFName() {
		return fName;
	}
	
	public String getLName() {
		return lName;
	}
	
	public static Employee employeeLogin(ArrayList<Employee> allEmp) {
		Scanner scanIn = new Scanner(System.in);
		String userInput;
		int passLock = 0;
		
		// Log in as Employee
		System.out.print("Please enter your username: ");
		userInput = scanIn.nextLine().toLowerCase();
		
		// find user in database
		while ((currEmp = (Employee)Employee.findUserInDatabase(userInput, allEmp)) == null) {
			System.out.print("Username not found. Please re-enter your username: ");
			userInput = scanIn.nextLine();
		}

		// Authenticate password
		System.out.print("Please enter your password: ");
		userInput = scanIn.nextLine();

		// User is locked out after 5 failed attempts
		while (!(currEmp.checkPassword(userInput)) && passLock < 5) {
			passLock++;
			if (passLock == 5) {
				System.out.println("Fatal Error: Account is locked. Exiting...");
				System.exit(0);
			}
			System.out.print("ERROR: Password did not match records. Please re-enter your password ("+
						(5-passLock) + (passLock==4 ? " try" : " tries") + " remaining): ");
			userInput = scanIn.nextLine();
		}
		return currEmp;
	}
	
	public String approvePendingAccounts(ArrayList<Account> pendingAccList, AccountDAO accDAO, EmployeeDAO empDAO){
		Scanner scanIn = new Scanner(System.in);
		String doContinueApproving;
		
		Account.displayAccounts(pendingAccList);
		
		System.out.print("Enter the account number that you would like to access: ");
		String accInput = scanIn.nextLine();
		if (Account.checkIdInList(accInput, pendingAccList)) {
			Account currAcc = Account.pullAccountFromId(accInput, pendingAccList);
			System.out.print("[A]pprove or [D]eny application: ");
			String approveChoice = scanIn.nextLine().toUpperCase();
			while (!approveChoice.matches("(A|D)")){
				System.out.print("ERROR: Unrecognized input. Please enter \'A\' or \'D\': ");
				approveChoice = scanIn.nextLine().toUpperCase();
			}
			if (approveChoice.equals("A")) currAcc.approveAccount();
			else if(approveChoice.equals("D")) currAcc.denyAccount();
			accDAO.updateAccount(currAcc);
		}
		
		// Get updated Customer Account information
		ArrayList<Account> empAccList = empDAO.fetchCustomerAccList(this);
		pendingAccList = Account.getPendingAcc(empAccList);
		
		if (pendingAccList.size() != 0) {
			Account.displayAccounts(pendingAccList);
			System.out.println("There are remaining accounts which are still pending. Would you like to approve more accounts (\'Y\' or \'N\'): ");
			doContinueApproving = scanIn.nextLine().toUpperCase();
		} else {doContinueApproving = "N";}
		return doContinueApproving;
	}
}