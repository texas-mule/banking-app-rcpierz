package com.revature;

import static org.junit.Assert.*;

import org.junit.Test;

import com.revature.Money.InsufficientFundsException;

public class MoneyTest {

	@Test
	public void testGreaterDollarsLessCents() {
		Money a = new Money(12, 34);
		Money b = new Money(3, 12);
		try {
			a.subtract(b);
		} catch (InsufficientFundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(a.toString(), "9.22");
	}

	@Test
	public void testGreaterDollarsGreaterCents() {
		Money a = new Money(12, 34);
		Money b = new Money(34, 56);
		try {
			a.subtract(b);
			fail();
		} catch (InsufficientFundsException e) {
			assertNotNull(e);
		}
	}

	@Test
	public void testLessDollarsLessCents() {
		Money a = new Money(12, 34);
		Money b = new Money(3, 56);
		try {
			a.subtract(b);
		} catch (InsufficientFundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(a.toString(), "8.78");
	}

}
