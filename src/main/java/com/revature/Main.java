package com.revature;

import java.util.Scanner;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		
		ArrayList<Person> personnel = new ArrayList<Person>();
		int id = 0;
		int accessLevel=0;
		boolean usernameFound;
		
		
		Scanner scanIn = new Scanner(System.in);
		System.out.println("Welcome to Texas Mule Banking Systems.+\n"
				+ "Please enter your username (Case Sensitive). If you do not have an account, enter \"guest\". Enter exit to close the application");
		String loginUsername = scanIn.nextLine();
		
		while (!loginUsername.equals("exit") || accessLevel == 0) {
			if (loginUsername.equalsIgnoreCase("GUEST")) {
				//Account Creation
				System.out.print("Please enter your first name: ");
				String fName = scanIn.nextLine();
				System.out.print("Please enter your last name: ");
				String lName = scanIn.nextLine();
				System.out.print("Please enter your desired username: ");
				String username = scanIn.nextLine();
				System.out.print("Please enter your desired password: ");
				String password = scanIn.nextLine();
				personnel.add(new Person(fName, lName, 101, username, password));
				System.out.print("Thank you for creating a new account. Please enter your username: ");
				loginUsername = scanIn.nextLine();
			}
			else {	//User is attempting to log in
				for (Person p : personnel) {
					if (loginUsername.equals(p.getUsername())){	//Username found, awaiting password
						usernameFound = true;
						System.out.print("Please enter your password: ");
						String loginPassword = scanIn.nextLine();
					}
				}
				if (!usernameFound) {
					
					
//					
//						while (!loginPassword.equals(p.getPassword()) && !loginPassword.equals("exit")) {
//							System.out.println("Incorrect password. Please re-enter your password.");
//							loginPassword = scanIn.nextLine();
//						}
//						if (loginPassword.equals(p.getPassword())) {
//							accessLevel = p.ACCESSLEVEL;
//							System.out.println("You have accessed your account, Mr./Mrs. "+p.getlName());
//							;
//						} else if(loginPassword.equals("exit")) { System.exit(0);}
					} else {
						System.out.println("User not found. Please re-enter your username.");
						loginUsername = scanIn.nextLine();
					}
				}
			}
		}	//end While loop
		System.out.println("Access has been granted, or you are exiting. Successfully exited While loop");
		
		
		
		
		
		scanIn.close();
		
	}
}
