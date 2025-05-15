package com.catering.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This class holds constant values for formatting patterns for decimal points
 * used in Jasper reports. These constants define the patterns for formatting 
 * numbers with varying numbers of decimal places.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JasperConstantPatternForDecimalPoint {

	public static final String ZERO_DECIMAL = "###,###,##0";
	public static final String ONE_DECIMAL = "###,###,##0.0";
	public static final String TWO_DECIMAL = "###,###,##0.00";
	public static final String THREE_DECIMAL = "###,###,##0.000";
	public static final String FOUR_DECIMAL = "###,###,##0.0000";

}