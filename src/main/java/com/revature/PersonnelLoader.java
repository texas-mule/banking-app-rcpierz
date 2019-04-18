package com.revature;

import java.util.ArrayList;
import java.io.*;

public class PersonnelLoader {
	public static ArrayList<Person> loadPersonnel() {
		ArrayList<Person> personnel = new ArrayList<Person>();
		BufferedReader br = null;
		String line = "";
		String delimit = "\\t";
		int lineCounter = 0;
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		InputStream is = classLoader.getResourceAsStream("rcpierz.project0/personnel.ser");
		
		
		br = new BufferedReader(new InputStreamReader(is));
		
		
		return personnel;
	}
}
