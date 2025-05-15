package com.catering.service.common;

import org.springframework.http.HttpStatus;

import com.catering.exception.RestException;

/**
 * Provides methods for throwing custom exceptions with specific HTTP status codes,
 * messages, and optional body data, facilitating consistent error handling throughout the application.
 */
public interface ExceptionService {

	/**
	 * Throws a RestException with an HTTP 500 Internal Server Error status.
	 *
	 * @throws RestException Always throws a RestException with HTTP 500 status.
	 */
	void throwInternalServerErrorRestException() throws RestException;

	/**
	 * Throws a RestException with the specified HTTP status and message.
	 *
	 * @param status The HTTP status to associate with the exception.
	 * @param message The message to include in the exception.
	 * @throws RestException Always throws a RestException with the provided HTTP status and message.
	 */
	void throwRestException(HttpStatus status, String message) throws RestException;

	/**
	 * Throws a RestException with the specified HTTP status, message, and an optional body.
	 *
	 * @param status The HTTP status to associate with the exception.
	 * @param message The message to include in the exception.
	 * @param o An optional object to include as the body of the exception.
	 * @throws RestException Always thrown with the provided HTTP status, message, and optional body.
	 */
	void throwRestException(HttpStatus status, String message, Object o) throws RestException;

	/**
	 * Throws a RestException with a HTTP 400 Bad Request status and the specified message.
	 *
	 * @param message The message to include in the exception.
	 * @throws RestException Always throws a RestException with HTTP 400 status and the provided message.
	 */
	void throwBadRequestException(String message) throws RestException;

	/**
	 * Throws a RestException with a HTTP 400 Bad Request status, the specified message, and an optional body.
	 *
	 * @param message The message to include in the exception.
	 * @param o An optional object to include as the body of the exception.
	 * @throws RestException Always thrown with HTTP 400 status, the provided message, and optional body.
	 */
	void throwBadRequestException(String message, Object o) throws RestException;

}