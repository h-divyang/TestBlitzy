package com.catering.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Utility class for managing and generating error details in a structured way.
 * <p>
 * This class provides a convenient way to track errors by storing them in a map where the key is a description or identifier for the error 
 * and the value is the error information. The class allows adding errors, checking if errors exist, and retrieving them.
 * </p>
 * <p>
 * The class uses Lombok annotations to automatically generate getter methods and the constructor.
 * </p>
 * 
 * <b>Methods:</b>
 * <ul>
 * <li><b>{@code builder()}</b>: Initializes a new instance of the {@link ErrorGenerator} class with an empty error map.</li>
 * <li><b>{@code putError(String key, Object value)}</b>: Adds an error with a specific key and value to the error map.</li>
 * <li><b>{@code hasError()}</b>: Checks if any errors have been added to the error map.</li>
 * </ul>
 */
@Getter
@AllArgsConstructor
public class ErrorGenerator {

	/**
	 * A map that holds the error details, where the key is the error identifier and the value is the error information.
	 */
	private Map<String, Object> errors;

	/**
	 * Initializes a new instance of the {@link ErrorGenerator} with an empty error map.
	 * 
	 * @return A new instance of {@link ErrorGenerator}.
	 */
	public static ErrorGenerator builder() {
		return new ErrorGenerator(new HashMap<>());
	}

	/**
	 * Adds an error with a specific key and value to the error map.
	 * 
	 * @param key The key representing the error (e.g., an error code or field identifier).
	 * @param value The error value (e.g., a message or object containing error details).
	 * @return The current instance of {@link ErrorGenerator} with the added error.
	 */
	public ErrorGenerator putError(String key, Object value) {
		if (Objects.nonNull(errors)) {
			errors.put(key, value);
		}
		return this;
	}

	/**
	 * Checks if any errors have been added to the error map.
	 * 
	 * @return {@code true} if the error map contains any errors, {@code false} otherwise.
	 */
	public boolean hasError() {
		return Objects.nonNull(errors) && !errors.isEmpty();
	}

}