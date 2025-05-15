package com.catering.service.superadmin;

import java.util.Optional;
import com.catering.exception.RestException;

/**
 * Common interface for creating or updating data objects.
 *
 * This interface defines a method to either create new data objects or update existing ones.
 *
 * @param <D> The type of the data object.
 */
public interface CommonDataService<D> {

	/**
	 * Creates or updates a data object.
	 *
	 * This method allows the creation of a new data object or updating an existing one.
	 * The input is an object of type {@code D}, and the method returns an optional
	 * containing the created or updated object. If the operation fails or does not produce
	 * a valid result, an empty optional is returned.
	 *
	 * @param dto The data transfer object of type {@code D} to be created or updated.
	 * @return An {@code Optional<D>} containing the created or updated data object.
	 * @throws RestException If there is an error during the creation or update process.
	 */
	Optional<D> createAndUpdate(D dto) throws RestException;

}