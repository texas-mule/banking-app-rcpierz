//package com.revature;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//
//public class AccountLoader {
//	public static ArrayList<Account> loadAccounts() {
//		ArrayList<Account> accounts = new ArrayList<Account>();
//		BufferedReader br = null;
//		String line = "";
//		String delimit = ";";
//		String[] data;
//		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
//		if (!(new File("com/revature/accounts.txt")).exists()) {
//			File inputFile = File.createNewFile("com/revature/accounts.txt");
//			
//		}
//		InputStream is = classLoader.getResourceAsStream("com/revature/accounts.txt");
//		br = new BufferedReader(new InputStreamReader(is));
//
//		try {
//			while ((line = br.readLine()) != null) {
//				data = line.split(delimit);
//				Account currentAccount = new Account(data[0], Boolean.parseBoolean(data[1]), (AccountType)AccountType.valueOf(data[2]), new Money(data[3]));
//
//				System.out.println("Adding to database: "+currentAccount.toString());
//				accounts.add(currentAccount);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				br.close();
//			} catch (IOException e) {
//				System.out.println("Unable to load accounts.txt");
//				System.exit(0);
//			}
//		}
//		
//		return accounts;
//	}
//	
//	public static void saveAccount(Account a) {
//		BufferedWriter bw;
//		String line="";
//		FileWriter fw = null;
//		try {
//			fw = new FileWriter("C:\\Users\\RCP\\git\\banking-app-rcpierz\\src\\main\\java\\com\\revature\\accounts.txt", true);
//			bw = new BufferedWriter(fw);
//			System.out.println("Saving "+a.toString() + " to accounts.txt");
//			line = a.writeToText();
//			fw.append("\n"+line);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			System.out.println("Failed to write to external file.");
//		} finally {
//			try {
//				fw.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				System.out.println("Failed to close output file.");
//			}
//		}
//	}
//}
