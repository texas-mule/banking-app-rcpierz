package com.revature;

import java.util.ArrayList;
import java.util.Scanner;

public class Person implements java.io.Serializable {
	public String fName;
	public String lName;
	public String id;
	private String username;
	private String password;
	public int ACCESSLEVEL = 0;
	ArrayList<String> access = new ArrayList<String>();
	
	Person(String id, String fName, String lName, String username, String password, ArrayList<String> access){
						//Capitalize first and last name
		if (fName.length() > 1) this.fName = fName.substring(0,1).toUpperCase() + fName.substring(1).toLowerCase();
		else this.fName = fName;
		if (lName.length() > 1) this.lName = lName.substring(0,1).toUpperCase() + lName.substring(1).toLowerCase();
		else this.lName = lName;
		this.id = id;
		this.username = username.toLowerCase();
		this.password = password;
		this.access = access;
	}
	
	public Person() {
		this.fName = "Test";
		this.lName = "User";
		this.id = "0";
		this.username = "username";
		this.password = "password";
		this.access = new ArrayList<String>();
	}

	public static ArrayList<String> parseAccess(String accessStr){
		ArrayList<String> access = new ArrayList<String>();
		String[] accessArr = accessStr.substring(1,accessStr.length()-1).split(";");
		for (String s : accessArr) {
			System.out.println(s);
			access.add(s);
		}
		return access;
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
		System.out.println("Checking against: " +this.toString());
		return this.password.equals(password);
	}
	
	public static Person createNew(ArrayList<Person> personnel) {
		String fName, lName, username, password;
		String id = "1";
		Scanner sc = new Scanner(System.in);
		System.out.print("Please enter your first name: ");
		fName = sc.nextLine();
		System.out.print("Please enter your last name: ");
		lName = sc.nextLine();
		System.out.print("Pleae enter your desired username: ");
		username = sc.nextLine();
		while (Person.findUserInDatabase(username,personnel) != null) {
			System.out.println("Username already exists. Please enter a new username: ");
			username = sc.nextLine();
		}
		System.out.print("Please enter your desired password: ");
		password = sc.nextLine();
		Person newUser = new Person(id, fName,lName, username, password, new ArrayList<String>());
		newUser.access.add("005");
		newUser.access.add("006");
		PersonnelLoader.savePersonnel(newUser);
		return newUser;
	}
	
	// findUserInDatabase scans the personnel directory to find the Person object that the user is attempting
	// to log in as, based on their username input
	public static Person findUserInDatabase(String loginUsername, ArrayList<Person> personnel) {
		for (Person p: personnel) {
			if (p.username.equals(loginUsername.toLowerCase()))
					return p;
		}
		//return new Person("","","","","");
		return null;
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
	
	@Override
	public String toString() {
		return id+";"+fName+";"+lName+";"+username+";"+password+";"+access.toString();
	}
	
}