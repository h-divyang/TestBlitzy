package com.catering.util;

import java.text.DecimalFormat;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for performing common number operations such as extracting values 
 * and rounding off decimal values.
 *
 * <p>This class provides methods to handle numbers safely and format them 
 * appropriately for display.</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NumberUtils {

	/**
	 * Extracts the value from the provided number. If the number is {@code null}, it returns 0.
	 * 
	 * @param value The number to extract the value from.
	 * @return The provided number if non-null, or 0 if the number is null.
	 */
	public static Number extractValue(Number value) {
		return Objects.nonNull(value) ? value : 0;
	}

	/**
	 * Rounds off the provided number to two decimal places and returns it as a {@link String}.
	 * 
	 * <p>This method uses the {@link DecimalFormat} class to format the number with two decimal places.</p>
	 *
	 * @param number The number to be rounded off.
	 * @return A {@link String} representing the rounded number with two decimal places.
	 */
	public static String roundOffTwoDecimalPlaces(Number number) {
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		return decimalFormat.format(number);
	}

}