package com.revature;

import java.util.Scanner;

import com.revature.Money.InsufficientFundsException;

import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		ArrayList<Person> personnel = PersonnelLoader.loadPersonnel();
		int accessLevel = 0;
		String loginUsername, loginPassword;
		Person currentUser = new Person();

		// Welcome Message with Login and Optional User Account Creation
		Scanner scanIn = new Scanner(System.in);
		System.out.println("++Welcome to Texas Mule Banking Systems++\n\n"
				+ "Please enter your username (Case Sensitive). If you do not have an account, enter \'guest\'. Enter \'exit\' to close the application");


		// Continue processing input until user enters 'exit'
		while (!(loginUsername = scanIn.nextLine()).equals("exit\n") || accessLevel == 0) {

			// Account Creation Option
			if (loginUsername.equalsIgnoreCase("GUEST")) {
				currentUser = Person.createNew(personnel); // Call Person creation method.
				personnel.add(currentUser); // Add new user to personnel directory
				System.out.print(
						"Thank you for creating a new account. You may now log in.\nPlease enter your username: ");
				loginUsername = scanIn.nextLine();
				
			}

			// Search for user
			while (Person.findUserInDatabase(loginUsername, personnel).getUsername().equals("")) {
				System.out.print("Username not found. Please re-enter your username: ");
				loginUsername = scanIn.nextLine();
			}

			// Prompt Password
			System.out.print("Please enter your password: ");
			loginPassword = scanIn.nextLine();

			// Check password
			while (!currentUser.checkPassword(loginPassword)) {
				System.out.print("Incorrect password. Please re-enter your password: ");
				loginPassword = scanIn.nextLine();
			}

			// User is logged in
			System.out.println("Thank you for logging in, Mr/Mrs. " + currentUser.getlName());
			
			System.out.println("You have access to the following accounts: "+currentUser.getAccess());
			System.out.println("Enter the account number you would like to access or enter 0 to create a new account");
			String accountChoice= scanIn.nextLine();
			while (!accountChoice.contains("0-9*")) {
				System.out.println("Unrecognized input. Please re-enter your selection");
				accountChoice = scanIn.nextLine();
			} 
			if (accountChoice == "0") {
				Account.createNewAccount(personnel);
			} else {
				// TODO
				//DISPLAY ACCOUNT INFORMATION. 
				//GIVE OPTIONS FOR WITHDRAW DEPOSIT AND TRANSFER
			}
				
			
			
		} // end While loop
		System.out.println("Access has been granted, or you are exiting. Successfully exited While loop");

		scanIn.close();

	}
}
