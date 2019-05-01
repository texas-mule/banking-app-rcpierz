package com.revature.rcpierzbankingapp.domain;

import java.util.ArrayList;
import java.util.Scanner;

public class Person {
	public String fName;
	public String lName;
	public String id;
	protected String username;
	protected String password;
	
	Person(String id, String fName, String lName, String username, String password, ArrayList<String> access){
						//Capitalize first and last name
		if (fName.length() > 1) this.fName = fName.substring(0,1).toUpperCase() + fName.substring(1).toLowerCase();
		else this.fName = fName;
		if (lName.length() > 1) this.lName = lName.substring(0,1).toUpperCase() + lName.substring(1).toLowerCase();
		else this.lName = lName;
		this.id = id;
		this.username = username.toLowerCase();
		this.password = password;
	}
	
	public Person() {
		this.fName = "Test";
		this.lName = "User";
		this.id = "0";
		this.username = "username";
		this.password = "password";
	}
	
	
	
	public String getfName() {
		return fName;
	}

	public String getlName() {
		return lName;
	}

	public String getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public boolean checkPassword(String password) {
		return this.password.equals(password);
	}
	
	// findUserInDatabase scans the personnel directory to find the Person object that the user is attempting
	// to log in as, based on their username input
	public static Person findUserInDatabase(String loginUsername, ArrayList<? extends Person> personnel) {
		for (Person p: personnel) {
			if (p.username.equals(loginUsername.toLowerCase()))
					return p;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return id+";"+fName+";"+lName+";"+username+";"+password+";"+access.toString();
	}
	
}
