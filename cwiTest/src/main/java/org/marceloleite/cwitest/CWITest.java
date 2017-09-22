package org.marceloleite.cwitest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidParameterException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * This is the class I created to answer the CWI test.
 * 
 * @author Marcelo Leite
 *
 */
public class CWITest {

	/**
	 * The base URL used to retrieve the Brazilian central bank quotation CSV file.
	 */
	private static final String BCB_CSV_BASE_URL = "http://www4.bcb.gov.br/Download/fechamento/";

	/**
	 * The extension used to identify a CSV file.
	 */
	private static final String CSV_EXTENSION = ".csv";

	/**
	 * Size of the buffer used to read the CSV file.
	 */
	private static final int FILE_READING_BUFFER_SIZE = 1024;

	/**
	 * The character used to separate values on the CSV file.
	 */
	private static final String CSV_SEPARATION_CHARACTER = ";";

	/**
	 * Entry method used for testing purposes.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		BigDecimal returnValue = null;

		CWITest cwiTest = new CWITest();
		try {
			returnValue = cwiTest.currencyQuotation("USD", "EUR", 100.00, "20/11/2014");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(returnValue);
	}

	/**
	 * Returns the value of a currency amount converted to another currency based on
	 * Brazilian central bank quotation.
	 * 
	 * @param from
	 *            The quotation base currency.
	 * @param to
	 *            The quotation quote currency.
	 * @param value
	 *            The amount of base currency to convert.
	 * @param quotation
	 *            The quotation date on "dd/MM/yyyy" format.
	 * @return TODO: Once the function is done, write this.
	 * @throws IOException
	 *             When the method cannot retrieve Brazilian central bank quotation
	 *             CSV file.
	 */
	public BigDecimal currencyQuotation(String from, String to, Number value, String quotation) throws IOException {

		/* Check parameters. */
		if (null == from || from.length() != 3) {
			throw new InvalidParameterException(
					"Currency informed on \"from\" parameter cannot be null and must have three characters.");
		}

		if (null == to || to.length() != 3) {
			throw new InvalidParameterException(
					"Currency informed on \"to\" parameter cannot be null and must have three characters.");
		}

		if (value.doubleValue() < 0) {
			throw new InvalidParameterException(
					"The amount informed on \"value\" parameter must be equal or greater than zero.");
		}

		if (null == quotation) {
			throw new InvalidParameterException("Quotation date cannot be null.");
		}

		Date quotationDate;
		try {
			quotationDate = new SimpleDateFormat("dd/MM/yyyy").parse(quotation);
		} catch (ParseException parseException) {
			throw new InvalidParameterException(
					"Could not parse quotation date \"" + quotation + "\". Is it on \"dd/MM/yyyy\" format?");
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(quotationDate);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		/*
		 * If a weekend day was informed, adjust the quotation date to its immediately
		 * preceding business day.
		 */
		int daysToSubtract = 0;
		if (dayOfWeek == Calendar.SATURDAY) {
			daysToSubtract = 1;
		} else {
			if (dayOfWeek == Calendar.SUNDAY) {
				daysToSubtract = 2;
			}
		}

		calendar.add(Calendar.DAY_OF_MONTH, -daysToSubtract);
		quotationDate = calendar.getTime();

		File csvFile = retrieveQuotationCsvFile(quotationDate);

		Map<String, CurrencyExchangingInformation> currencyExchangingInformationMap;
		currencyExchangingInformationMap = createExchangingInformationMap(csvFile);

		csvFile.delete();

		CurrencyExchangingInformation currencyExchangingInformationFrom = currencyExchangingInformationMap.get(from);
		if (null == currencyExchangingInformationFrom) {
			throw new RuntimeException("Could not find exchanging rates for \"" + from + "\" currency.");
		}

		CurrencyExchangingInformation currencyExchangingInformationTo = currencyExchangingInformationMap.get(to);
		if (null == currencyExchangingInformationTo) {
			throw new RuntimeException("Could not find exchanging rates for \"" + to + "\" currency.");
		}

		double exchangeRate = (currencyExchangingInformationFrom.getBuyingRate()
				/ currencyExchangingInformationTo.getBuyingRate());
		double convertedValue = Math.round((value.doubleValue() * exchangeRate) * 100.0) / 100.0;
		BigDecimal convertedValueBigDecimal = new BigDecimal(Double.toString(convertedValue));

		return convertedValueBigDecimal;
	}

	/**
	 * Retrieves the Real quotation CSV file of a specific date from brazilian
	 * central bank.
	 * 
	 * @param quotationDate
	 *            Date from which the quotation CSV file should be retrieved.
	 * @return The specified date's quotation CSV file.
	 * @throws IOException
	 *             When the quotation CSV file could not be retrieved.
	 */
	private final File retrieveQuotationCsvFile(Date quotationDate) throws IOException {

		/* Elaborates the URL to retrieve the CSV quotation. */
		String quotationCsvUrlString = BCB_CSV_BASE_URL;
		quotationCsvUrlString += new SimpleDateFormat("yyyyMMdd").format(quotationDate);
		quotationCsvUrlString += CSV_EXTENSION;

		URL quotationCsvUrl;
		try {
			quotationCsvUrl = new URL(quotationCsvUrlString);
		} catch (MalformedURLException malformedURLException) {
			throw new RuntimeException("Could not create an URL based on the string \"" + quotationCsvUrlString + "\".",
					malformedURLException);
		}

		/* Retrieves the CSV file. */
		URLConnection urlConnection;
		InputStream urlConnectionInputStream;
		IOException couldNotRetrieveCsvFileIoException = new IOException(
				"Could not retrieve Brazilian central bank quotation CSV file for "
						+ new SimpleDateFormat("dd/MM/yyyy").format(quotationDate) + ".");

		try {
			urlConnection = quotationCsvUrl.openConnection();
			urlConnectionInputStream = urlConnection.getInputStream();
		} catch (IOException ioException) {
			couldNotRetrieveCsvFileIoException.initCause(ioException);
			throw couldNotRetrieveCsvFileIoException;
		}

		String csvFileName = new SimpleDateFormat("yyyyMMdd").format(quotationDate);
		csvFileName += CSV_EXTENSION;
		File csvFile = new File(csvFileName);

		FileOutputStream fosQuotationCsvFile;
		try {
			fosQuotationCsvFile = new FileOutputStream(csvFile);
		} catch (FileNotFoundException fileNotFoundException) {
			throw new IOException("Could not open file \"" + csvFile.getAbsolutePath()
					+ "\" to retrieve Brazilian central bank quotation CSV file.", fileNotFoundException);
		}

		byte[] buffer = new byte[FILE_READING_BUFFER_SIZE];
		int bytesRead;

		try {
			boolean readConcluded = false;
			while (!readConcluded) {
				bytesRead = urlConnectionInputStream.read(buffer, 0, FILE_READING_BUFFER_SIZE);
				if (bytesRead == -1) {
					readConcluded = true;
				} else {
					fosQuotationCsvFile.write(buffer, 0, bytesRead);
				}
			}
			fosQuotationCsvFile.flush();
		} catch (IOException ioException) {
			couldNotRetrieveCsvFileIoException.initCause(ioException);
			throw couldNotRetrieveCsvFileIoException;
		} finally {
			try {
				urlConnectionInputStream.close();
			} catch (IOException ioException) {
				throw new IOException("Exception while closing connection with URL \"" + quotationCsvUrlString + "\".",
						ioException);
			}
			try {
				fosQuotationCsvFile.close();
			} catch (IOException ioException) {
				throw new IOException("Exception while closing CSV file \"" + csvFile.getAbsolutePath() + "\".",
						ioException);
			}
		}

		return csvFile;
	}

	/**
	 * creates a {@link HashMap} with the currencies' exchanging information based
	 * on the values read from a CSV file.
	 * 
	 * @param csvFile
	 *            The CSV file from which the currencies' exchanging information
	 *            will be retrieved.
	 * 
	 * @return A {@link HashMap} with the currencies' exchanging information
	 */
	private final Map<String, CurrencyExchangingInformation> createExchangingInformationMap(File csvFile)
			throws IOException {
		Map<String, CurrencyExchangingInformation> currencyExchangingInformationMap = new HashMap<>();

		/* Opens the CSV file scanner. */
		Scanner csvFileScanner;
		try {
			csvFileScanner = new Scanner(csvFile);
		} catch (FileNotFoundException fileNotFoundException) {
			throw new IOException("Could not find quotation CSV file \"" + csvFile + "\".", fileNotFoundException);
		}

		String lineBuffer;
		String[] values;
		Date exchangingDate;
		String currencyCode;
		String type;
		String currencyAbbreviation = null;
		Double buyingRate;
		Double sellingRate;
		Double buyingPpp;
		Double sellingPpp;
		CurrencyExchangingInformation currencyExchangingInformation;

		/*
		 * Since we're reading values acquired from a brazilian bank, the number parser
		 * must read then on the brazilian format (i. e. the decimal separator character
		 * is comma and the grouping separator character is dot).
		 */
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator(',');
		decimalFormatSymbols.setGroupingSeparator('.');

		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);

		try {
			while (csvFileScanner.hasNextLine()) {
				lineBuffer = csvFileScanner.nextLine();
				values = lineBuffer.split(CSV_SEPARATION_CHARACTER);
				currencyAbbreviation = values[3];
				currencyCode = values[1];
				type = values[2];
				buyingRate = decimalFormat.parse(values[4]).doubleValue();
				sellingRate = decimalFormat.parse(values[5]).doubleValue();
				buyingPpp = decimalFormat.parse(values[6]).doubleValue();
				sellingPpp = decimalFormat.parse(values[7]).doubleValue();
				exchangingDate = new SimpleDateFormat("dd/MM/yyyy").parse(values[0]);

				currencyExchangingInformation = new CurrencyExchangingInformation(exchangingDate, currencyCode, type,
						currencyAbbreviation, buyingRate, sellingRate, buyingPpp, sellingPpp);

				currencyExchangingInformationMap.put(currencyExchangingInformation.getCurrencyAbbreviation(),
						currencyExchangingInformation);
			}
		} catch (ParseException parseException) {
			throw new IOException("Could not parse an information for currency \"" + currencyAbbreviation
					+ "\" from CSV file \"" + csvFile.getAbsolutePath() + "\".", parseException);
		} finally {
			csvFileScanner.close();
		}

		return currencyExchangingInformationMap;
	}
}
