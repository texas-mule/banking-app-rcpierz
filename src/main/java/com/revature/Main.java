package com.revature;

import java.util.Scanner;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {

		ArrayList<Person> personnel = new ArrayList<Person>();
		int id = 0;
		int accessLevel = 0;
		boolean usernameFound = false, passwordFound = false;
		String loginUsername, loginPassword;
		Person currentUser = new Person();

		Scanner scanIn = new Scanner(System.in);
		System.out.println("Welcome to Texas Mule Banking Systems.+\n"
				+ "Please enter your username (Case Sensitive). If you do not have an account, enter \"guest\". Enter exit to close the application");

		// Continue processing input until user enters 'exit'
		while (!(loginUsername = scanIn.nextLine()).equals("exit\n") || accessLevel == 0) {

			// Account Creation Option
			if (loginUsername.equalsIgnoreCase("GUEST")) {
				while (!(Person.findUserInDatabase(loginUsername, personnel).id == 0)) {
					System.out.println("Username already exists. Please enter a new username.");
					loginUsername = scanIn.nextLine();
				}
				currentUser = Person.createNew(); // Call Person creation method.
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

		} // end While loop
		System.out.println("Access has been granted, or you are exiting. Successfully exited While loop");

		scanIn.close();

	}
}
