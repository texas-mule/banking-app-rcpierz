package com.revature;

import java.util.ArrayList;
import java.io.*;

public class PersonnelLoader {
	public static ArrayList<Person> loadPersonnel() {
		ArrayList<Person> personnel = new ArrayList<Person>();
		BufferedReader br = null;
		String line = "";
		String delimit = ",";
		int lineCounter = 0;
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		InputStream is = classLoader.getResourceAsStream("com/revature/personnel.txt");
		
		br = new BufferedReader(new InputStreamReader(is));
		
		try {
			while ((line = br.readLine()) != null) {
				String data[] = line.split(delimit);
				personnel.add(new Person(data[0], data[1], data[2], data[3], data[4]));
			}
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return personnel;
	}
	
	public static void savePersonnel(Person p) {
		BufferedWriter bw = null;
		String line="";
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		FileWriter fw;
		try {
			fw = new FileWriter("personnel.txt", true);
			bw = new BufferedWriter(fw);
			System.out.println(p.toString());
			line = p.toString();
			fw.write(line);
			fw.write("\n");
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
