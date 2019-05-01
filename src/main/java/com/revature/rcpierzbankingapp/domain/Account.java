package com.revature.rcpierzbankingapp.domain;

import java.util.ArrayList;
import java.util.Scanner;

public class Account {
									// SQL Index
	public String id; 				// 1
	public int owner_id; 			// 2
	public int joint_id; 			// 3
	public AccountType type; 		// 4
	private Money balance; 			// 5
	public Boolean status; 			// 6

	// Default Account
	public Account() {
		this.id = null;
		this.owner_id = 0;
		this.joint_id = 0;
		this.type = AccountType.CHECKING;
		this.balance = new Money();
		this.status = null;
	}

	// Parse Data from String[] (from SQL Query)
	public Account(String[] accData) {
		this.id = accData[0];
		this.owner_id = Integer.parseInt(accData[1]);
		this.joint_id = (accData[2] != null ? Integer.parseInt(accData[2]) : 0);
		this.type = AccountType.valueOf(accData[3]);
		this.balance = new Money(accData[4]);
		if (accData[5] == null) this.status = null;
		else if(accData[5].equals("f")) this.status = false;
		else if(accData[5].equals("t")) this.status = true;
	}

	// Full Constructor
	public Account(String id, int owner_id, int joint_id, AccountType type, Money balance, Boolean status) {
		this.id = id;
		this.owner_id = owner_id;
		this.joint_id = joint_id;
		this.type = type;
		this.balance = balance;
		if (status.equals("t")) this.status = true;
		else if(status.equals("f")) this.status = false;
		else this.status = null;
	}

	// Full Constructor with 8 individual String inputs
	public Account(String id, String owner_id, String joint_id, String type, String balance, String status) {
		this.id = id;
		this.owner_id = Integer.parseInt(owner_id);
		this.joint_id = (joint_id !=null ? Integer.parseInt(joint_id) : 0);
		this.type = AccountType.valueOf(type);
		this.balance = new Money(balance);
		if (status == null) this.status = null;
		else if(status.equals("t")) this.status = true;
		else if(status.equals("f")) this.status = false;
	}

	// Getters and Setters
	String getAccountId() {
		return this.id;
	}
	void setAccountId(String id) {
		this.id = id;
	}
	
	String getOwnerId() {
		return Integer.toString(owner_id);
	}

	void setOwnerId(String id) {
		this.owner_id = Integer.parseInt(id);
		System.out.println("Setting Owner to ID #"+id);
	}
	
	int getAccountIdAsInt() {
		return Integer.parseInt(this.id);
	}
	
	String getJointId() {
		return Integer.toString(this.joint_id);
	}

	void setJointId(String id) {
		this.joint_id = Integer.parseInt(id);
		System.out.println("Setting Joint owner to ID #"+id);
	}
	
	void setType(String type) {
		this.type = AccountType.valueOf(type);
		System.out.println("Setting account type to "+type);
	}
	
	public String getBalance() {
		return this.balance.toString();
	}
	
	public String getStatus() {
		if (this.status == null) return "Pending";
		else if (this.status) return "Open";
		else return "Closed";
	}
	
	public Boolean isOpen() {
		if (this.status == null) return false;	//NullPointerException if this is not checked first
		else if (this.status) return true;
		return false;
	}
	
	public void approveAccount() {
		this.status = true;
		System.out.println("Account #"+this.id+" has been opened.");
	}
	
	public void denyAccount() {
		this.status = false;
		System.out.println("Account #"+this.id+" has been closed.");
	}
	
	public static Boolean checkIdInList(String inputId, ArrayList<Account> AccList) {
		for (Account a : AccList) {
			if (inputId.equals(a.getAccountId()))
				return true;
		}
		return false;
	}
	
	public static Boolean checkOpenIdInList(String inputId, ArrayList<Account> AccList) {
		for (Account a : AccList) {
			// Check if Account is open
			if (inputId.equals(a.getAccountId()) && a.isOpen())
				return true;
		}
		return false;
	}
	
	public static Account pullAccountFromId(String inputId, ArrayList<Account> custAccList) {
		for(Account acc : custAccList) {
			if (acc.getAccountId().equals(inputId))
				return acc;
		}
		return null;
	}
	
	public static Account createNewAccount(Customer currCust, CustomerDAO custDAO) {
		Scanner sc = new Scanner(System.in);
		String userInput;
		Person jointUser;

		String[] accData = new String[6];
		
		// Save passed username and password
		accData[0] = Integer.toString(++AccountDAO.maxID);
		accData[1] = currCust.getId();

		// Ask for joint status
		System.out.println("Will this be a joint account? 'Y' or 'N'");
		userInput = sc.nextLine();
		while (!(userInput.contains("Y") || userInput.contains("N"))) {
			System.out.println("Invalid entry. Will this be a joint account? 'Y' or 'N'");
			userInput = sc.nextLine();
		}
		
		// User is adding a joint user to the account
		if (userInput.contains("Y")) {
			// Search joint user by username
			System.out.print("Please enter the username of the joint account holder: ");
			String joint_username = sc.nextLine();
			while ((jointUser = custDAO.fetchCustomerByUsername(joint_username)) == null
					|| joint_username.equals(currCust.getUsername())) {
				System.out.println(
						"Unable to locate user.\n" + "Please re-enter the username of the joint account holder: ");
				joint_username = sc.nextLine();
			}
			// Add secondary user's ID to data
			accData[2] = jointUser.getId();
		} 
		// Otherwise set Joint_id to 0
		else accData[2]=null;
		
		// Get account type from user
		System.out.println("Which type of Account would you like to create? (Checking or Savings)");
		userInput=sc.nextLine().toUpperCase();
		while(!(userInput.equals("CHECKING")|| userInput.equals("SAVINGS"))) {
			System.out.println("Unknown type. Please enter \'Checking\' or \'Savings\'");
			userInput = sc.nextLine().toLowerCase();
		}
		accData[3] = userInput;
		
		// Set initial amount 
		System.out.println("Enter the amount (without commas) you would like to place into the account: ");
		userInput = (Money.getValidMoney()).toString();
		accData[4] = userInput;
		
		// Alert user that account is currently pending
		System.out.println("The account is currently pending... Requires employee approval before use");
		accData[5] = null;
		
		return new Account(accData);
	}
	
	public void editAccountBalance(Person currUser, ArrayList<Account> custAccList, AccountDAO accDAO) {
		Scanner scanIn = new Scanner(System.in);
		// Give options : deposit, withdraw, transfer
		System.out.println("What action would you like to perform on the Account (deposit, withdraw, or transfer): ");
		String userOption = scanIn.nextLine();

		// Repeat through options, allow quit
		while (!((userOption.equals("exit")) ||  
				(userOption.equals("deposit")) ||
				(userOption.equals("withdraw")) ||
				(userOption.equals("transfer")))) {
			System.out.println("Unrecognized input. Please enter \'deposit\', \'withdraw\', \'transfer\', or \'exit\'");
			userOption = scanIn.nextLine();
		}
							
		if (userOption.equals("exit")) {
			System.out.println("Exiting...");
			System.exit(0);
		} else if(userOption.equals("deposit")) {
			this.deposit(currUser);
			accDAO.updateAccount(this);
		} else if(userOption.equals("withdraw")) {
			this.withdraw(currUser);
			accDAO.updateAccount(this);
		}
		else if(userOption.equals("transfer")) {
			Account otherAcc = this.transfer(currUser, custAccList);
			accDAO.updateAccount(this);
			accDAO.updateAccount(otherAcc);
		}
	}

	public void withdraw(Person currUser) {
		System.out.print("Enter the amount you like to withdraw from the account: ");
		Money withdrawAmount = Money.getValidMoney();
		this.balance.subtract(withdrawAmount);
		System.out.println("Sucessfully withdrew "+withdrawAmount+" from Account #"+this.id);
		this.displayAccount();
		TransactionDAO.logWithdraw(currUser, withdrawAmount, this);
	}

	public void deposit(Person currUser) {
		System.out.print("Enter the amount you like to deposit into the account: ");
		Money depositAmount = Money.getValidMoney();
		this.balance.add(depositAmount);
		System.out.println("Successfully deposited "+depositAmount+" into Account #"+this.id);
		this.displayAccount();

		TransactionDAO.logDeposit(currUser, depositAmount, this);
	}

	// Transfers from source to target in amount of TransferAmount
	public Account transfer(Person currUser, ArrayList<Account> custAccList){
		Scanner sc = new Scanner(System.in);
		System.out.println("Would you like to transfer TO this account or FROM this account: ");
		String transferDirection = sc.nextLine().toUpperCase();
		while (!transferDirection.matches("FROM|TO")) {
			System.out.println("ERROR: Unrecognized input. Please enter \'TO\' or \'FROM\'");
			transferDirection = sc.nextLine().toUpperCase();
		}
		
		//Display Open Accounts
		ArrayList<Account> openAccList = Customer.displayOpenAccounts(custAccList);
		
		System.out.println("Enter the account that you would like to transfer with: ");
		String otherAccId = sc.nextLine();
		if (!Account.checkOpenIdInList(otherAccId, openAccList) || otherAccId.equals(this.id)) {
			System.out.print("ERROR: Unrecognized input. Please enter a valid account from the available accounts: ");
			otherAccId = sc.nextLine();
		}
		
		// Fetch second account by ID
		Account otherAcc = Account.pullAccountFromId(otherAccId, custAccList);
		
		System.out.print("Enter the amount that you would like to transfer: ");
		Money transferAmount = Money.getValidMoney();
		
		if (transferDirection.equals("FROM")) {
			this.balance.subtract(transferAmount);
			otherAcc.balance.add(transferAmount);	
			System.out.println("Successfully transferred "+transferAmount +" from Account #" +
					this.id + " to Account #"+otherAcc.id);
		} else if(transferDirection.equals("TO")) {
			this.balance.add(transferAmount);
			otherAcc.balance.subtract(transferAmount);
			System.out.println("Successfully transferred "+transferAmount +" to Account #" + 
					this.id + " from Account #"+otherAcc.id);
		}
		this.displayAccount();
		otherAcc.displayAccount();
		
		TransactionDAO.logTransfer(currUser, transferAmount, this, otherAcc);
		return otherAcc;
	}
	
	public void displayAccount() {
		System.out.println("ID\tOwnerID\tJointID\tType\tStatus\tBalance");
		System.out.println(this.toString());
	}
	
	public static void displayAccounts(ArrayList<Account> accounts) {
		System.out.println("ID\tOwnerID\tJointID\tType\tStatus\tBalance");
		for (Account acc : accounts) {
			System.out.println(acc.toString());
		}
	}
	
	public static ArrayList<Account> getPendingAcc(ArrayList<Account> accounts){
		ArrayList<Account> pendingAccList = new ArrayList<Account>();
		for (Account acc : accounts) {
			if (acc.getStatus().equals("Pending"))
				pendingAccList.add(acc);
		}
		return pendingAccList;
	}
	
	public static Boolean isAccPending(ArrayList<Account> accounts) {
		for (Account acc : accounts) {
			if (acc.getStatus() == null)
				return true;
		}
		return false;
	}
	
	public String toSqlString() {
		return "("+this.id+","+this.owner_id+","+this.joint_id+",\'"+this.type+"\',"+this.balance+","+this.status+")";
	}
	
	@Override
	public String toString() {
		return this.id + "\t" + owner_id + "\t" + ((joint_id != 0) ? joint_id : "No") + "\t" 
				+ (this.type == AccountType.CHECKING ? "CHECK" : "SAVINGS") 
				+ "\t" +this.getStatus() + "\t"
				+ this.balance;
	}
}
