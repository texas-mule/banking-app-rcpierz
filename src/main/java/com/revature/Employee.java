package com.revature;

import java.util.ArrayList;

public class Employee extends Person {
	final int ACCESSLEVEL = 2;
	Employee(String fName, String lName, String id, String username, String password, ArrayList<String> access){
		super(fName, lName, id, username, password, access);
	}
	
}
