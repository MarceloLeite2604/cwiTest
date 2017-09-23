package org.marceloleite.cwitest;

import java.util.Date;

/**
 * Exchanging rates information from Brazilian Real to another currency on a
 * given date.
 * 
 * @author Marcelo Leite
 *
 */
public class CurrencyExchangingInformation {

	/**
	 * The exchanging date.
	 */
	private Date exchangingDate;

	/**
	 * The currency identification code.
	 */
	private String currencyCode;

	/**
	 * The currency type.
	 */
	private String type;

	/**
	 * The currency abbreviation.
	 */
	private String currencyAbbreviation;

	/**
	 * The currency's buying rate.
	 */
	private double buyingRate;

	/**
	 * The currency's selling rate.
	 */
	private double sellingRate;

	/**
	 * The currency's buying purchasing power parity.
	 */
	private double buyingPpp;

	/**
	 * The currency's selling purchasing power parity.
	 */
	private double sellingPpp;

	/**
	 * Creates a new {@link CurrencyExchangingInformation} object.
	 * 
	 * @param exchangingDate
	 *            The exchanging date.
	 * @param currencyCode
	 *            The currency identification code.
	 * @param type
	 *            The currency type.
	 * @param currencyAbbreviation
	 *            The currency abbreviation.
	 * @param buyingRate
	 *            The currency's buying rate.
	 * @param sellingRate
	 *            The currency's selling rate.
	 * @param buyingPpp
	 *            The currency's buying purchasing power parity
	 * @param sellingPpp
	 *            The currency's selling purchasing power parity.
	 */
	public CurrencyExchangingInformation(Date exchangingDate, String currencyCode, String type,
			String currencyAbbreviation, double buyingRate, double sellingRate, double buyingPpp, double sellingPpp) {
		this.exchangingDate = exchangingDate;
		this.currencyCode = currencyCode;
		this.type = type;
		this.currencyAbbreviation = currencyAbbreviation;
		this.buyingRate = buyingRate;
		this.sellingRate = sellingRate;
		this.buyingPpp = buyingPpp;
		this.sellingPpp = sellingPpp;
	}

	/**
	 * Returns the exchanging date.
	 * 
	 * @return The exchanging date.
	 */
	public final Date getExchangingDate() {
		return exchangingDate;
	}

	/**
	 * Returns the currency identification code.
	 * 
	 * @return The currency identification code.
	 */
	public final String getCurrencyCode() {
		return currencyCode;
	}

	/**
	 * Returns the currency type.
	 * 
	 * @return The currency type.
	 */
	public final String getType() {
		return type;
	}

	/**
	 * Returns the currency abbreviation.
	 * 
	 * @return The currency abbreviation.
	 */
	public final String getCurrencyAbbreviation() {
		return currencyAbbreviation;
	}

	/**
	 * Returns the currency buying rate.
	 * 
	 * @return The currency buying rate.
	 */
	public final double getBuyingRate() {
		return buyingRate;
	}

	/**
	 * Returns the currency selling rate.
	 * 
	 * @return The currency selling rate.
	 */
	public final double getSellingRate() {
		return sellingRate;
	}

	/**
	 * Returns the currency's buying purchasing power parity
	 * 
	 * @return The currency's buying purchasing power parity
	 */
	public final double getBuyingPpp() {
		return buyingPpp;
	}

	/**
	 * Returns the currency's selling purchasing power parity
	 * 
	 * @return The currency's selling purchasing power parity
	 */
	public final double getSellingPpp() {
		return sellingPpp;
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(currencyAbbreviation + " - ");
		stringBuffer.append("Type: " + type + ", ");
		stringBuffer.append("buying rate: " + buyingRate + ", ");
		stringBuffer.append("buying ppp: " + buyingPpp + ".");
		return stringBuffer.toString();
	}

}
