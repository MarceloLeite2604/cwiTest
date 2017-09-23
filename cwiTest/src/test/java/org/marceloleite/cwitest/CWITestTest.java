package org.marceloleite.cwitest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.Random;

import org.junit.Test;
import org.marceloleite.cwitest.CWITest;

/**
 * Test cases created to check the {@link CWITest} class.
 * 
 * @author Marcelo Leite
 *
 */
public class CWITestTest {

	/**
	 * Tests the {@link CWITest#currencyQuotation} method based on the example shown
	 * on test description.
	 */
	@Test
	public void testCurrencyQuotationExample() {
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

	/**
	 * Tests if the {@link CWITest#currencyQuotation} result is rounded with two
	 * decimal places.
	 */
	@Test
	public void testCurrencyQuotationResultWithTwoDecimalPlaces() {
		/* Max value used to avoid an exponential representation of the result. */
		final double maxValue = 100000;
		final String twoDecimalsRegex = "^\\d+\\.\\d{2}$";

		BigDecimal returnedValue = null;
		Number randomAmount = ((new Random().nextDouble()) * maxValue);
		try {
			returnedValue = new CWITest().currencyQuotation("USD", "EUR", randomAmount, "20/11/2014");
		} catch (IOException ioException) {
			fail("IOException thrown: " + ioException.getMessage());
		}
		String returnedValueString = returnedValue.toString();
		assertTrue(returnedValueString.matches(twoDecimalsRegex));
	}

	/**
	 * Tests if the execution of the {@link CWITest#currencyQuotation} method with
	 * an empty value on "from" parameter throws an
	 * {@link InvalidParameterException}.
	 */
	@Test(expected = InvalidParameterException.class)
	public void testCurrencyQuotationEmptyFromInvalidParameterException() {
		try {
			new CWITest().currencyQuotation("", "EUR", 100.00, "20/09/2017");
		} catch (IOException ioException) {
			fail("IOException thrown: " + ioException.getMessage());
		}
		fail("An InvalidParameterException must be thrown.");
	}

	/**
	 * Tests if the execution of the {@link CWITest#currencyQuotation} method with a
	 * null value on "from" parameter throws an {@link InvalidParameterException}.
	 */
	@Test(expected = InvalidParameterException.class)
	public void testCurrencyQuotationNullFromInvalidParameterException() {
		try {
			new CWITest().currencyQuotation(null, "EUR", 100.00, "20/09/2017");
		} catch (IOException ioException) {
			fail("IOException thrown: " + ioException.getMessage());
		}
		fail("An InvalidParameterException must be thrown.");
	}

	/**
	 * Tests if the execution of the {@link CWITest#currencyQuotation} method with
	 * an invalid value on "from" parameter throws an {@link RuntimeException}.
	 */
	@Test(expected = RuntimeException.class)
	public void testCurrencyQuotationInvalidFromRuntimeException() {
		try {
			new CWITest().currencyQuotation("???", "EUR", 100.00, "20/09/2017");
		} catch (IOException ioException) {
			fail("IOException thrown: " + ioException.getMessage());
		}
		fail("An RuntimeException must be thrown.");
	}

	/**
	 * Tests if the execution of the {@link CWITest#currencyQuotation} method with
	 * an empty value on "to" parameter throws an {@link InvalidParameterException}.
	 */
	@Test(expected = InvalidParameterException.class)
	public void testCurrencyQuotationEmptyToInvalidParameterException() {
		try {
			new CWITest().currencyQuotation("USD", "", 100.00, "20/09/2017");
		} catch (IOException ioException) {
			fail("IOException thrown: " + ioException.getMessage());
		}
		fail("An InvalidParameterException must be thrown.");
	}

	/**
	 * Tests if the execution of the {@link CWITest#currencyQuotation} method with a
	 * null value on "to" parameter throws an {@link InvalidParameterException}.
	 */
	@Test(expected = InvalidParameterException.class)
	public void testCurrencyQuotationNullToInvalidParameterException() {
		try {
			new CWITest().currencyQuotation("USD", null, 100.00, "20/09/2017");
		} catch (IOException ioException) {
			fail("IOException thrown: " + ioException.getMessage());
		}
		fail("An InvalidParameterException must be thrown.");
	}

	/**
	 * Tests if the execution of the {@link CWITest#currencyQuotation} method with
	 * an invalid value on "to" parameter throws an {@link RuntimeException}.
	 */
	@Test(expected = RuntimeException.class)
	public void testCurrencyQuotationInvalidToRuntimeException() {
		try {
			new CWITest().currencyQuotation("USD", ":::", 100.00, "20/09/2017");
		} catch (IOException ioException) {
			fail("IOException thrown: " + ioException.getMessage());
		}
		fail("An RuntimeException must be thrown.");
	}

	/**
	 * Tests if the execution of the {@link CWITest#currencyQuotation} method
	 * passing a value smaller than zero on "value" parameter throws an
	 * {@link InvalidParameterException}. exception.
	 */
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

	/**
	 * Tests if the execution of the {@link CWITest#currencyQuotation} method
	 * passing a null value on "quotation" parameter throws an
	 * {@link InvalidParameterException}.
	 */
	@Test(expected = InvalidParameterException.class)
	public void testCurrencyQuotationNullQuotationDateInvalidParameterException() {
		try {
			new CWITest().currencyQuotation("USD", "EUR", 100.00, null);
		} catch (IOException ioException) {
			fail("IOException thrown: " + ioException.getMessage());
		}
		fail("An InvalidParameterException must be thrown.");
	}

	/**
	 * Tests if the execution of the{@link CWITest#currencyQuotation} method passing
	 * an invalid date format on "quotation" parameter throws an
	 * {@link InvalidParameterException}. exception.
	 */
	@Test(expected = InvalidParameterException.class)
	public void testCurrencyQuotationInvalidQuotationDateFormatInvalidParameterException() {
		try {
			new CWITest().currencyQuotation("USD", "EUR", 100.00, "20-SEP-2017");
		} catch (IOException ioException) {
			fail("IOException thrown: " + ioException.getMessage());
		}
		fail("An InvalidParameterException must be thrown.");
	}

	/**
	 * Tests if the execution of the "currencyQuotation" method passing an
	 * unavailable quotation date on "quotation" parameter throws an
	 * {@link IOException}.
	 */
	@Test(expected = IOException.class)
	public void testCurrencyQuotationQuotationDateNotAvailableIOException() throws IOException {
		new CWITest().currencyQuotation("USD", "EUR", 100.00, "01/02/1800");
	}
}
