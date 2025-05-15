package com.catering.bean;

import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Class representing the details required to send an email.
 * <p>
 * This class encapsulates the necessary information to construct and send an email. It includes fields such as the recipient's email address, 
 * subject, message body, the template to use (if any), and additional properties (e.g., attachments or dynamic content).
 * </p>
 * <p>
 * The class uses Lombok annotations to automatically generate common methods such as getters, constructors, and builders.
 * </p>
 * 
 * <b>Fields:</b>
 * <ul>
 * <li><b>{@code to}</b>: The recipient's email address.</li>
 * <li><b>{@code subject}</b>: The subject line of the email.</li>
 * <li><b>{@code message}</b>: The content of the email (body).</li>
 * <li><b>{@code templet}</b>: The email template to use for rendering the message, if applicable.</li>
 * <li><b>{@code properties}</b>: A map of additional properties that can be used for dynamic content (e.g., personalized content or attachments).</li>
 * </ul>
 * 
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailDetails {

	/**
	 * The recipient's email address.
	 */
	private String to;

	/**
	 * The subject of the email.
	 */
	private String subject;

	/**
	 * The message content of the email.
	 */
	private String message;

	/**
	 * The email template to use (if applicable).
	 */
	private String templet;

	/**
	 * A map of additional properties, such as dynamic content or attachments.
	 */
	private Map<String, Object> properties;

}