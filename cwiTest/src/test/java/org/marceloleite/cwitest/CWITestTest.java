package org.marceloleite.cwitest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.Random;

import org.junit.Test;
import org.marceloleite.cwitest.CWITest;

public class CWITestTest {

	@Test
	public void testCurrencyQuotationExampleSuccess() {
		BigDecimal[] expected = new BigDecimal[1];
		expected[0] = new BigDecimal("79.69");
		BigDecimal[] results = new BigDecimal[1];
		try {
			results[0] = new CWITest().currencyQuotation("USD", "EUR", 100.00, "20/11/2014");
		} catch (IOException ioException) {
			fail("IOException thrown: " + ioException.getMessage());
		}
		assertArrayEquals(expected, results);
	}

	@Test
	public void testCurrencyQuotationResultWithTwoDecimalPlacesSuccess() {
		/* Max value used to avoid an exponential representation of the result. */
		final double MAX_VALUE = 100000;
		
		BigDecimal returnedValue = null;
		Number randomAmount = ((new Random().nextDouble() ) * MAX_VALUE);
		try {
			returnedValue = new CWITest().currencyQuotation("USD", "EUR", randomAmount, "20/11/2014");
		} catch (IOException ioException) {
			fail("IOException thrown: " + ioException.getMessage());
		}
		String returnedValueString = returnedValue.toString();
		assertTrue(returnedValueString.matches("^\\d+\\.\\d{2}$"));

	}

	@Test(expected = InvalidParameterException.class)
	public void testCurrencyQuotationEmptyFromInvalidParameterException() {
		try {
			new CWITest().currencyQuotation("", "EUR", 100.00, "20/09/2017");
		} catch (IOException ioException) {
			fail("IOException thrown: " + ioException.getMessage());
		}
		fail("An InvalidParameterException must be thrown.");
	}

	@Test(expected = InvalidParameterException.class)
	public void testCurrencyQuotationNullFromInvalidParameterException() {
		try {
			new CWITest().currencyQuotation(null, "EUR", 100.00, "20/09/2017");
		} catch (IOException ioException) {
			fail("IOException thrown: " + ioException.getMessage());
		}
		fail("An InvalidParameterException must be thrown.");
	}

	@Test(expected = InvalidParameterException.class)
	public void testCurrencyQuotationEmptyToInvalidParameterException() {
		try {
			new CWITest().currencyQuotation("USD", "", 100.00, "20/09/2017");
		} catch (IOException ioException) {
			fail("IOException thrown: " + ioException.getMessage());
		}
		fail("An InvalidParameterException must be thrown.");
	}

	@Test(expected = InvalidParameterException.class)
	public void testCurrencyQuotationNullToInvalidParameterException() {
		try {
			new CWITest().currencyQuotation("USD", null, 100.00, "20/09/2017");
		} catch (IOException ioException) {
			fail("IOException thrown: " + ioException.getMessage());
		}
		fail("An InvalidParameterException must be thrown.");
	}

	@Test(expected = InvalidParameterException.class)
	public void testCurrencyQuotationValueSmallerThanZeroInvalidParameterException() {
		Number randomNegativeValue = ((new Random().nextDouble() - 1) * Double.MAX_VALUE);
		try {
			new CWITest().currencyQuotation("USD", "EUR", randomNegativeValue, "20/09/2017");
		} catch (IOException ioException) {
			fail("IOException thrown: " + ioException.getMessage());
		}
		fail("An InvalidParameterException must be thrown.");
	}

	@Test(expected = InvalidParameterException.class)
	public void testCurrencyQuotationInvalidQuotationDateNullInvalidParameterException() {
		try {
			new CWITest().currencyQuotation("USD", "EUR", 100.00, null);
		} catch (IOException ioException) {
			fail("IOException thrown: " + ioException.getMessage());
		}
		fail("An InvalidParameterException must be thrown.");
	}

	@Test(expected = InvalidParameterException.class)
	public void testCurrencyQuotationInvalidQuotationDateFormatInvalidParameterException() {
		try {
			new CWITest().currencyQuotation("USD", "EUR", 100.00, "20-SEP-2017");
		} catch (IOException ioException) {
			fail("IOException thrown: " + ioException.getMessage());
		}
		fail("An InvalidParameterException must be thrown.");
	}

	@Test(expected = IOException.class)
	public void testCurrencyQuotationQuotationDateNotAvailableIOException() throws IOException {
		new CWITest().currencyQuotation("USD", "EUR", 100.00, "01/01/1800");
	}
}
