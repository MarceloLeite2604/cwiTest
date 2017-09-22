package org.marceloleite.cwitest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.security.InvalidParameterException;

import org.junit.Test;
import org.marceloleite.cwitest.CWITest;

public class CWITestTest {
	
	@Test(expected = InvalidParameterException.class)
	public void testCurrencyQuotationEmptyFromInvalidParameter() {
		try {
			new CWITest().currencyQuotation("", "EUR", 100.00, "20/09/2017");
		} catch (IOException ioException) {
			fail("IOException thrown: " + ioException.getMessage());
		}
		fail("An InvalidParameterException must be thrown.");
	}

	@Test(expected = InvalidParameterException.class)
	public void testCurrencyQuotationNullFromInvalidParameter() {
		try {
			new CWITest().currencyQuotation(null, "EUR", 100.00, "20/09/2017");
		} catch (IOException ioException) {
			fail("IOException thrown: " + ioException.getMessage());
		}
		fail("An InvalidParameterException must be thrown.");
	}
	
	@Test(expected = InvalidParameterException.class)
	public void testCurrencyQuotationEmptyToInvalidParameter() {
		try {
			new CWITest().currencyQuotation("USD", "", 100.00, "20/09/2017");
		} catch (IOException ioException) {
			fail("IOException thrown: " + ioException.getMessage());
		}
		fail("An InvalidParameterException must be thrown.");
	}

	@Test(expected = InvalidParameterException.class)
	public void testCurrencyQuotationNullToInvalidParameter() {
		try {
			new CWITest().currencyQuotation("USD", null, 100.00, "20/09/2017");
		} catch (IOException ioException) {
			fail("IOException thrown: " + ioException.getMessage());
		}
		fail("An InvalidParameterException must be thrown.");
	}
	
	@Test(expected = InvalidParameterException.class)
	public void testCurrencyQuotationValueSmallerThanZeroInvalidParameter() {
		try {
			new CWITest().currencyQuotation("USD", "EUR", -2, "20/09/2017");
		} catch (IOException ioException) {
			fail("IOException thrown: " + ioException.getMessage());
		}
		fail("An InvalidParameterException must be thrown.");
	}

}
