package com.catering.service.common;

import com.catering.bean.EmailDetails;

/**
 * Provides methods for sending email, including plain text email and template-based email.
 */
public interface EmailService {

	/**
	 * Sends a plain text email based on the provided EmailDetails object.
	 *
	 * @param emailDetailsBean The EmailDetails object containing the recipient, subject, and message for the email.
	 * @return A boolean value indicating the success or failure of the email sending process.
	 */
	boolean sendMailText(EmailDetails emailDetailsBean);

	/**
	 * Sends an email using a predefined template based on the given EmailDetails object.
	 *
	 * @param emailDetailsBean The EmailDetails object containing the recipient's information.
	 * @return A boolean value indicating whether the email was successfully sent (true) or if the process failed (false).
	 */
	boolean sendMailTemplet(EmailDetails emailDetailsBean);

}