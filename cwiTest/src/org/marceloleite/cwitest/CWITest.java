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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
	 * Entry method used for testing purposes.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		BigDecimal returnValue;

		CWITest cwiTest = new CWITest();
		try {
			returnValue = cwiTest.currencyQuotation("UDS", "BRL", 12, "18/09/2017");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

		Date quotationDate;
		try {
			quotationDate = new SimpleDateFormat("dd/MM/yyyy").parse(quotation);
		} catch (ParseException parseException) {
			throw new RuntimeException(
					"Could not parse quotation date \"" + quotation + "\". Is it on \"dd/MM/yyyy format?",
					parseException);
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

		return null;
	}

	/**
	 * TODO: Document.
	 * 
	 * @param quotationDate
	 * @return
	 * @throws IOException
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
}
