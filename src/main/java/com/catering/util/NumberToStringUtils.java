package com.catering.util;

/**
 * Utility class for converting a given number (in the form of a {@link Long}) to its equivalent word representation
 * in the Indian numbering system, along with the suffix "Rupees Only".
 *
 * <p>This class supports conversion for amounts in Indian Rupees with places like hundred, thousand, lakh, crore, etc.</p>
 */
public class NumberToStringUtils {

	private static String amount;
	private static String[] units = { "", " One", " Two", " Three", " Four", " Five", " Six", " Seven", " Eight",
			" Nine" };
	private static String[] teen = { " Ten", " Eleven", " Twelve", " Thirteen", " Fourteen", " Fifteen", " Sixteen",
			" Seventeen", " Eighteen", " Nineteen" };
	private static String[] tens = { " Twenty", " Thirty", " Fourty", " Fifty", " Sixty", " Seventy", " Eighty",
			" Ninety" };
	private static String[] maxs = { "", "", " Hundred", " Thousand", " Lakh", " Crore", "Araba", "Karba"};

	/**
	 * Converts a given numeric amount into its word representation in the Indian numbering system.
	 * The output will be in the format: "X Rupees Only".
	 *
	 * @param n The numeric amount to be converted.
	 * @return A string representing the amount in words with the suffix "Rupees Only".
	 */
	public String convertToWords(Long n) {
		amount = numToString(n);
		String converted = "";
		int pos = 1;
		boolean hun = false;
		while (amount.length() > 0) {
			if (pos == 1) { // TENS AND UNIT POSITION
				if (amount.length() >= 2) { // 2DIGIT NUMBERS
					String C = amount.substring(amount.length() - 2, amount.length());
					amount = amount.substring(0, amount.length() - 2);
					converted += digits(C);
				} else if (amount.length() == 1) { // 1 DIGIT NUMBER
					converted += digits(amount);
					amount = "";
				}
				pos++; // INCREASING POSITION COUNTER
			} else if (pos == 2) { // HUNDRED POSITION
				String C = amount.substring(amount.length() - 1, amount.length());
				amount = amount.substring(0, amount.length() - 1);
				if (converted.length() > 0 && digits(C).toString().equals("")) {
					converted = (digits(C) + maxs[pos] + " and") + converted;
					hun = true;
				} else {
					if (digits(C).toString().equals(""))
						;
					else
						converted = (digits(C) + maxs[pos]) + converted;
					hun = true;
				}
				pos++; // INCREASING POSITION COUNTER
			} else if (pos > 2) { // REMAINING NUMBERS PAIRED BY TWO
				if (amount.length() >= 2) { // EXTRACT 2 DIGITS
					String C = amount.substring(amount.length() - 2, amount.length());
					amount = amount.substring(0, amount.length() - 2);
					if (!hun && converted.length() > 0)
						converted = digits(C) + maxs[pos] + " and" + converted;
					else {
						if (digits(C).toString().equals(""))
							;
						else
							converted = digits(C) + maxs[pos] + converted;
					}
				} else if (amount.length() == 1) { // EXTRACT 1 DIGIT
					if (!hun && converted.length() > 0)
						converted = digits(amount) + maxs[pos] + " and" + converted;
					else {
						if (digits(amount).toString().equals(""))
							;
						else
							converted = digits(amount) + maxs[pos] + converted;
						amount = "";
					}
				}
				pos++; // INCREASING POSITION COUNTER
			}
		}
		if (converted.trim().equalsIgnoreCase("")) {
			return "Zero Ruppes Only";
		} else {
			return converted.trim() + " Rupees Only";
		}
	}

	/**
	 * Converts a numeric string to its corresponding word representation, such as "One", "Twenty", etc.
	 *
	 * @param C The string representing the numeric value to be converted.
	 * @return The corresponding word representation of the numeric value.
	 */
	private String digits(String C) { // TO RETURN SELECTED NUMBERS IN WORDS
		String converted = "";
		for (int i = C.length() - 1; i >= 0; i--) {
			int ch = C.charAt(i) - 48;
			if (i == 0 && ch > 1 && C.length() > 1)
				converted = tens[ch - 2] + converted; // IF TENS DIGIT STARTS WITH 2 OR MORE IT FALLS UNDER TENS
			else if (i == 0 && ch == 1 && C.length() == 2) { // IF TENS DIGIT STARTS WITH 1 IT FALLS UNDER TEENS
				int sum = 0;
				for (int j = 0; j < 2; j++)
					sum = (sum * 10) + (C.charAt(j) - 48);
				return teen[sum - 10];
			} else {
				if (ch > 0)
					converted = units[ch] + converted;
			} // IF SINGLE DIGIT PROVIDED
		}
		return converted;
	}

	/**
	 * Converts a numeric value to a string representation.
	 *
	 * @param n The number to be converted to string.
	 * @return The string representation of the number.
	 */
	private String numToString(Long n) { // CONVERT THE NUMBER TO STRING
		String num = "";
		while (n != 0) {
			num = ((char) ((n % 10) + 48)) + num;
			n /= 10;
		}
		return num;
	}

}