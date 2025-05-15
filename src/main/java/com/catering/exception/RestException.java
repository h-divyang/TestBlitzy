package com.catering.exception;

import org.springframework.http.HttpStatus;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Custom exception class for representing HTTP errors in RESTful applications.
 * This class extends {@link RuntimeException} and provides additional details like
 * HTTP status, error message, and an optional body with additional information.
 * 
 * <p>Instances of this class are typically thrown when an error occurs during
 * the processing of an HTTP request, and it provides relevant error details to
 * be included in the response sent to the client.</p>
 * 
 * <p>The exception contains the following properties:
 * <ul>
 * <li>HTTP status: The HTTP status code associated with the error.</li>
 * <li>Message: A description of the error or issue.</li>
 * <li>Body: An optional object that can carry additional details related to the error (such as validation errors, etc.).</li>
 * </ul>
 * </p>
 * 
 * <p>Example usage:</p>
 * <pre>throw new RestException(HttpStatus.NOT_FOUND, "Resource not found");</pre>
 * or
 * <pre>
 * throw new RestException(HttpStatus.BAD_REQUEST, "Invalid input", errorDetails);
 * </pre>
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class RestException extends RuntimeException {

	private static final long serialVersionUID = 337819360079626152L;

	private final HttpStatus status;
	private final String message;
	private final transient Object body;

	/**
	 * Constructor for creating a {@link RestException} with only a status and message.
	 * 
	 * @param status the HTTP status code associated with the error
	 * @param message the error message
	 */
	public RestException(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
		this.body = null;
	}

	/**
	 * Constructor for creating a {@link RestException} with a status, message, and additional error details.
	 * 
	 * @param status the HTTP status code associated with the error
	 * @param message the error message
	 * @param o additional error details (optional)
	 */
	public RestException(HttpStatus status, String message, Object o) {
		this.status = status;
		this.message = message;
		this.body = o;
	}

}