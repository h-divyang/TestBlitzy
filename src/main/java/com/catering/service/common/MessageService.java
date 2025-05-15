package com.catering.service.common;

/**
 * Interface providing methods to retrieve and format messages based on unique keys.
 */
public interface MessageService {

	/**
	 * Retrieves a message associated with the specified key.
	 *
	 * @param key The unique identifier for the message to be retrieved.
	 * @return A string containing the message corresponding to the provided key.
	 */
	String getMessage(String key);

	/**
	 * Retrieves a formatted message associated with the specified key, with optional arguments to fill place holders in the message.
	 *
	 * @param key The unique identifier for the message to be retrieved.
	 * @param args Optional arguments to replace place holders in the message, if applicable.
	 * @return A string containing the formatted message corresponding to the provided key,
	 * potentially including resolved placeholders based on the arguments.
	 */
	String getMessage(String key, Object ...args);

}