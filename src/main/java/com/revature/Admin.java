package com.revature;

public class Admin extends Person {
	final int ACCESSLEVEL = 1;
	
	//Constructor
	Admin(String fName, String lName, String id, String username, String password){
		super(fName, lName, id, username, password);
	}
	
	
	@Override
	void removeAccess(String accountNumber) {
		//THROW EXCEPTION FOR ILLEGAL ACTION
	}
	
	
}
