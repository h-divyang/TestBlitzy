package com.catering.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This class holds regular expression patterns used for validating various types of data.
 * These regex patterns can be used to validate input fields like GST number, website URL, email, etc.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegexConstant {

	public static final String GST_NUMBER_VALIDATION = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$";
	public static final String WEBSITE_VALIDATION = "^(?:(?:https?|ftp):\\/\\/)?(?:[a-zA-Z0-9]+\\.){1,}[a-zA-Z]{2,}(?:\\/[^\\s]*)?$";
	public static final String ONLY_NUMBERS_WITHOUT_SPACE = "^[0-9]+$";
	public static final String PERMITTED_IP ="^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$";
	public static final String ALPHA_NUMERIC_WITH_SPACE = "^[A-Za-z0-9 ]+$";
	public static final String ALPHA_NUMERIC_WITHOUT_SPACE = "^[A-Za-z0-9]+$";
	public static final String EMAIL = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
	public static final String UUID = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
	public static final String GST = "^\\d{2}[A-Z]{5}\\d{4}[A-Z]{1}[A-Z\\d]{1}[Z]{1}[A-Z\\d]{1}$";
	public static final String PAN = "^([a-zA-Z]){5}(\\d){4}([a-zA-Z]){1}?$";
	public static final String URL = "^[(http(s)?)://(www\\.)?a-zA-Z0-9@:%._\\+-~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)$";

}