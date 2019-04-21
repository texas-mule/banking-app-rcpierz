package com.revature;

import com.revature.Money.InsufficientFundsException;

public class Account {
	private Money balance;
	public Boolean joint;
	public String id;
	AccountType type;
	
	public Account() {
		this.type = AccountType.CHECKING;
		this.joint = false;
	}
	
	public Account(AccountType type) {
		this.type = type;
		this.balance = new Money();
		this.joint = false;
	}
	
	public Account(AccountType type, Money balance, Boolean joint) {
		this.type = type;
		this.balance = balance;
		this.joint = joint;
	}
	
	public void withdraw(Money withdrawAmount) throws InsufficientFundsException {
		this.balance.subtract(withdrawAmount);
	}
	
	public void deposit(Money depositAmount) {
		this.balance.add(depositAmount);
	}
	
	
}

