package com.revature;

import java.util.ArrayList;

public class Person implements java.io.Serializable {
	public String fName;
	public String lName;
	public long id;
	private String username;
	private String password;
	public int ACCESSLEVEL = 0;
	ArrayList<String> access = new ArrayList<String>();
	
	Person(String fName, String lName, long id, String username, String password){
		this.fName = fName;
		this.lName = lName;
		this.id = id;
		this.username = username;
		this.password = password;
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

	public String getPassword() {
		return password;
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
