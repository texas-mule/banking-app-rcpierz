package com.revature.rcpierzbankingapp.domain;

import java.util.Scanner;

public class Money {
	// Declare fields
	private int dollars;
	private int cents;
	public String account_id;

	// Constructor Methods
	// Parameterless Constructor
	Money() {
		this.dollars = 0;
		this.cents = 0;
	}

	// Input as String of type (XX.YY)
	Money(String input) {
		input = input.replace("$","");
		input = input.replaceAll(",","");
		if (input.split("\\.").length == 1) {
			this.dollars = Integer.parseInt(input);
			this.cents = 0;
		} else if (input.split("\\.").length == 2) {
			this.dollars = Integer.parseInt(input.split("\\.")[0]);
			this.cents = Integer.parseInt(input.split("\\.")[1]);
		} else
			System.out.println("Unable to parse Money string.");
	}

	// 2-Parameter Constructor
	Money(int dollars, int cents) {
		this.dollars = dollars;
		this.cents = cents;
	}

	// 3-Parameter Constructor
	Money(int dollars, int cents, String account_id) {
		this.dollars = dollars;
		this.cents = cents;
		this.account_id = account_id;
	}
	
	void copyMoney(Money other) {
		this.dollars = other.dollars;
		this.cents = other.cents;
	}

	// Mutator/Setters
	void setDollars(int dollars) {
		this.dollars = dollars;
	}

	void setCents(int cents) {
		this.cents = cents;
	}

	// Accessors/Getters
	int getDollars() {
		return this.dollars;
	}

	int getCents() {
		return this.cents;
	}

	int compare(Money other) {
		return (this.dollars - other.dollars);
	}
	
	public static Money getValidMoney() {
		Scanner sc = new Scanner(System.in);
		String userInput = sc.nextLine();
		
		//Check that user's input matches form of ($)XX(.YY)
		while (!userInput.matches("^[$]?\\d+(\\.[0-9]{2})?")){
			System.out.print("ERROR: Unrecognized input. Please input a valid amount: ");
			userInput = sc.nextLine();
		}
		return new Money(userInput);
	}

	void subtract(Money other) {
		while ((this.compare(other) < 0) || (this.compare(other) == 0 && (other.cents > this.cents))) {
			System.out.print("ERROR: Insufficient Funds. Please enter an amount greater than "+this.toString()+": ");
			this.copyMoney(Money.getValidMoney());
		} 
		if (this.compare(other) >= 0) {
			if ((this.compare(other) > 0) && (this.cents < other.cents)) {
				this.dollars = this.dollars - other.dollars - 1;
				this.cents = 100 - (other.cents - this.cents);
			} else {
				this.dollars = this.dollars - other.dollars;
				this.cents = this.cents - other.cents;
			}
		}
	}

	void add(Money other) {
		this.dollars += other.dollars;
		this.cents += other.cents;
		if (this.cents > 100) {
			this.dollars++;
			this.cents -= 100;
		}
	}

	@Override
	public String toString() {
		return this.dollars + "." + (this.cents < 10 ? "0" : "") + this.cents;
	}
}
