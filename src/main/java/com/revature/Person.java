package com.revature;

import java.util.ArrayList;
import java.util.Scanner;

public class Person implements java.io.Serializable {
	public String fName;
	public String lName;
	public long id;
	private String username;
	private String password;
	public int ACCESSLEVEL = 0;
	ArrayList<String> access = new ArrayList<String>();
	
	Person(String fName, String lName, long id, String username, String password){
						//Capitalize first and last name
		this.fName = fName.substring(0,1).toUpperCase() + fName.substring(1).toLowerCase();
		this.lName = lName.substring(0,1).toUpperCase() + lName.substring(1).toLowerCase();
		this.id = id;
		this.username = username.toLowerCase();
		this.password = password;
	}
	
	public Person() {
		this.fName = "Test";
		this.lName = "User";
		this.id = 0;
		this.username = "username";
		this.password = "password";
	}

	public String getfName() {
		return fName;
	}

	public String getlName() {
		return lName;
	}

	public long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public boolean checkPassword(String password) {
		return this.password.equals(password);
	}
	
	public static Person createNew() {
		String fName, lName, username, password;
		int id = 1;
		Scanner sc = new Scanner(System.in);
		System.out.print("Please enter your first name: ");
		fName = sc.nextLine();
		System.out.print("Please enter your last name: ");
		lName = sc.nextLine();
		System.out.print("Pleae enter your desired username: ");
		username = sc.nextLine();
		System.out.print("Please enter your desired password: ");
		password = sc.nextLine();
		return new Person(fName, lName, id, username, password);
	}
	
	// findUserInDatabase scans the personnel directory to find the Person object that the user is attempting
	// to log in as, based on their username input
	public static Person findUserInDatabase(String loginUsername, ArrayList<Person> personnel) {
		for (Person p: personnel) {
			if (p.username.equals(loginUsername))
					return p;
		}
		return new Person("","",0,"","");
	}

	public ArrayList<String> getAccess() {
		return access;
	}

	void addAccess(String accountNumber) {
		access.add(accountNumber);
	}
	void removeAccess(String accountNumber) {
		access.remove(accountNumber);
	}
	
	
}
