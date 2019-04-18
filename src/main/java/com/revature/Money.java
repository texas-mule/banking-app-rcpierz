package com.revature;

public class Money {
	//Declare fields
	private int dollars;
	private int cents;
	
	//Constructor Methods
	//Parameterless Constructor
	Money(){
		this.dollars = 0;
		this.cents = 0;
	}
	//2-Parameter Constructor
	Money(int dollars, int cents){
		this.dollars = dollars;
		this.cents = cents;
	}
	
	//Mutator/Setters
	void setDollars(int dollars) {
		this.dollars = dollars;
	}
	void setCents(int cents) {
		this.cents = cents;
	}
	
	//Accessors/Getters
	int getDollars() {
		return this.dollars;
	}
	int getCents() {
		return this.cents;
	}
	
	void subtract(Money other) {
		if (((other.dollars >= this.dollars) && (other.cents > this.cents)) || (other.dollars > this.dollars)) {
			//THROW EXCEPTION
			System.out.println(((other.dollars < this.dollars) && (other.cents > this.cents)) );
			System.out.println(other.dollars>this.dollars);
			System.out.println("Case 1");
		}
		else if(other.dollars < this.dollars && other.cents >= this.cents) {
			System.out.println("Case 2");
			this.dollars = this.dollars - other.dollars - 1 ;
			this.cents = 100-(other.cents - this.cents);
		}
		else if(other.dollars <= this.dollars && other.cents <= this.cents) {
			System.out.println("Case 3");
			this.dollars -= other.dollars;
			this.cents -= other.cents;
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
	
	public String toString() {
		return this.dollars+"."+ (this.cents < 10 ? "0":"") + this.cents;
	}
}
