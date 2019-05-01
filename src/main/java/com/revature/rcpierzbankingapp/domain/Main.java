package com.revature.rcpierzbankingapp.domain;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		SQLConnection sqlconnection = SQLConnection.getInstance();
		String userInput, accId, continueA, continueB, continueC;
		Customer currCust;
		Boolean adminStatus=false;
		
		Properties prop = new Properties();
		try {
			prop.load(ClassLoader.getSystemResourceAsStream("connection.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Scanner scanIn = new Scanner(System.in);

		// Create/Open a connection to a postgres instance and take in Scanner input from standard input
		try (Connection connection = sqlconnection.establishConnection(prop.getProperty("url"), prop.getProperty("user"), 
				prop.getProperty("password"))){
			CustomerDAO custDAO = CustomerDAO.establish(connection);
			EmployeeDAO empDAO = EmployeeDAO.establish(connection);
			AccountDAO accDAO = AccountDAO.establish(connection);
			TransactionDAO transDAO = TransactionDAO.establish(connection);

			System.out.println("++++++++++++++++++++++++++++++++++\n"+
					   		   "++Welcome to the Texas Mule Bank++\n"+
							   "++++++++++++++++++++++++++++++++++");
			
			// persist until exit
			while (true) {
				Statement statement = connection.createStatement();

				// get all customer and employee data
				ArrayList<Customer> allCust = custDAO.getAll();
				ArrayList<Employee> allEmp = empDAO.getAll();
				ArrayList<Account> allAcc = accDAO.getAll();
				
				
				
				if (CustomerDAO.getInstance().getAll() == null) {
					System.out.println("No customers were found. Please create a primary customer before continuing");
					currCust = Customer.createNew(allCust, allEmp);
					custDAO.addCustomer(currCust);
				}
				
				System.out.println("Proceeding to login...");
				
				// customers were found, continue to log in options
				System.out.print("Are you logging in as a Customer (C) or Employee (E): ");
				userInput = scanIn.nextLine().toUpperCase();

				if (userInput.equals("exit")) break;
				
				while (!((userInput.equals("C") || userInput.equals("E")))) {
					System.out.print("Unrecognized input. Please enter 'C' for Customer or 'E' for Employee: ");
					userInput = scanIn.nextLine().toUpperCase();
				}
				// User is logging in as Customer
				if (userInput.equals("C")) {
					// Run Log in Process, return Customer object
					currCust = Customer.customerLogin(allCust, allEmp, custDAO);
					System.out.println("Login successful, Welcome "+
							(currCust.getGender().equals("U") ? "Mr/Mrs " :
							(currCust.getGender().equals("M") ? "Mr " : "Mrs "))+
							 currCust.getlName()+". You have been assigned Employee "+
							(empDAO.getEmpByID(currCust.getEmp_id())).getLName());
					
					
					//**CUSTOMER INDIVIDUAL ACCOUNT ACCESS PROCESS**//
					ArrayList<Account> custAccList = accDAO.fetchAvailableCustomerAccounts(currCust.getId());
					if (custAccList.size() == 0) {
						
						System.out.println("Creating initial account...");
						Account newAcc = Account.createNewAccount(currCust, custDAO);
						accDAO.addAccountToDatabase(currCust, newAcc);
					} 

					// Display all accounts available to user
					Customer.displayAccounts(custAccList);
					
					// New Account Creation option
					if (userInput.equals("0")) {
						System.out.println();
						System.out.println("Creating new account...");
						Account newAcc = Account.createNewAccount(currCust, custDAO);
						accDAO.addAccountToDatabase(currCust, newAcc);
					}
					
					else {
						
						// TODO Check for open accounts
						ArrayList<Account> openAccList = accDAO.fetchOpenCustomerAccounts(currCust.getId());
						if (openAccList.size() != 0) {
						
							while (true) {
								System.out.print("Please enter the account number that you would like to access: ");
								accId = scanIn.nextLine();
								
								Account currAcc;
								
								while  (!Account.checkOpenIdInList(accId, custAccList)) {
									if (Account.checkIdInList(accId, custAccList))
										System.out.println("ERROR: Unable to access pending accounts!");
									System.out.println("Invalid entry. Please re-enter the account id that you would like to access");
									accId = scanIn.nextLine();
								}
								
								while (true) {
									currAcc = accDAO.fetchAccountByAccId(accId);
									currAcc.editAccountBalance(currCust, custAccList, accDAO);
									System.out.println("Would you like to make another transaction (\'Y\' or \'N\'): ");
									continueB = scanIn.nextLine().toUpperCase();
									if (!(continueB.equals("Y"))) break;
								}
								System.out.println("Would you like to access a different account (\'Y\' or \'N\': ");
								continueA = scanIn.nextLine().toUpperCase();
								if (!(continueB.equals("Y"))) break;
							}
						}
					}

				} else if (userInput.equals("E")) {
					// Log in as Employee					
					Employee currEmp = Employee.employeeLogin(allEmp);
					System.out.println("Welcome, "+currEmp.getFName() + " "+currEmp.getLName());
					
					// Change adminStatus if logged in as Admin (i.e. ID = 1)
					if (currEmp.getId().equals("1")) {
						adminStatus = true;
					}
					
					while (true) {
					
						System.out.print("Would you like to view [customers] information or view available [accounts] (or [exit]): ");
						userInput = scanIn.nextLine().toLowerCase();
						if (userInput.equals("exit")) break;
						
						while (!(userInput.equals("customers") || userInput.equals("accounts"))) {
							System.out.println("ERROR: Unrecognized input. Please enter \'customer\' or \'accounts\': ");
							userInput = scanIn.nextLine().toLowerCase();
						}
						
						if(userInput.equals("customers")) {
							ArrayList<Customer> allOverseenCustomers = empDAO.fetchOverseenCustomers(currEmp);
							Customer.displayCustomerInformation(allOverseenCustomers);
						}
					
						else if (userInput.equals("accounts")) {
							
							if (!adminStatus) {
								ArrayList<Account> empAccList = empDAO.fetchCustomerAccList(currEmp);
								Account.displayAccounts(empAccList);
								ArrayList<Account> pendingAccList = Account.getPendingAcc(empAccList);
								
								while (pendingAccList.size() != 0) {	//If there are pending accounts
									// Give option to edit pending accounts
									System.out.print("There are pending accounts. Would you like to approve accounts (\'Y\' or \'N\'): ");
									userInput = scanIn.nextLine().toUpperCase();
									
									while (!(userInput.matches("(Y|N)") || userInput.equals("EXIT"))){
										System.out.print("ERROR: Unrecognized input. Please enter \'Y\', \'N\' or \'exit\'");
										userInput = scanIn.nextLine().toUpperCase();
									}
									if (userInput.equals("Y")) {
										userInput = currEmp.approvePendingAccounts(pendingAccList, accDAO, empDAO);
									} else if (userInput.equals("N")) break;
									else if(userInput.equals("exit")) System.exit(0);
									
									pendingAccList = Account.getPendingAcc(empAccList);
								}
								if (pendingAccList.size() == 0)
									System.out.println("There are no currently pending accounts.");
							}
	
							else if(adminStatus) {
								while(true) {
									ArrayList<Account> empAccList = empDAO.fetchCustomerAccList(currEmp);
									Account.displayAccounts(empAccList);
									ArrayList<Account> pendingAccList = Account.getPendingAcc(empAccList);
									
									System.out.print("Enter the ID of the account you would like to access (or 'N' to stop): ");
									userInput = scanIn.nextLine().toUpperCase();
									while (!(userInput.matches("(\\d+|N)"))){
										System.out.println("ERROR: Unrecognized input. Please enter an account number or \'N\' to stop accessing accounts: ");
										userInput = scanIn.nextLine().toUpperCase();
									}
									
									if (userInput.matches("N")) break;
									
									Account currAcc = accDAO.fetchAccountByAccId(userInput);
									currAcc.displayAccount();
									
									while(true) {
										System.out.println("Would you like to [edit] the account information or perform a [transaction] on it: ");
										userInput = scanIn.nextLine().toUpperCase();
										while(!(userInput.equals("EDIT") || userInput.equals("TRANSACTION"))) {
											System.out.println("ERROR: Unrecognized input. Please enter \'edit\' or \'transaction\': ");
											userInput = scanIn.nextLine().toUpperCase();
										}
										if (userInput.equals("EDIT")){
											while (true) {
												
												System.out.println("Enter one of the following attributes to edit: [owner], [joint], [type], [status]: ");
												userInput = scanIn.nextLine().toUpperCase();
												while (!(userInput.equals("OWNER") ||
														userInput.equals("JOINT") ||
														userInput.equals("TYPE") ||
														userInput.equals("STATUS"))) {
													System.out.println("ERROR: Unrecognized input. Please enter \'owner\', \'joint\', \'type\', or \'status\'");
													userInput = scanIn.nextLine().toUpperCase();
												}
												if (userInput.equals("OWNER")){
													ArrayList<Customer> allOverseenCustomers = empDAO.fetchOverseenCustomers(currEmp);
													Customer.displayCustomerInformation(allOverseenCustomers);
													System.out.println("Enter the ID that you would like to associate this account with: ");
													userInput = scanIn.nextLine();
													currCust = null;
													while ((!(Customer.isIdInList(userInput, allOverseenCustomers)) || userInput.equals(currAcc.getJointId()))) {
														System.out.print("ERROR: Unrecognized ID. Please enter a valid ID to associate the current account with: ");
														userInput = scanIn.nextLine();
													}
													currAcc.setOwnerId(userInput);
													accDAO.updateAccount(currAcc);
													currAcc.displayAccount();
													TransactionDAO.logAccountEdit(currEmp, currAcc, "Owner", userInput);
												}
												else if(userInput.equals("JOINT")) {
													ArrayList<Customer> allOverseenCustomers = empDAO.fetchOverseenCustomers(currEmp);
													Customer.displayCustomerInformation(allOverseenCustomers);
													System.out.println("Enter the ID that you would like to replace as the joint user for the account (or 0 to remove joint user): ");
													userInput = scanIn.nextLine();
													Customer jointCust;
													while ((!(Customer.isIdInList(userInput, allOverseenCustomers)) ||
															(userInput.equals(currAcc.getOwnerId())))) {
														if (userInput.equals("0")) break;	// joint id == 0 means no joint user so dont error check
														System.out.print("ERROR: Unrecognized ID. Please enter a valid ID to replace as the joint user: ");
														userInput = scanIn.nextLine();
													}
													currAcc.setJointId(userInput);
													accDAO.updateAccount(currAcc);
													currAcc.displayAccount();
													TransactionDAO.logAccountEdit(currEmp, currAcc, "Joint User", userInput);
												}
												else if(userInput.equals("TYPE")) {
													System.out.println("Edit account to be [Checking] or [Savings]: ");
													userInput = scanIn.nextLine().toUpperCase();
													currAcc.setType(userInput);
													accDAO.updateAccount(currAcc);
													currAcc.displayAccount();
													TransactionDAO.logAccountEdit(currEmp, currAcc, "Type", userInput);
												}
												else if(userInput.equals("STATUS")) {
													if (currAcc.getStatus().equals("Open")) {
														System.out.print("Would you like to close this account? (\'Y\' or \'N\'): ");
														userInput = scanIn.nextLine().toUpperCase();
														while (!(userInput.matches("(Y|N)"))) {
															System.out.print("ERROR: Unrecognized input. Please enter \'Y\' or \'N\': ");
															userInput = scanIn.nextLine().toUpperCase();
														}
														if (userInput.equals("Y")){
															currAcc.denyAccount();
															accDAO.updateAccount(currAcc);
															TransactionDAO.logAccountEdit(currEmp, currAcc, "Status", "Closed");
														}
													}
													else if(currAcc.getStatus().equals("Closed")) {
														System.out.print("Would you like to open this account? (\'Y\' or \'N\'): ");
														userInput = scanIn.nextLine().toUpperCase();
														while (!(userInput.matches("(Y|N)"))) {
															System.out.print("ERROR: Unrecognized input. Please enter \'Y\' or \'N\': ");
															userInput = scanIn.nextLine().toUpperCase();
														}
														if (userInput.equals("Y")) {
															currAcc.approveAccount();
															accDAO.updateAccount(currAcc);
															TransactionDAO.logAccountEdit(currEmp, currAcc, "Status", "Open");
														}
													}
													else if(currAcc.getStatus().equals("Pending")) {
														System.out.println("Would you like to [open] or [close] this account: ");
														userInput = scanIn.nextLine().toUpperCase();
														while (!(userInput.equals("OPEN") || userInput.equals("CLOSE"))){
															System.out.println("ERROR: Unrecognized input. Please enter \'open\' or \'close\': ");
															userInput = scanIn.nextLine().toUpperCase();
														}
														if (userInput.equals("OPEN")) {
															currAcc.approveAccount();
															accDAO.updateAccount(currAcc);
															TransactionDAO.logAccountEdit(currEmp, currAcc, "Status", "Open");
														} else if(userInput.equals("CLOSE")) {
															currAcc.denyAccount();
															accDAO.updateAccount(currAcc);
															TransactionDAO.logAccountEdit(currEmp, currAcc, "Status", "Closed");
														}
													}
													currAcc.displayAccount();	
												}
												System.out.println("Would you like to continue editing this account? (\'Y\' or \'N\'): ");
												continueC = scanIn.nextLine().toUpperCase();
												if (!(continueC.equals("Y"))) break;
											}
										}
										else if(userInput.equals("TRANSACTION")) {
											while (true) {
												currAcc.editAccountBalance(currEmp, empAccList, accDAO);
												
												System.out.println("Would you like to continue performing transactions on this account? (\'Y\' or \'N\'): ");
												continueC = scanIn.nextLine().toUpperCase();
												if (!(continueC.equals("Y"))) break;
											}
										}
									System.out.println("Would you like to continue accessing this account? (\'Y\' or \'N\'): ");
									continueB = scanIn.nextLine().toUpperCase();
									if (!(continueB.equals("Y"))) break;
									}
								System.out.print("Would you like to access another account (\'Y\' or \'N\'): ");
								continueA = scanIn.nextLine().toUpperCase();
								if (!continueA.matches("Y")) break;
								}
							}
						}
					}
				}
			} // end While loop
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
	}
}
