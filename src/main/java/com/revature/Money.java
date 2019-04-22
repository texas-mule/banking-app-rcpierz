package com.revature;

public class Money {
	// Declare fields
	private int dollars;
	private int cents;
	public String account_id;

	public class InsufficientFundsException extends Exception {
		public InsufficientFundsException(Money a, Money b) {
			System.out.println("Attempting to withdraw $" + b + " from $" + a + ". Insufficient funds available.");
		}
	}

	// Constructor Methods
	// Parameterless Constructor
	Money() {
		this.dollars = 0;
		this.cents = 0;
	}

	// Input as String of type (XX.YY)
	Money(String input) {
		this.dollars = Integer.parseInt(input.split("\\.")[0]);
		this.cents = Integer.parseInt(input.split("\\.")[1]);
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

	void subtract(Money other) throws InsufficientFundsException {
		System.out.println("Attempting to subtract " + other + " from " + this);
		if ((this.compare(other) < 0) || (this.compare(other) == 0 && (other.cents > this.cents))) {
			throw new InsufficientFundsException(this, other);
		} else if (this.compare(other) >= 0) {
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
