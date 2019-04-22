package com.revature;

import java.util.ArrayList;

public class Customer extends Person {
	final int ACCESSLEVEL = 3;
	Customer(String fName, String lName, String id, String username, String password, ArrayList<String> access){
		super(fName, lName, id, username, password, access);
	}
	
}
