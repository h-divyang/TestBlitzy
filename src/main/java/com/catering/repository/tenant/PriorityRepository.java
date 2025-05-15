package com.catering.repository.tenant;

import java.util.Optional;

/**
 * Repository interface for accessing entities with a priority field.
 * 
 * This interface extends the generic repository to allow fetching entities sorted by priority.
 * It provides a custom method to retrieve the entity with the highest priority, ordered in descending order.
 *
 * @param <M> the type of the entity
 */
public interface PriorityRepository<M> {

	/**
	 * Finds the entity with the highest priority.
	 * 
	 * This method retrieves the entity with the highest priority, sorted in descending order based on the priority field.
	 * 
	 * @return an {@link Optional} containing the entity with the highest priority, or an empty {@link Optional} if no such entity exists
	 */
	Optional<M> findTop1ByOrderByPriorityDesc();

}