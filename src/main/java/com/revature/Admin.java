package com.revature;

import java.util.ArrayList;

public class Admin extends Person {
	final int ACCESSLEVEL = 1;
	
	//Constructor
	Admin(String fName, String lName, String id, String username, String password, ArrayList<String> access){
		super(fName, lName, id, username, password, access);
	}
	
	
	@Override
	void removeAccess(String accountNumber) {
		//THROW EXCEPTION FOR ILLEGAL ACTION
	}
	
	
}
