package com.revature;

import java.util.ArrayList;
import java.util.Scanner;

import com.revature.Money.InsufficientFundsException;

public class Account {
	private Money balance;
	public Boolean joint;
	public String id;
	AccountType type;
	
	public Account() {
		this.id = "-1";
		this.type = AccountType.CHECKING;
		this.joint = false;
	}
	
	String getAccountId() {
		return this.id;
	}
	
	public Account(AccountType type) {
		this.type = type;
		this.balance = new Money();
		this.joint = false;
	}
	// Full Constructor
	public Account(AccountType type, Money balance, Boolean joint) {
		this.type = type;
		this.balance = balance;
		this.joint = joint;
	}
	
	public static Account createNewAccount(ArrayList<Person> personnel) {
		Account newAccount = new Account();
		Scanner sc = new Scanner(System.in);
		String userInput;
		Person jointUser;
		
		//Ask for Joint status
		System.out.println("Will this account be a Joint account?");
		while (!((userInput = sc.nextLine()).contains("y")) || userInput.contains("n")) {
			System.out.println("Unable to parse. Please enter \'yes\' or \'no\'");
		}
		
		//Connect Account with Joint User
		if (userInput.contains("y")) {
			newAccount.joint = true; 
			System.out.print("Please enter the username for the associated partner: ");
			while ((jointUser = Person.findUserInDatabase(sc.nextLine(), personnel)) != null) {
				System.out.print("Unable to locate user. Please re-enter partner's username: ");
			}
			System.out.print("Please enter the password for user "+jointUser.getUsername()+": ");
			while (jointUser.checkPassword(sc.nextLine()) != true)
				System.out.println("Incorrect password entered. Please re-enter");
			System.out.println("Successfully connected account with user:"+jointUser.getUsername());
			jointUser.addAccess(newAccount.getAccountId());
		}
		if (userInput.contains("n")) newAccount.joint = false;
		
		// Checking or Savings
		System.out.println("Which type of Account would you like to create? (Checking or Savings)");
		userInput = sc.nextLine().toLowerCase();
		while(!(userInput.equals("checking")) || !(userInput.equals("savings"))) {
			System.out.println("Unknown type. Please enter \'Checking\' or \'Savings\'");
			userInput = sc.nextLine().toLowerCase();
		}
		if (userInput.equals("checking"))
			newAccount.type = AccountType.CHECKING;
		else if (userInput.equals("savings"))
			newAccount.type = AccountType.SAVINGS;
		
		// Deposit initial amount
		System.out.println("How much money would you like to place in the account?");
		userInput = sc.nextLine();
		while (userInput.split("\\.").length != 2) {
			System.out.println("Unable to parse input amount. Please re-enter the desired amount to place in the account: ");
			userInput = sc.nextLine();
		} 
		newAccount.deposit(new Money(userInput));
		
		
		return newAccount;
	}
	
	public void withdraw(Money withdrawAmount) throws InsufficientFundsException {
		this.balance.subtract(withdrawAmount);
	}
	
	public void deposit(Money depositAmount) {
		this.balance.add(depositAmount);
	}
	
	// Transfers from source to target in amount of TransferAmount
	static void transfer(Account source, Account target, Money transferAmount) throws InsufficientFundsException{
		target.balance.add(transferAmount);
		source.balance.subtract(transferAmount);
	}
	
	
	
	
}

