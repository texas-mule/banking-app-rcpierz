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
		this.balance = new Money();
	}

	String getAccountId() {
		return this.id;
	}

	void setAccountId(String id) {
		this.id = id;
	}

	int getAccountIdAsInt() {
		return Integer.parseInt(this.id);
	}

	public static int getHighestId(ArrayList<Account> accounts) {
		int highest = -1;
		for (Account a : accounts) {
			if (a.getAccountIdAsInt() > highest)
				highest = a.getAccountIdAsInt();
		}
		return highest;
	}

	public Account(AccountType type) {
		this.type = type;
		this.balance = new Money();
		this.joint = false;
	}

	// Full Constructor
	public Account(String id, Boolean joint, AccountType type, Money balance) {
		this.id = id;
		this.type = type;
		this.balance = balance;
		this.joint = joint;
	}

	public static Account createNewAccount(ArrayList<Person> personnel, ArrayList<Account> accounts) {
		Account newAccount = new Account();
		Scanner sc = new Scanner(System.in);
		String userInput;
		Person jointUser;

		// Ask for Joint status
		System.out.println("Will this account be a Joint account?");
		userInput = sc.nextLine();
		while (!((userInput.contains("y")) || userInput.contains("n"))) {
			System.out.println("Unable to parse. Please enter \'yes\' or \'no\'");
			userInput = sc.nextLine();
		}

		// Connect Account with Joint User
		if (userInput.contains("y")) {
			newAccount.joint = true;
			System.out.print("Please enter the username for the associated partner: ");
			while ((jointUser = Person.findUserInDatabase(sc.nextLine(), personnel)) == null) {
				System.out.print("Unable to locate user. Please re-enter partner's username: ");
			}
			System.out.print("Please enter the password for user " + jointUser.getUsername() + ": ");
			while (jointUser.checkPassword(sc.nextLine()) != true)
				System.out.println("Incorrect password entered. Please re-enter");
			System.out.println("Successfully connected account with user:" + jointUser.getUsername());
			jointUser.addAccess(newAccount.getAccountId());
		}
		if (userInput.contains("n"))
			newAccount.joint = false;

		// Checking or Savings
		System.out.println("Which type of Account would you like to create? (Checking or Savings)");
		userInput = sc.nextLine().toLowerCase();
		while (!((userInput.equals("checking")) || (userInput.equals("savings")))) {
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
		while (userInput.split("\\.").length > 2) {
			System.out.println(
					"Unable to parse input amount. Please re-enter the desired amount to place in the account: ");
			userInput = sc.nextLine();
		}
		newAccount.deposit(new Money(userInput));
		newAccount.setAccountId(Integer.toString(Account.getHighestId(accounts) + 1));

		System.out.println("Created new account: " + newAccount.toString());
		AccountLoader.saveAccount(newAccount);
		return newAccount;
	}

	public Account loadAccount(String id, ArrayList<Account> accounts) {
		for (Account a : accounts) {
			if (id.contentEquals(a.getAccountId())) return a;
		}
		System.out.println("Unable to locate target account. Generating blank account.");
		return new Account();
	}

	public void withdraw(Money withdrawAmount) throws InsufficientFundsException {
		this.balance.subtract(withdrawAmount);
	}

	public void deposit(Money depositAmount) {
		this.balance.add(depositAmount);
	}

	// Transfers from source to target in amount of TransferAmount
	static void transfer(Account source, Account target, Money transferAmount) throws InsufficientFundsException {
		target.balance.add(transferAmount);
		source.balance.subtract(transferAmount);
	}

	public String writeToText() {
		return this.id + ";" + this.joint + ";" + this.type + ";" + this.balance;
	}

	public static Account findAccountById(String id, ArrayList<Account> accounts) {
		for (Account a : accounts) {
			if (a.getAccountId().equals(id)) {
				return a;
			}
		}
		System.out.println("Unable to locate account.");
		// TODO rework else case
		return new Account();
	}

	@Override
	public String toString() {
		return "Account ID: " + this.id + (this.joint ? ", Joint " : ", Non-Joint ") + this.type + ", Balance: $"
				+ this.balance;
	}

}
