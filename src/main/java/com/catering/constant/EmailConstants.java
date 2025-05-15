package com.catering.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This class contains constants used for email-related operations.
 * These constants are used to reference email templates, subjects, and links.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailConstants {

	public static final String RESET_PASSWORD_LINK = "RESET_PASSWORD_LINK";

	public static final String TEMPLATE_RESET_PASSWORD = "reset-password";
	public static final String SUBJECT_RESET_PASSWORD = "Reset Jucas Account Password";

}