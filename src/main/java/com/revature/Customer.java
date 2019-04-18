package com.revature;

public class Customer extends Person {
	final int ACCESSLEVEL = 3;
	Customer(String fName, String lName, int id, String username, String password){
		super(fName, lName, id, username, password);
	}
	
}
