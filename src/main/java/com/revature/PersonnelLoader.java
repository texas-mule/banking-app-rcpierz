package com.revature;

import java.util.ArrayList;
import java.io.*;

public class PersonnelLoader {
	public static ArrayList<Person> loadPersonnel() {
		ArrayList<Person> personnel = new ArrayList<Person>();
		BufferedReader br = null;
		String line = "";
		String delimit = ";";
		int lineCounter = 0;
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		InputStream is = classLoader.getResourceAsStream("com/revature/personnel.txt");
		
		br = new BufferedReader(new InputStreamReader(is));
	
		try {
			while ((line = br.readLine()) != null) {
				String data[] = line.split(delimit);
				Person currentPerson = new Person(data[0], data[1], data[2], data[3], data[4], Person.parseAccess(data[5]));
				System.out.println("Loading "+currentPerson.toString()+" into current database from personnel.txt");
				personnel.add(currentPerson);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Unable to close input file.");
			}
		}
		
		return personnel;
	}
	
	public static void savePersonnel(Person p) {
		BufferedWriter bw;
		String line="";
		FileWriter fw = null;
		try {
			fw = new FileWriter("C:\\Users\\RCP\\git\\banking-app-rcpierz\\src\\main\\java\\com\\revature\\personnel.txt", true);
			bw = new BufferedWriter(fw);
			System.out.println("Saving "+p.toString() + " to personnel.txt");
			line = p.toString();
			fw.append("\n"+line);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to write to external file.");
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Failed to close output file.");
			}
		}
	}
}
