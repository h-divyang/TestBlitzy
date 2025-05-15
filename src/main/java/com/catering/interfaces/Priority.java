package com.catering.interfaces;

/**
 * Interface representing the priority of an entity.
 * This interface provides methods to get and set the priority level, 
 * which can be used for ordering or categorizing entities based on importance or precedence.
 */
public interface Priority {

	/**
	 * Gets the priority level of the entity.
	 *
	 * @return The priority level as an Integer.
	 */
	public Integer getPriority();

	/**
	 * Sets the priority level of the entity.
	 *
	 * @param priority The priority level to set, represented as an Integer.
	 */
	public void setPriority(Integer priority);

}