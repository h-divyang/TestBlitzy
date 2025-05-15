package com.catering.util;

import com.catering.constant.JasperConstantPatternForDecimalPoint;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for handling decimal patterns in Jasper reports.
 * 
 * <p>This class provides methods to determine the precision pattern
 * for amounts based on specific identifiers, ensuring consistent formatting
 * in Jasper reports.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * String pattern = DecimalPatternUtils.getPecisionPatternForAmountJasper(2);
 * // Returns the pattern for two decimal points.
 * }</pre>
 *
 * <p>This class is designed to be non-instantiable.</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DecimalPatternUtils {

	/**
	 * Retrieves the decimal precision pattern for amounts used in Jasper reports.
	 * 
	 * <p>This method provides a specific pattern based on the given ID, defaulting
	 * to a pattern with two decimal points if the ID does not match predefined cases.</p>
	 *
	 * @param id The identifier for the desired decimal precision. 
	 * @return The corresponding decimal precision pattern as a {@code String}.
	 */
	public static String getPecisionPatternForAmountJasper(int id) {
		return getDecimalPattern(id, JasperConstantPatternForDecimalPoint.TWO_DECIMAL);
	}

	/**
	 * Determines the decimal precision pattern based on the given ID.
	 * 
	 * <p>This method is used internally to map IDs to their corresponding
	 * precision patterns. If the ID is not recognized, it returns a default
	 * pattern specified by the caller.</p>
	 *
	 * @param id The identifier for the desired decimal precision.
	 * @param defaultDecimal The default pattern to return if the ID does not match predefined cases.
	 * @return The corresponding decimal precision pattern as a {@code String}.
	 */
	private static String getDecimalPattern(int id, String defaultDecimal) {
		switch (id) {
		case 0:
			return JasperConstantPatternForDecimalPoint.ZERO_DECIMAL;
		case 1:
			return JasperConstantPatternForDecimalPoint.ONE_DECIMAL;
		case 2:
			return JasperConstantPatternForDecimalPoint.TWO_DECIMAL;
		case 3:
			return JasperConstantPatternForDecimalPoint.THREE_DECIMAL;
		default:
			return defaultDecimal;
		}
	}

}